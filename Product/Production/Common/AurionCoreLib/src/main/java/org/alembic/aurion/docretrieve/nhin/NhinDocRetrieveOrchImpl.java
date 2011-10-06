/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.nhin;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docretrieve.adapter.proxy.AdapterDocRetrieveProxy;
import org.alembic.aurion.docretrieve.adapter.proxy.AdapterDocRetrieveProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.eventcommon.DocRetrieveEventType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.gateway.aggregator.document.DocumentConstants;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 * The business logic for orchestrating a doc retrieve message from the nhin.
 *
 * @author westberg
 */
public class NhinDocRetrieveOrchImpl
{

    private static Log log = LogFactory.getLog(NhinDocRetrieveOrchImpl.class);

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        log.debug("Entering DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        RetrieveDocumentSetResponseType response = null;

        log.debug("Calling audit on doc retrieve request received from NHIN");
        
        if (isNhinAudit) {
            auditRequestMessage(body, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        }

        try
        {
            String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
            if (isServiceEnabled())
            {
                log.debug("Doc retrieve service is enabled. Procesing message");
                if (isInPassThroughMode())
                {
                    log.debug("In passthrough mode. Sending adapter doc retrieve directly to adapter");
                    response = sendDocRetrieveToAgency(body, assertion);
                }
                else
                {
                    log.debug("Not in passthrough mode. Calling internal processing for adapter doc retrieve");
                    response = serviceDocRetrieveInternal(body, assertion, homeCommunityId);
                }
            }
            else
            {
                log.debug("Doc retrieve service is not enabled. returning an empty response");
                response = createEmptyResponse(homeCommunityId);
            }
        }
        catch (Throwable t)
        { 
            log.error("Error processing NHIN Doc Retrieve: " + t.getMessage(), t);
            response = createErrorResponse("Processing document retrieve message");
        }

        log.debug("Calling audit on doc retrieve response returned to NHIN");
        if (isNhinAudit) {
            auditResponseMessage(response, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, assertion);
        }

        log.debug("Exiting DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

    private RetrieveDocumentSetResponseType serviceDocRetrieveInternal(RetrieveDocumentSetRequestType request, AssertionType assertion, String homeCommunityId)
    {
        log.debug("Begin DocRetrieveImpl.serviceDocRetrieveInternal");
        RetrieveDocumentSetResponseType response = null;

        if (checkPolicy(request, assertion))
        {
            log.debug("Adapter doc retrieve policy check successful");
            response = sendDocRetrieveToAgency(request, assertion);
        }
        else
        {
            log.debug("Adapter doc retrieve policy check failed");
            response = createEmptyResponse(homeCommunityId);
            response.getRegistryResponse().setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        }

        log.debug("End DocRetrieveImpl.serviceDocRetrieveInternal");
        return response;
    }

    private RetrieveDocumentSetResponseType sendDocRetrieveToAgency(RetrieveDocumentSetRequestType request, AssertionType assertion)
    {
        log.debug("Begin DocRetrieveImpl.sendDocRetrieveToAgency");
        RetrieveDocumentSetResponseType response = null;

        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        log.debug("Calling audit log for doc retrieve request sent to adapter");
        if (isAdapterAudit) {
            auditRequestMessage(request, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);
        }

        log.debug("Creating adapter doc retrieve proxy");
        AdapterDocRetrieveProxy proxy = new AdapterDocRetrieveProxyObjectFactory().getAdapterDocRetrieveProxy();
        log.debug("Sending adapter doc retrieve to adapter");
        response = proxy.retrieveDocumentSet(request, assertion);

        log.debug("Calling audit log for doc retrieve response received from adapter");
        if (isAdapterAudit) {
            auditResponseMessage(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, assertion);
        }

        log.debug("End DocRetrieveImpl.sendDocRetrieveToAgency");
        return response;
    }

    private void auditRequestMessage(RetrieveDocumentSetRequestType request, String direction, String connectInterface, AssertionType assertion)
    {
        org.alembic.aurion.common.auditlog.DocRetrieveMessageType message = new org.alembic.aurion.common.auditlog.DocRetrieveMessageType();
        message.setRetrieveDocumentSetRequest(request);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieve(message, direction, connectInterface);
        if (auditLogMsg != null)
        {
            auditMessage(auditLogMsg, assertion);
        }
    }

    private void auditResponseMessage(RetrieveDocumentSetResponseType response, String direction, String connectInterface, AssertionType assertion)
    {
        org.alembic.aurion.common.auditlog.DocRetrieveResponseMessageType message = new org.alembic.aurion.common.auditlog.DocRetrieveResponseMessageType();
        message.setRetrieveDocumentSetResponse(response);
        message.setAssertion(assertion);
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logDocRetrieveResult(message, direction, connectInterface);
        if (auditLogMsg != null)
        {
            auditMessage(auditLogMsg, assertion);
        }
    }

    private AcknowledgementType auditMessage(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    private boolean isServiceEnabled()
    {
        boolean serviceEnabled = false;
        try
        {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY);
        }
        catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode()
    {
        boolean passThroughModeEnabled = false;
        try
        {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY);
        }
        catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY + " from property file " + NhincConstants.GATEWAY_PROPERTY_FILE + ": " + ex.getMessage(), ex);
        }
        return passThroughModeEnabled;
    }

    private boolean checkPolicy(RetrieveDocumentSetRequestType message, AssertionType assertion)
    {
        boolean policyIsValid = false;

        //convert the request message to an object recognized by the policy engine
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        org.alembic.aurion.common.eventcommon.DocRetrieveMessageType request = new org.alembic.aurion.common.eventcommon.DocRetrieveMessageType();
        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(message);
        policyCheckReq.setMessage(request);

        //call the policy engine to check the permission on the request
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        //check the policy engine's response, return true if response = permit
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT)
        {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    private RetrieveDocumentSetResponseType createEmptyResponse(String homeCommunityId)
    {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
        RetrieveDocumentSetResponseType.DocumentResponse docResponse = new RetrieveDocumentSetResponseType.DocumentResponse();
        docResponse.setHomeCommunityId(homeCommunityId);
        response.getDocumentResponse().add(docResponse);
        return response;
    }

    private RetrieveDocumentSetResponseType createErrorResponse(String codeContext)
    {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return response;
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
