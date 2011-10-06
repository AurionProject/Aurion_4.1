/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.request;

import org.alembic.aurion.asyncmsgs.dao.AsyncMsgRecordDao;
import org.alembic.aurion.asyncmsgs.model.AsyncMsgRecord;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201305Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryAuditLogger;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryPolicyChecker;
import org.alembic.aurion.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxy;
import org.alembic.aurion.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxyObjectFactory;
import org.alembic.aurion.transform.subdisc.HL7AckTransforms;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import java.sql.Blob;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.hl7.v3.II;
import org.hibernate.Hibernate;

public class EntityPatientDiscoveryDeferredRequestOrchImpl
{

    private Log log = null;

    public EntityPatientDiscoveryDeferredRequestOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PatientDiscoveryAuditLogger createAuditLogger()
    {
        return new PatientDiscoveryAuditLogger();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message,
            AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        PatientDiscovery201305Processor pd201305Processor = new PatientDiscovery201305Processor();

        if (message != null &&
                assertion != null)
        {
            // Audit the Patient Discovery Request Message sent on the Entity Interface
            PatientDiscoveryAuditLogger auditLog = createAuditLogger();

            RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
            unsecureRequest.setNhinTargetCommunities(targets);
            unsecureRequest.setPRPAIN201305UV02(message);
            unsecureRequest.setAssertion(assertion);
            boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
            if (isEntityAudit) {
                auditLog.auditEntity201305(unsecureRequest, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
            }

            urlInfoList = getTargets(targets);

            //loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null)
            {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo())
                {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, urlInfo.getHcid());

                    newRequest.setAssertion(assertion);
                    newRequest.setPRPAIN201305UV02(new201305);
                    newRequest.setNhinTargetCommunities(targets);

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk)
                    {
                        addEntryToDatabase(newRequest);

                        ack = sendToProxy(newRequest, urlInfo);
                    } else
                    {
                        ack = HL7AckTransforms.createAckFrom201305(message, "Policy Failed");
                    }
                }
            } else
            {
                log.warn("No targets were found for the Patient Discovery Request");
                ack = HL7AckTransforms.createAckFrom201305(message, "No Targets Found");
            }

            if (isEntityAudit) {
                auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
            }

        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
    {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try
        {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo)
    {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        PassthruPatientDiscoveryDeferredRequestProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryDeferredRequestProxyObjectFactory();
        PassthruPatientDiscoveryDeferredRequestProxy proxy = patientDiscoveryFactory.getPassthruPatientDiscoveryDeferredRequestProxy();

        log.debug("Invoking " + proxy + ".processPatientDiscoveryAsyncReq with " + request.getPRPAIN201305UV02()
                + " assertion: " + request.getAssertion() + " and target " + oTargetSystemType + " url: " + oTargetSystemType.getUrl());
        resp = proxy.processPatientDiscoveryAsyncReq(request.getPRPAIN201305UV02(), request.getAssertion(), oTargetSystemType);

        return resp;
    }

    protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord rec = new AsyncMsgRecord();
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        // Replace with message id from the assertion class
        rec.setMessageId(request.getAssertion().getMessageId());
        rec.setCreationTime(new Date());
        rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        rec.setMsgData(createBlob(request));
        asyncMsgRecs.add(rec);

        boolean result = instance.insertRecords(asyncMsgRecs);

        if (result == false)
        {
            log.error("Failed to insert asynchronous record in the database");
        }
    }

    private Blob createBlob(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        Blob data = null;

        PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (request != null &&
                request.getPRPAIN201305UV02() != null)
        {
            II patId = msgProcessor.extractPatientIdFrom201305(request.getPRPAIN201305UV02());
            baOutStrm.reset();

            try
            {
                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(baOutStrm);
                try
                {
                    // Write object.
                    xenc.writeObject(patId);
                    xenc.flush();
                } finally
                {
                    xenc.close();
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            log.debug("Byte Array: " + baOutStrm.toString());

            data = Hibernate.createBlob(buffer);
        }

        return data;
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
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
