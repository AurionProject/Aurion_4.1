/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.nhin.deferred.request;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.docretrieve.DocRetrieveDeferredAuditLogger;
import org.alembic.aurion.docretrieve.adapter.deferred.request.error.proxy.AdapterDocRetrieveDeferredReqErrorProxy;
import org.alembic.aurion.docretrieve.adapter.deferred.request.error.proxy.AdapterDocRetrieveDeferredReqErrorProxyObjectFactory;
import org.alembic.aurion.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxy;
import org.alembic.aurion.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxyObjectFactory;
import org.alembic.aurion.docretrieve.nhin.deferred.NhinDocRetrieveDeferred;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 1:50:15 PM
 */
public class NhinDocRetrieveDeferredReqOrchImpl extends NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredReqOrchImpl.class);

    /**
     *
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body,
                                                                  AssertionType  assertion) {
        DocRetrieveAcknowledgementType  response = null;
        DocRetrieveDeferredAuditLogger  auditLog = new DocRetrieveDeferredAuditLogger();
        String errMsg = null;
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        if (isNhinAudit) {
            auditLog.auditDocRetrieveDeferredRequest(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        }
        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_KEY)) {
                if (isInPassThroughMode(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_REQUEST_SERVICE_PASSTHRU_PROPERTY)) {
                    response = sendDocRetrieveDeferredRequestToAgency(body, assertion);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
           } else {
                errMsg = "Doc retrieve deferred request service is not enabled";
                log.error(errMsg);
                response = sendToAgencyErrorInterface(body, assertion, errMsg);
            }
        } catch (Throwable t) {
            errMsg = "Error processing NHIN Doc Retrieve: " + t.getMessage();
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);

            response = sendToAgencyErrorInterface(body, assertion, errMsg);
        }

        if (isNhinAudit) {
            auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(),
                    assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }
        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return
     */
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request,
                                                                      AssertionType assertion,
                                                                      String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();
        String errMsg = null;

        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValidForRequest(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredRequestToAgency(request, assertion);
        } else {
            errMsg = "Policy Check Failed";
            log.error(errMsg);
            response = sendToAgencyErrorInterface(request, assertion, errMsg);
        }

        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");

        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredRequestToAgency(RetrieveDocumentSetRequestType request,
                                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqProxy proxy;
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType body;

        log.debug("Begin DocRetrieveReqImpl.sendDocRetrieveToAgency");
        if (isAdapterAudit) {
            auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);
        }

        proxy = new AdapterDocRetrieveDeferredReqProxyObjectFactory().getAdapterDocRetrieveDeferredRequestProxy();
        body = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        body.setRetrieveDocumentSetRequest(request);

        response = proxy.sendToAdapter(request, assertion);

        if (isAdapterAudit) {
            auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(),
                    assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        log.debug("End DocRetrieveReqImpl.sendDocRetrieveToAgency");
        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType sendToAgencyErrorInterface(RetrieveDocumentSetRequestType request,
                                                                                  AssertionType assertion, String errMsg) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredReqErrorProxy proxy;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        log.debug("Begin DocRetrieveReqImpl.sendToAgencyErrorInterface");
        if (isAdapterAudit) {
            auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);
        }

        proxy = new AdapterDocRetrieveDeferredReqErrorProxyObjectFactory().getAdapterDocRetrieveDeferredRequestErrorProxy();

        response = proxy.sendToAdapter(request, assertion, errMsg);

        if (isAdapterAudit) {
            auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        log.debug("End DocRetrieveReqImpl.sendToAgencyErrorInterface");
        return response;
    }

    /**
     *
     * @param request
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    protected void auditDeferredRetrieveMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface, AssertionType assertion) {
        org.alembic.aurion.common.auditlog.DocRetrieveMessageType message = new org.alembic.aurion.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface);
        if (auditLogMsg != null) {
            auditMessage(auditLogMsg, assertion);
        }
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
