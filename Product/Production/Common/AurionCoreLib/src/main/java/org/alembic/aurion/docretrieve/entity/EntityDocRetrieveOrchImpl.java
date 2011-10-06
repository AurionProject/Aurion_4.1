/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docretrieve.DocRetrieveAuditLog;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.eventcommon.DocRetrieveEventType;
import org.alembic.aurion.common.eventcommon.DocRetrieveMessageType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.docretrieve.passthru.proxy.PassthruDocRetrieveProxy;
import org.alembic.aurion.docretrieve.passthru.proxy.PassthruDocRetrieveProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.service.ServiceFanout;

/**
 *
 * @author dunnek
 */
public class EntityDocRetrieveOrchImpl {
    private static org.apache.commons.logging.Log log = null;

    /**
     * default constructor
     */
    public EntityDocRetrieveOrchImpl()
    {
        log = createLogger();
    }

    /**
     * create log4j logger instance
     * @return Log
     */
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * Actual implementation called by entity service...
     * @param body
     * @param assertion
     * @return RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        RetrieveDocumentSetResponseType response = null;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        DocRetrieveAuditLog auditLog = new DocRetrieveAuditLog();
        if (isEntityAudit) {
            auditLog.auditDocRetrieveRequest(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        response = getResponseFromCommunities(body, assertion);
        // Audit log - response
        if (isEntityAudit) {
            auditLog.auditResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        log.debug("End EntityDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
 
    }

    /**
     * Actual logic for parallel processing the requests....
     * @param body
     * @param assertion
     * @return RetrieveDocumentSetResponseType
     */
    private  RetrieveDocumentSetResponseType getResponseFromCommunities(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        log.debug("EntityDocRetrieveOrchImpl.getResponseFromCommunities() -- Begin");
        RetrieveDocumentSetResponseType response = null;
        RetrieveDocumentSetResponseType resultFromNhin = null;
        PassthruDocRetrieveProxyObjectFactory objectFactory = new PassthruDocRetrieveProxyObjectFactory();
        PassthruDocRetrieveProxy proxy = objectFactory.getPassthruDocRetrieveProxy();
        if(body != null
                && body.getDocumentRequest()!= null
                && body.getDocumentRequest().size() > 0)
        {
            List<ServiceFanout> fanoutList = new ArrayList<ServiceFanout>();
            List<Thread> threadList = new ArrayList<Thread>();
            List<RespondingGatewayCrossGatewayRetrieveSecuredRequestType> proxyMsgList = new ArrayList<RespondingGatewayCrossGatewayRetrieveSecuredRequestType>();
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType createNewRequest = null;
            RetrieveDocumentSetRequestType docRetrieveRequest = null;
            NhinTargetSystemType nhinTargetSystem = null;
            HomeCommunityType hCommunity = null;
            for(DocumentRequest eachRequest : body.getDocumentRequest())
            {
                createNewRequest = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                docRetrieveRequest = new RetrieveDocumentSetRequestType();
                docRetrieveRequest.getDocumentRequest().add(eachRequest);
                createNewRequest.setRetrieveDocumentSetRequest(docRetrieveRequest);
                nhinTargetSystem = new NhinTargetSystemType();
                hCommunity = new HomeCommunityType();
                hCommunity.setHomeCommunityId(eachRequest.getHomeCommunityId());
                nhinTargetSystem.setHomeCommunity(hCommunity);
                createNewRequest.setNhinTargetSystem(nhinTargetSystem);
                proxyMsgList.add(createNewRequest);
                boolean bIsPolicyOk = isPolicyValid(docRetrieveRequest, assertion, nhinTargetSystem.getHomeCommunity());
                if (bIsPolicyOk)
                {
                    ServiceFanout fanout = new ServiceFanout(proxy, PassthruDocRetrieveProxy.class, "respondingGatewayCrossGatewayRetrieve",  createNewRequest.getRetrieveDocumentSetRequest(), assertion, nhinTargetSystem);
                    fanoutList.add(fanout);
                    Thread drThread = new Thread(fanout);
                    threadList.add(drThread);
                    // Start the fanout thread and wait for it to finish
                    drThread.start();
                } else {
                    resultFromNhin = createErrorResponse("Policy Check Failed For DR Request on Community "+nhinTargetSystem.getHomeCommunity());
                } //end policy fail
            } //end for loop

            // If there were threads started then join those threads to wait until they are finished and gather the results
            if (NullChecker.isNotNullish(threadList) &&
                    NullChecker.isNotNullish(fanoutList) &&
                    NullChecker.isNotNullish(proxyMsgList) &&
                    threadList.size() == fanoutList.size() &&
                    threadList.size() == proxyMsgList.size()) {
                log.debug("Start collecting responses from " + threadList.size() + " threads");
                RetrieveDocumentSetResponseType params = new RetrieveDocumentSetResponseType();
                Iterator<RespondingGatewayCrossGatewayRetrieveSecuredRequestType> msgIterator = proxyMsgList.iterator();
                Iterator<ServiceFanout> fanoutIterator = fanoutList.iterator();
                response = new RetrieveDocumentSetResponseType();
                // Wait for all of the threads to either complete execution or timeout
                for (Thread tempThread : threadList) {
                    try {
                        log.debug("Collecting response from thread");
                        tempThread.join(120000);
                        resultFromNhin = (RetrieveDocumentSetResponseType) fanoutIterator.next().getResponse();
                        if(resultFromNhin != null
                                && resultFromNhin.getDocumentResponse()!=null)
                        {
                            response.getDocumentResponse().addAll(resultFromNhin.getDocumentResponse());
                            response.setRegistryResponse(resultFromNhin.getRegistryResponse());
                        }
                    } catch (InterruptedException ex) {
                        log.error("Thread was interrupted prior to finishing execution");
                        log.error(ex);
                    }
                } //End Thread for loop...
                log.debug("Collected " + response.getDocumentResponse().size() + " responses from threads");
            }
        }
        log.debug("EntityDocRetrieveOrchImpl.getResponseFromCommunities() -- End");
        return response;
    }

    /**
     * Creating error response...
     * @param codeContext
     * @return RetrieveDocumentSetResponseType
     */
    private RetrieveDocumentSetResponseType createErrorResponse(String codeContext) {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType responseType = new RegistryResponseType();
        response.setRegistryResponse(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRegistryError");
        regErr.setSeverity("Error");
        return response;
    }

    /**
     * Creates NhinTargetSystem using home community Id...
     * @param homeCommunityId
     * @return NhinTargetSystemType
     */
    private NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

    /**
     * Check policy Engine response for each new request gets created....
     * @param oEachNhinRequest
     * @param oAssertion
     * @param targetCommunity
     * @return boolean
     */
    protected boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;

        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        checkPolicyMessage.setRetrieveDocumentSetRequest(oEachNhinRequest);
        checkPolicyMessage.setAssertion(oAssertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, oAssertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
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
