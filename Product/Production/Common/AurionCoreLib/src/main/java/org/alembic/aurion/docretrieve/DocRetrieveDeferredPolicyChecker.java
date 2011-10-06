/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve;

import org.alembic.aurion.common.eventcommon.DocRetrieveResponseMessageType;
import org.alembic.aurion.common.eventcommon.DocRetrieveResultEventType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.transform.policy.DocRetrieveDeferredTransformHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredPolicyChecker {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public DocRetrieveDeferredPolicyChecker() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param target
     * @return boolean
     */
    public boolean checkOutgoingPolicy(RetrieveDocumentSetResponseType request, AssertionType assertion, String target) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredPolicyChecker.checkOutgoingPolicy() --");
        }
        if (debugEnabled) {
            log.debug("checking the policy engine for the new response to a target community");
        }
        DocRetrieveDeferredTransformHelper policyHelper = new DocRetrieveDeferredTransformHelper();
        DocRetrieveResultEventType eventRequest = new DocRetrieveResultEventType();
        DocRetrieveResponseMessageType respMessage = new DocRetrieveResponseMessageType();
        respMessage.setRetrieveDocumentSetResponse(request);
        respMessage.setAssertion(assertion);
        eventRequest.setMessage(respMessage);
        eventRequest.setMessage(respMessage);
        eventRequest.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        eventRequest.setInterface("Entity");
        if (null != assertion) {
            HomeCommunityType loclHC = assertion.getHomeCommunity();
            eventRequest.setSendingHomeCommunity(loclHC);
        }
        HomeCommunityType remoteHC = new HomeCommunityType();
        remoteHC.setHomeCommunityId(target);
        eventRequest.setReceivingHomeCommunity(remoteHC);
        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformDocRetrieveDeferredRespToCheckPolicy(eventRequest);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredPolicyChecker.checkOutgoingPolicy() --");
        }
        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return boolean
     */
    public boolean checkIncomingPolicy(RetrieveDocumentSetResponseType request, AssertionType assertion) {
        if (debugEnabled) {
            log.debug("-- Begin DocRetrieveDeferredPolicyChecker.checkIncomingPolicy() --");
        }
        if (debugEnabled) {
            log.debug("checking the policy engine for the new request to a target community");
        }
        DocRetrieveDeferredTransformHelper policyHelper = new DocRetrieveDeferredTransformHelper();
        DocRetrieveResultEventType eventRequest = new DocRetrieveResultEventType();
        DocRetrieveResponseMessageType respMessage = new DocRetrieveResponseMessageType();
        respMessage.setRetrieveDocumentSetResponse(request);
        respMessage.setRetrieveDocumentSetResponse(request);
        eventRequest.setMessage(respMessage);
        eventRequest.setMessage(respMessage);
        eventRequest.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        eventRequest.setInterface("Nhin");
        if (null != assertion) {
            HomeCommunityType loclHC = assertion.getHomeCommunity();
            eventRequest.setSendingHomeCommunity(loclHC);
        }
        if (null != request && null != request.getDocumentResponse() && request.getDocumentResponse().size() > 0) {
            HomeCommunityType remoteHC = new HomeCommunityType();
            remoteHC.setHomeCommunityId(request.getDocumentResponse().get(0).getHomeCommunityId());
            eventRequest.setReceivingHomeCommunity(remoteHC);
        }
        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformDocRetrieveDeferredRespToCheckPolicy(eventRequest);
        if (debugEnabled) {
            log.debug("-- End DocRetrieveDeferredPolicyChecker.checkIncomingPolicy() --");
        }
        return invokePolicyEngine(checkPolicyRequest);
    }

    /**
     * 
     * @param policyCheckReq
     * @return boolean
     */
    protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
        if (debugEnabled) {
            log.debug("Begin DocRetrieveDeferredPolicyChecker.invokePolicyEngine");
        }
        boolean policyIsValid = false;
        if (debugEnabled) {
            log.debug("start invokePolicyEngine");
        }
        /* invoke check policy */
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if(policyCheckReq != null)
        {
            assertion = policyCheckReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyCheckReq, assertion);

        /* if response='permit' */
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            if (debugEnabled) {
                log.debug("Policy engine check returned permit.");
            }
            policyIsValid = true;
        } else {
            if (debugEnabled) {
                log.debug("Policy engine check returned deny.");
            }
            policyIsValid = false;
        }
        if (debugEnabled) {
            log.debug("End DocRetrieveDeferredPolicyChecker.invokePolicyEngine");
        }
        return policyIsValid;
    }

}
