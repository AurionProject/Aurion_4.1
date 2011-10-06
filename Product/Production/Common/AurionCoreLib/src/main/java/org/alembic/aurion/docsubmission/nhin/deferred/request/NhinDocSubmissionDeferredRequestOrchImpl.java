/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.nhin.deferred.request;

import java.sql.Blob;
import org.alembic.aurion.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxy;
import org.alembic.aurion.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory;
import org.alembic.aurion.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxy;
import org.alembic.aurion.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docsubmission.NhinDocSubmissionUtils;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.XDRPolicyChecker;
import org.alembic.aurion.gateway.lift.StartLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.StartLiftTransactionResponseType;
import javax.xml.bind.Marshaller;
import org.alembic.aurion.lift.dao.GatewayLiftMessageDao;
import org.alembic.aurion.lift.model.GatewayLiftMsgRecord;
import org.alembic.aurion.lift.payload.builder.LiFTPayloadBuilder;
import org.alembic.aurion.lift.proxy.GatewayLiftManagerProxy;
import org.alembic.aurion.lift.proxy.GatewayLiftManagerProxyObjectFactory;
import org.alembic.aurion.lift.utils.LiFTMessageHelper;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.LIFTMessageType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredRequestOrchImpl {

    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed";
    private static final Log logger = LogFactory.getLog(NhinDocSubmissionDeferredRequestOrchImpl.class);

    /**
     *
     * @return
     */
    protected Log getLogger() {
        return logger;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        XDRAcknowledgementType result = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        result.setMessage(regResp);
        AcknowledgementType ack = null;
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");
        if (isNhinAudit) {
            ack = getXDRAuditLogger().auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
            getLogger().debug("Audit Log Ack Message:" + ack.getMessage());
        }

        String localHCID = retrieveHomeCommunityID();

        getLogger().debug("Local Home Community ID: " + localHCID);

        // Check if the Patient Discovery Async Request Service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID)) {
                    getLogger().debug("Policy Check Succeeded");

                    // Check to see if this message contains a LiFT Payload and that we support LiFT
                    if (isLiftMessage(body) && checkLiftProperty()) {
                        result = processLiftMessage(body, assertion);
                    } else {
                        result = forwardToAgency(body, assertion);
                    }
                } else {
                    getLogger().error("Policy Check Failed");
                    result = sendErrorToAgency(body, assertion, "Policy Check Failed");
                }
            } else {
                result = forwardToAgency(body, assertion);
            }
        } else {
            getLogger().warn("Document Submission Request Service is not enabled");
            result = sendErrorToAgency(body, assertion, "Policy Check Failed");
        }
        if (isNhinAudit) {
            ack = getXDRAuditLogger().auditAcknowledgement(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
            getLogger().debug("Audit Log Ack Message for Outbound Acknowledgement:" + ack.getMessage());
        }

        logger.debug(
                "Exiting provideAndRegisterDocumentSetBRequest");
        return result;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Request Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinDocSubmissionUtils.isServiceEnabled(NhincConstants.DOC_SUBMISSION_DEFERRED_REQ_SERVICE_PROP);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return NhinDocSubmissionUtils.isInPassThroughMode(NhincConstants.DOC_SUBMISSION_DEFERRED_REQ_PASSTHRU_PROP);
    }

    /**
     *
     * @return
     */
    protected String retrieveHomeCommunityID() {
        String localHCID = null;
        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            logger.error("Exception while retrieving home community ID", ex);
        }

        return localHCID;
    }

    /**
     *
     * @return
     */
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    /**
     *
     * @param body
     * @param context
     * @return
     */
    protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        getLogger().debug("Entering forwardToAgency");
        AcknowledgementType ack = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        if (isAdapterAudit) {
            ack = getXDRAuditLogger().auditXDRAdapterReq(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        }
        AdapterDocSubmissionDeferredRequestProxyObjectFactory factory = new AdapterDocSubmissionDeferredRequestProxyObjectFactory();

        AdapterDocSubmissionDeferredRequestProxy proxy = factory.getAdapterDocSubmissionDeferredRequestProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(body, null, assertion);
        if (isAdapterAudit) {
            ack = getXDRAuditLogger().auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }
        getLogger().debug("Exiting forwardToAgency");

        return response;
    }

    protected XDRAcknowledgementType sendErrorToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String errMsg) {

        AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory factory = new AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory();
        AdapterDocSubmissionDeferredRequestErrorProxy proxy = factory.getAdapterDocSubmissionDeferredRequestErrorProxy();

        XDRAcknowledgementType adapterResp = proxy.provideAndRegisterDocumentSetBRequestError(body, errMsg, assertion);

        return adapterResp;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean isPolicyOk = false;

        getLogger().debug("Check policy");

        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        isPolicyOk =
                policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        getLogger().debug("Response from policy engine: " + isPolicyOk);

        return isPolicyOk;

    }

    /**
     * The method will determine if the input message contains a LiFT request
     * @param request  The input message
     * @return  true if the url field of the request message is specified, otherwise will return false
     */
    protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
        boolean result = false;
        ExtrinsicObjectType extObj = null;

        // Check to see if a url was provided in the message and if LiFT is supported
        if (request != null &&
                request.getSubmitObjectsRequest() != null &&
                request.getSubmitObjectsRequest().getRegistryObjectList() != null) {
            RegistryObjectListType regObjList = request.getSubmitObjectsRequest().getRegistryObjectList();

            // Extract the ExtrinsicObjectType from the Registry List
            extObj =
                    LiFTMessageHelper.extractExtrinsicObject(regObjList);

            if (extObj != null) {

                for (SlotType1 slot : extObj.getSlot()) {
                    if (NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_NAME.equalsIgnoreCase(slot.getName()) ||
                            NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_NAME.equalsIgnoreCase(slot.getName())) {
                        result = true;
                        break;

                    }


                }
            }
        }

        logger.debug("isLiftMessage returning: " + result);
        return result;
    }

    /**
     * This method returns the value of the property that determines whether LiFT transfers are enabled or not
     * @return  true if LiFT Transforms are enabled, false if they are not
     */
    protected boolean checkLiftProperty() {
        boolean result = false;

        // Check the property file to see if LiFT is supported
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_ENABLED_PROPERTY_NAME);
        } catch (PropertyAccessException ex) {
            logger.error("Error: Failed to retrieve " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            logger.error(ex.getMessage());
        }

        logger.debug("Obtained value of the " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + "property: " + result);
        return result;
    }

    protected XDRAcknowledgementType processLiftMessage(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        String guid = null;

        // Add an entry to the Gateway Lift Database
        guid =
                addEntryToDatabase(request, assertion);

        // Send a notification to the Gateway Lift Manager that an entry is ready to be processed
        sendNotificationToLiftManager(guid);

        // Retrun the ack to the Initiating Gateway
        return ack;
    }

    private String addEntryToDatabase(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        String guid = null;
        GatewayLiftMessageDao dbDao = new GatewayLiftMessageDao();
        List<GatewayLiftMsgRecord> dbRecList = new ArrayList<GatewayLiftMsgRecord>();
        GatewayLiftMsgRecord dbRec = new GatewayLiftMsgRecord();

        if (request != null &&
                assertion != null &&
                NullChecker.isNotNullish(request.getDocument()) &&
                request.getDocument().get(0) != null) {
            LiFTPayloadBuilder payloadBuilder = new LiFTPayloadBuilder();
            LIFTMessageType liftMsg = payloadBuilder.extractLiftPayload(request.getDocument().get(0));

            // Create the database record
            if (liftMsg != null &&
                    liftMsg.getDataElement() != null &&
                    liftMsg.getDataElement().getClientData() != null &&
                    NullChecker.isNotNullish(liftMsg.getDataElement().getClientData().getClientData()) &&
                    liftMsg.getRequestElement() != null &&
                    NullChecker.isNotNullish(liftMsg.getRequestElement().getRequestGuid()) &&
                    liftMsg.getDataElement().getServerProxyData() != null &&
                    NullChecker.isNotNullish(liftMsg.getDataElement().getServerProxyData().getServerProxyAddress())) {
                dbRec.setAssertion(createAssertionBlob(assertion));
                dbRec.setFileNameToRetrieve(liftMsg.getDataElement().getClientData().getClientData());
                dbRec.setInitialEntryTimestamp(new Date());
                dbRec.setMessage(createMsgBlob(request));
                dbRec.setMessageState(NhincConstants.LIFT_GATEWAY_MESSAGE_DB_STATE_ENTERED);
                dbRec.setMessageType(NhincConstants.LIFT_GATEWAY_MESSAGE_DB_TYPE_DOC_SUB);
                dbRec.setProducerProxyAddress(liftMsg.getDataElement().getServerProxyData().getServerProxyAddress());
                Long longObj = new Long(liftMsg.getDataElement().getServerProxyData().getServerProxyPort());
                dbRec.setProducerProxyPort(longObj);

                guid =
                        liftMsg.getRequestElement().getRequestGuid();
                dbRec.setRequestKeyGuid(guid);
                dbRecList.add(dbRec);

                if (dbDao.insertRecords(dbRecList) == false) {
                    logger.error("Failed to insert LifT record in the database");
                    guid =
                            null;
                }

            }
        }

        return guid;
    }

    private Blob createAssertionBlob(AssertionType assertion) {
        Blob data = null;
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (assertion != null) {
            baOutStrm.reset();

            // Marshall the Assertion Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("org.alembic.aurion.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                org.alembic.aurion.common.nhinccommon.ObjectFactory factory = new org.alembic.aurion.common.nhinccommon.ObjectFactory();
                JAXBElement oJaxbElement = factory.createAssertion(assertion);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            logger.debug("Byte Array: " + baOutStrm.toString());

            data =
                    Hibernate.createBlob(buffer);
        }

        return data;
    }

    private Blob createMsgBlob(ProvideAndRegisterDocumentSetRequestType msg) {
        Blob data = null;
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (msg != null) {
            baOutStrm.reset();

            // Marshall the Message Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
                JAXBElement oJaxbElement = factory.createProvideAndRegisterDocumentSetRequest(msg);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            logger.debug("Byte Array: " + baOutStrm.toString());

            data =
                    Hibernate.createBlob(buffer);
        }

        return data;
    }

    private StartLiftTransactionResponseType sendNotificationToLiftManager(String guid) {
        GatewayLiftManagerProxyObjectFactory factory = new GatewayLiftManagerProxyObjectFactory();
        GatewayLiftManagerProxy proxy = factory.getGatewayLiftManagerProxy();

        StartLiftTransactionRequestType startRequest = new StartLiftTransactionRequestType();
        startRequest.setRequestKeyGuid(guid);

        StartLiftTransactionResponseType resp = proxy.startLiftTransaction(startRequest);

        return resp;
    }

    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            logger.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
