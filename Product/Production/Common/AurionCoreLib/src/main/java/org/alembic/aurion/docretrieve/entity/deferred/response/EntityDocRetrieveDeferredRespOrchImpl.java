/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.response;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.docretrieve.DocRetrieveDeferredAuditLogger;
import org.alembic.aurion.docretrieve.DocRetrieveDeferredPolicyChecker;
import org.alembic.aurion.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxyObjectFactory;
import org.alembic.aurion.docretrieve.passthru.deferred.response.proxy.PassthruDocRetrieveDeferredRespProxy;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespOrchImpl() {
        log = createLogger();
        debugEnabled = false;
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        DocRetrieveAcknowledgementType nhinResponse = null;
        String homeCommunityId = null;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if (null != response && (null != assertion) && (null != target)) {
                if(isEntityAudit)
                auditLog.auditDocRetrieveDeferredResponse(response, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion);
                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;
                homeCommunityId = getHomeCommFromTarget(target);
                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
                    log.warn("No targets were found for the Document retrieve deferred Response service");
                    nhinResponse = buildRegistryErrorAck();
                } else {
                    if (debugEnabled) {
                        log.debug("Creating NHIN doc retrieve proxy");
                    }
                    PassthruDocRetrieveDeferredRespProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredRespProxyObjectFactory();
                    PassthruDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredRespProxy();
                    DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();
                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        // Call NHIN proxy
                        oTargetSystem = new NhinTargetSystemType();
                        oTargetSystem.setUrl(urlInfo.getUrl());
                        if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                            // Call NHIN proxy
                            nhinResponse = docRetrieveProxy.crossGatewayRetrieveResponse(response, assertion, oTargetSystem);
                        }
                        else {
                            nhinResponse = buildRegistryErrorAck();
                        }

                    }
                }
                
            }
        } catch (Exception ex) {
            log.error(ex);
            nhinResponse = buildRegistryErrorAck();
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhinResponse && isEntityAudit) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        return nhinResponse;
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
     * @param target
     * @return String
     */
    protected String getHomeCommFromTarget(NhinTargetCommunitiesType target) {
        String sHomComm = null;
        if (target.getNhinTargetCommunity() != null &&
                target.getNhinTargetCommunity().size() > 0) {
            NhinTargetCommunityType comm = target.getNhinTargetCommunity().get(0);
            if (comm.getHomeCommunity() != null) {
                sHomComm = comm.getHomeCommunity().getHomeCommunityId();
            }
        }
        return sHomComm;
    }

    /**
     *
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_RESPONSE);
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
