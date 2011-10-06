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
package org.alembic.aurion.docquery.entity.deferred.response;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.DocQueryPolicyChecker;
import org.alembic.aurion.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxy;
import org.alembic.aurion.docquery.passthru.deferred.response.proxy.PassthruDocQueryDeferredResponseProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocQueryDeferredResponseOrchImpl {

    private static final Log log = LogFactory.getLog(EntityDocQueryDeferredResponseOrchImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        AcknowledgementType ack = null;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        respAck.setMessage(regResp);

        // Audit the incoming Entity Message
        if (isEntityAudit) {
            ack = auditResponse(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        try {
            CMUrlInfos urlInfoList = getEndpoints(targets);

            if (urlInfoList != null &&
                    NullChecker.isNotNullish(urlInfoList.getUrlInfo()) &&
                    urlInfoList.getUrlInfo().get(0) != null &&
                    NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getHcid()) &&
                    NullChecker.isNotNullish(urlInfoList.getUrlInfo().get(0).getUrl())) {
                HomeCommunityType targetHcid = new HomeCommunityType();
                targetHcid.setHomeCommunityId(urlInfoList.getUrlInfo().get(0).getHcid());

                if (isPolicyValid(msg, assertion, targetHcid)) {
                    NhinTargetSystemType target = new NhinTargetSystemType();
                    target.setUrl(urlInfoList.getUrlInfo().get(0).getUrl());

                    PassthruDocQueryDeferredResponseProxyObjectFactory factory = new PassthruDocQueryDeferredResponseProxyObjectFactory();
                    PassthruDocQueryDeferredResponseProxy proxy = factory.getPassthruDocQueryDeferredResponseProxy();
                    respAck = proxy.respondingGatewayCrossGatewayQuery(msg, assertion, target);
                } else {
                    log.error("Outgoing Policy Check Failed");
                    regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
                }
            } else {
                log.error("Failed to obtain target URL from connection manager");
                regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
            }
        } catch (Exception ex) {
            log.error(ex);
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
        }

        // Audit the outgoing Entity Message
        if (isEntityAudit) {
            ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        return respAck;
    }

    private AcknowledgementType auditResponse(AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface);

        return ack;
    }

    private boolean isPolicyValid(AdhocQueryResponse message, AssertionType assertion, HomeCommunityType target) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkOutgoingResponsePolicy(message, assertion, target);

        return policyIsValid;
    }

    private CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME);
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
