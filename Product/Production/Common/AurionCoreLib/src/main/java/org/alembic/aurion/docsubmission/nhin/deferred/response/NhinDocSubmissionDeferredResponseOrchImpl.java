/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.docsubmission.nhin.deferred.response;

import org.alembic.aurion.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy;
import org.alembic.aurion.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxyObjectFactory;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docsubmission.NhinDocSubmissionUtils;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.XDRPolicyChecker;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseOrchImpl {

    private static final Log logger = LogFactory.getLog(NhinDocSubmissionDeferredResponseOrchImpl.class);

    /**
     *
     * @return
     */
    protected Log getLogger() {
        return logger;
    }

    /**
     * 
     * @param body
     * @param assertion
     * @return XDRAcknowledgementType
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion) {
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        XDRAcknowledgementType result = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        result.setMessage(regResp);
        AcknowledgementType ack = null;
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");
        if (isNhinAudit) {
            ack = getXDRAuditLogger().auditNhinXDRResponse(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        }

        getLogger().debug("Audit Log Ack Message:" + ack.getMessage());

        String localHCID = retrieveHomeCommunityID();

        getLogger().debug("Local Home Community ID: " + localHCID);

        // Check if the Patient Discovery Async Request Service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID)) {
                    getLogger().debug("Policy Check Succeeded");
                    result = forwardToAgency(body, assertion);
                } else {
                    getLogger().error("Policy Check Failed");
                }
            } else {
                result = forwardToAgency(body, assertion);
            }
        } else {
            getLogger().warn("Document Submission Response Service is not enabled");
        }
        if (isNhinAudit) {
            ack = getXDRAuditLogger().auditAcknowledgement(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        getLogger().debug("Audit Log Ack Message for Outbound Acknowledgement:" + ack.getMessage());

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return result;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Request Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinDocSubmissionUtils.isServiceEnabled(NhincConstants.DOC_SUBMISSION_DEFERRED_RESP_SERVICE_PROP);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return NhinDocSubmissionUtils.isInPassThroughMode(NhincConstants.DOC_SUBMISSION_DEFERRED_RESP_PASSTHRU_PROP);
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
    protected XDRAcknowledgementType forwardToAgency(RegistryResponseType body, AssertionType assertion) {
        getLogger().debug("Entering forwardToAgency");
        AcknowledgementType ack = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        if (isAdapterAudit) {
            ack = getXDRAuditLogger().auditAdapterXDRResponse(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        }

        AdapterDocSubmissionDeferredResponseProxyObjectFactory factory = new AdapterDocSubmissionDeferredResponseProxyObjectFactory();

        AdapterDocSubmissionDeferredResponseProxy proxy = factory.getAdapterDocSubmissionDeferredResponseProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBResponse(body, assertion);
        if (isAdapterAudit) {
            ack = getXDRAuditLogger().auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }
        getLogger().debug("Exiting forwardToAgency");

        return response;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(RegistryResponseType request, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean isPolicyOk = false;

        getLogger().debug("Check policy");

        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        isPolicyOk = policyChecker.checkXDRResponsePolicy(request, assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        getLogger().debug("Response from policy engine: " + isPolicyOk);

        return isPolicyOk;

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
