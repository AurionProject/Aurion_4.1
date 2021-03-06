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

package org.alembic.aurion.docsubmission.entity.deferred.response;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.XDRPolicyChecker;
import org.alembic.aurion.docsubmission.passthru.deferred.response.proxy.PassthruDocSubmissionDeferredResponseProxy;
import org.alembic.aurion.docsubmission.passthru.deferred.response.proxy.PassthruDocSubmissionDeferredResponseProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.transform.policy.SubjectHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredResponseOrchImpl {

    private Log log = null;
    private XDRAuditLogger auditLogger = null;

     public EntityDocSubmissionDeferredResponseOrchImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.info("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        provideAndRegisterDocumentSetSecuredResponseRequest.setNhinTargetCommunities(targets);
        provideAndRegisterDocumentSetSecuredResponseRequest.setRegistryResponse(request);
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        if (isEntityAudit) {
            logRequest(provideAndRegisterDocumentSetSecuredResponseRequest, assertion);
        }

        if (provideAndRegisterDocumentSetSecuredResponseRequest != null &&
                provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId()))
        {

            if (checkPolicy(provideAndRegisterDocumentSetSecuredResponseRequest, assertion))
            {
                log.info("Policy check successful");

                NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
                targetSystemType.setHomeCommunity(provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());

                org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType proxyRequest = new org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
                proxyRequest.setRegistryResponse(provideAndRegisterDocumentSetSecuredResponseRequest.getRegistryResponse());
                proxyRequest.setNhinTargetSystem(targetSystemType);

                log.debug("Sending request from entity service to NHIN proxy service");
                response = callNhinXDRResponseProxy(proxyRequest, assertion);
            } else
            {
                log.error("Policy check unsuccessful");

            }
        } else
        {
            log.warn("There was not a target community provided in the Entity message");
        }

        if (isEntityAudit) {
            logResponse(response, assertion);
        }

        log.info("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    protected XDRAcknowledgementType callNhinXDRResponseProxy(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, AssertionType assertion)
    {
        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        PassthruDocSubmissionDeferredResponseProxyObjectFactory factory = new PassthruDocSubmissionDeferredResponseProxyObjectFactory();
        PassthruDocSubmissionDeferredResponseProxy proxy = factory.getPassthruDocSubmissionDeferredResponseProxy();

        log.debug("Calling NHIN Proxy");
        response = proxy.provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());

        log.debug("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditEntityXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    private void logResponse(XDRAcknowledgementType response, AssertionType assertion)
    {
        log.debug("Beging logResponse");
        auditLogger.auditEntityAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);
        log.debug("End logResponse");
    }

    protected XDRAuditLogger createAuditLogger()
    {
        return new XDRAuditLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion)
    {
        log.debug("Begin checkPolicy");
        boolean bPolicyOk = false;

        if (request != null &&
                request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId()))
        {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to " + receiverHCID);

            //return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRResponsePolicy(request.getRegistryResponse(), assertion, senderHCID, receiverHCID, direction);
        } else
        {
            log.warn("EntityXDRResponseSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType");
        }
        log.debug("EntityXDRResponseSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;
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
