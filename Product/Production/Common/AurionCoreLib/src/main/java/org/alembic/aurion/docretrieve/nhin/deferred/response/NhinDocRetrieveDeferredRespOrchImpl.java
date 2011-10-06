/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.nhin.deferred.response;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.common.auditlog.DocRetrieveResponseMessageType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.docretrieve.DocRetrieveDeferredAuditLogger;
import org.alembic.aurion.docretrieve.adapter.deferred.response.proxy.AdapterDocRetrieveDeferredRespProxy;
import org.alembic.aurion.docretrieve.adapter.deferred.response.proxy.AdapterDocRetrieveDeferredRespProxyObjectFactory;
import org.alembic.aurion.docretrieve.nhin.deferred.NhinDocRetrieveDeferred;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class NhinDocRetrieveDeferredRespOrchImpl extends NhinDocRetrieveDeferred {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDeferredRespOrchImpl.class);

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetResponseType body,
                                                           AssertionType  assertion) {
        DocRetrieveAcknowledgementType  response = null;
        DocRetrieveDeferredAuditLogger  auditLog = new DocRetrieveDeferredAuditLogger();
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        if (isNhinAudit) {
            auditLog.auditDocRetrieveDeferredResponse(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        }

        try {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_RESPONSE_SERVICE_KEY)) {
                if (isInPassThroughMode(NhincConstants.NHINC_DOCUMENT_RETRIEVE_DEFERRED_RESPONSE_SERVICE_PASSTHRU_PROPERTY)) {
                    response = sendDocRetrieveDeferredResponseToAgency(body, assertion);
                } else {
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
            } else {
                String  msg = "Doc retrieve service is not enabled for Home Community Id: " + homeCommunityId;
                log.warn(msg);
                response = createSuccessResponse(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);
            }
        } catch (Throwable t) {
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);
            response = createSuccessResponse(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);
        }

        if (isNhinAudit) {
            auditLog.auditDocRetrieveDeferredAckResponse(response.getMessage(), assertion,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
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
    private DocRetrieveAcknowledgementType serviceDocRetrieveInternal(RetrieveDocumentSetResponseType request,
                                                                      AssertionType assertion, String homeCommunityId) {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        DocRetrieveAcknowledgementType response = null;
        HomeCommunityType hcId = new HomeCommunityType();
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        String  msg = "Adapter doc retrieve deferred response policy check failed.";

        hcId.setHomeCommunityId(homeCommunityId);
        if (isPolicyValidForResponse(request, assertion, hcId)) {
            log.debug("Adapter doc retrieve deferred policy check successful");
            response = sendDocRetrieveDeferredResponseToAgency(request, assertion);
        } else {
            msg = "Policy Check Failed on NHIN community : "+ homeCommunityId;
            log.error(msg);
            response = createSuccessResponse(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);
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
    private DocRetrieveAcknowledgementType sendDocRetrieveDeferredResponseToAgency(RetrieveDocumentSetResponseType request,
                                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        AdapterDocRetrieveDeferredRespProxy proxy;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        log.debug("Begin DocRetrieveReqImpl.sendDocRetrieveToAgency");
        if (isAdapterAudit) {
            auditDeferredRetrieveMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                    NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);
        }

        proxy = new AdapterDocRetrieveDeferredRespProxyObjectFactory().getAdapterDocRetrieveDeferredResponseProxy();
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
     * @param direction
     * @param connectInterface
     * @param assertion
     */
    protected void auditDeferredRetrieveMessage(RetrieveDocumentSetResponseType request, String direction, String connectInterface, AssertionType assertion) {
        DocRetrieveResponseMessageType message = new DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface);
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
