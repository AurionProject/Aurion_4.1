/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.request;

import org.alembic.aurion.common.eventcommon.DocRetrieveEventType;
import org.alembic.aurion.common.eventcommon.DocRetrieveMessageType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.docretrieve.DocRetrieveDeferredAuditLogger;
import org.alembic.aurion.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxyObjectFactory;
import org.alembic.aurion.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxy;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Implementation class for Entity Document Retrieve Deferred request message
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * Constructor
     */
    public EntityDocRetrieveDeferredReqOrchImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Entity Implementation method
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message,
                                                                      AssertionType assertion,
                                                                      NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        }
        DocRetrieveAcknowledgementType nhincResponse = null;
        String homeCommunityId = null;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if ((null != message) && (message.getDocumentRequest() != null) && (message.getDocumentRequest().size() > 0)) {
                if (isEntityAudit) {
                    auditLog.auditDocRetrieveDeferredRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion);
                }
                DocumentRequest docRequest = message.getDocumentRequest().get(0);
                homeCommunityId = docRequest.getHomeCommunityId();
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                // Set document request
                RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
                nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                nhinDocRequest.getDocumentRequest().add(docRequest);
                nhinDocRetrieveMsg.setNhinTargetSystem(buildHomeCommunity(docRequest.getHomeCommunityId()));
                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;
                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
                    log.warn("No targets were found for the Document retrieve deferred service Request");
                    nhincResponse = buildRegistryErrorAck();
                } else {
                    nhincResponse = new DocRetrieveAcknowledgementType();
                    if (debugEnabled) {
                                log.debug("Creating NHIN doc retrieve proxy");
                            }
                    PassthruDocRetrieveDeferredReqProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredReqProxyObjectFactory();
                    PassthruDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredReqProxy();
                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        if (isPolicyValid(nhinDocRequest, assertion, urlInfo.getHcid())) {
                            // Call NHIN proxy
                            oTargetSystem = new NhinTargetSystemType();
                            oTargetSystem.setUrl(urlInfo.getUrl());
                            if (debugEnabled) {
                                log.debug("Calling doc retrieve proxy");
                            }
                            nhincResponse = docRetrieveProxy.crossGatewayRetrieveRequest(message, assertion,
                                                                                         oTargetSystem);
                        } else {
                            nhincResponse = buildRegistryErrorAck();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error sending doc retrieve deferred message..." + ex.getMessage());
            nhincResponse = buildRegistryErrorAck();
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhincResponse && isEntityAudit) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhincResponse.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        }
        return nhincResponse;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType buildRegistryErrorAck() {
        DocRetrieveAcknowledgementType nhinResponse = new DocRetrieveAcknowledgementType();
        RegistryResponseType registryResponse = new RegistryResponseType();
        nhinResponse.setMessage(registryResponse);
        registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return nhinResponse;
    }

    /**
     *
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
     * Policy Engine check
     * @param body
     * @param assertion
     * @return boolean
     */
    private boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, String hcId) {
        boolean isValid = false;
        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        checkPolicyMessage.setRetrieveDocumentSetRequest(oEachNhinRequest);
        checkPolicyMessage.setAssertion(oAssertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcId);
        checkPolicy.setReceivingHomeCommunity(homeCommunity);
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
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
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
