/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.common.eventcommon.AdhocQueryRequestEventType;
import org.alembic.aurion.common.eventcommon.AdhocQueryRequestMessageType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.passthru.proxy.PassthruDocQueryProxy;
import org.alembic.aurion.docquery.passthru.proxy.PassthruDocQueryProxyObjectFactory;
import org.alembic.aurion.gateway.aggregator.SetResponseMsgDocQueryRequestType;
import org.alembic.aurion.gateway.aggregator.document.DocQueryAggregator;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.document.DocumentQueryTransform;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;

/**
 *
 * @author Neil Webb
 */
public class DocQuerySender {

    private Log log = null;
    private String sLocalHomeCommunity = null;
    private String sLocalAssigningAuthorityId = null;
    private String sTransactionId = null;
    private AssertionType oAssertion;
    private QualifiedSubjectIdentifierType oSubjectId;
    private AdhocQueryRequest oOriginalQueryRequest;

    public DocQuerySender(String transactionId, AssertionType assertion, QualifiedSubjectIdentifierType subjectId, AdhocQueryRequest originalQueryRequest, String localAssigningAuthorityId) {
        log = createLogger();
        sLocalHomeCommunity = getLocalHomeCommunityId();
        oOriginalQueryRequest = originalQueryRequest;
        sTransactionId = transactionId;
        oAssertion = assertion;
        oSubjectId = subjectId;
        sLocalAssigningAuthorityId = localAssigningAuthorityId;
    }

    protected Log createLogger() {
        return ((log != null) ? log : org.apache.commons.logging.LogFactory.getLog(DocQuerySender.class));
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (sLocalHomeCommunity != null) {
            sHomeCommunity = sLocalHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex) {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    

    protected DocumentQueryTransform createDocumentTransform() {
        return new DocumentQueryTransform();
    }

    protected DocQueryAggregator createDocQueryAggregator() {
        return new DocQueryAggregator();
    }

    public void sendMessage() {
        org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType docQuery = new org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType();
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        String assigningAuthority = oSubjectId.getAssigningAuthorityIdentifier();
        EntityDocQueryHelper helper = new EntityDocQueryHelper();

        HomeCommunityType targetCommunity = new EntityDocQueryHelper().lookupHomeCommunityId(assigningAuthority, sLocalAssigningAuthorityId, sLocalHomeCommunity);
        String sTargetHomeCommunityId = null;
        if (targetCommunity != null) {
            targetSystem.setHomeCommunity(targetCommunity);
            sTargetHomeCommunityId = targetCommunity.getHomeCommunityId();
        }
        docQuery.setNhinTargetSystem(targetSystem);
        // Replace the patient id in the document query message
        DocumentQueryTransform transform = createDocumentTransform();
        AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(oOriginalQueryRequest, sLocalHomeCommunity, oSubjectId.getAssigningAuthorityIdentifier(), oSubjectId.getSubjectIdentifier());
        docQuery.setAdhocQueryRequest(adhocQueryRequest);
        AdhocQueryResponse queryResults = null;
        if (isValidPolicy(adhocQueryRequest, oAssertion, targetCommunity)) {
            try {
                log.debug("Creating NhinDocQueryProxy");
                PassthruDocQueryProxyObjectFactory docQueryFactory = new PassthruDocQueryProxyObjectFactory();
                PassthruDocQueryProxy proxy = docQueryFactory.getPassthruDocQueryProxy();

                RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();

                request.setAdhocQueryRequest(docQuery.getAdhocQueryRequest());
                request.setAssertion(oAssertion);
                request.setNhinTargetSystem(docQuery.getNhinTargetSystem());

                log.debug("Calling NhinDocQueryProxy.respondingGatewayCrossGatewayQuery(request)");
                queryResults = proxy.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest(), request.getAssertion(), request.getNhinTargetSystem());
            } catch (Throwable t) {
                queryResults = new AdhocQueryResponse();
                RegistryErrorList regErrList = new RegistryErrorList();
                RegistryError regErr = new RegistryError();
                regErrList.getRegistryError().add(regErr);
                regErr.setCodeContext("Fault encountered processing internal document query for community "+ sTargetHomeCommunityId);
                regErr.setErrorCode("XDSRegistryNotAvailable");
                regErr.setSeverity("Error");
                queryResults.setRegistryErrorList(regErrList);
                queryResults.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            }
        } else {
            queryResults = new AdhocQueryResponse();
            RegistryErrorList regErrList = new RegistryErrorList();
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Policy Check Failed");
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
            queryResults.setRegistryErrorList(regErrList);
            queryResults.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }
        registerResponseMessage(queryResults, sTargetHomeCommunityId);
    }

    private void registerResponseMessage(AdhocQueryResponse queryResults, String sTargetHomeCommunityId) {
        SetResponseMsgDocQueryRequestType oSetResponseRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseRequest.setTransactionId(sTransactionId);
        oSetResponseRequest.setHomeCommunityId(sTargetHomeCommunityId);
        oSetResponseRequest.setQualifiedPatientIdentifier(oSubjectId);
        oSetResponseRequest.setAdhocQueryResponse(queryResults);
        DocQueryAggregator aggregator = createDocQueryAggregator();
        aggregator.setResponseMsg(oSetResponseRequest);
    }

    /**
     * Policy Check verification done here...
     * @param queryRequest
     * @param assertion
     * @return boolean
     */
    private boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion, HomeCommunityType targetCommunity) {
        boolean isValid = false;
        AdhocQueryRequestEventType checkPolicy = new AdhocQueryRequestEventType();
        AdhocQueryRequestMessageType checkPolicyMessage = new AdhocQueryRequestMessageType();
        checkPolicyMessage.setAdhocQueryRequest(queryRequest);
        checkPolicyMessage.setAssertion(assertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        checkPolicy.setReceivingHomeCommunity(targetCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyAdhocQuery(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }
}
