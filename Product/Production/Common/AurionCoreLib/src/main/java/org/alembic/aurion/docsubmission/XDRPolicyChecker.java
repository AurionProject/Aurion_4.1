/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.common.eventcommon.XDREventType;
import org.alembic.aurion.common.eventcommon.XDRMessageType;
import org.alembic.aurion.common.eventcommon.XDRResponseEventType;
import org.alembic.aurion.common.eventcommon.XDRResponseMessageType;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 *
 * @author dunnek
 */
public class XDRPolicyChecker {
    private static Log log = null;

    public XDRPolicyChecker()
    {
        log = createLogger();
    }

    public boolean checkXDRRequestPolicy(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion, String senderHCID, String receiverHCID, String direction) {


        XDREventType policyCheckReq = new XDREventType();
        XDRMessageType policyMsg = new XDRMessageType();

        policyCheckReq.setDirection(direction);

        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(senderHCID);

        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();

        receiverHC.setHomeCommunityId(receiverHCID);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        policyMsg.setAssertion(assertion);
        policyMsg.setProvideAndRegisterDocumentSetRequest(message);

        policyCheckReq.setMessage(policyMsg);

        return invokePolicyEngine(policyCheckReq);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean invokePolicyEngine(XDREventType policyCheckReq) {
        boolean policyIsValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyXDRRequest(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        AssertionType assertion = null;
        if(policyReq != null)
        {
            assertion = policyReq.getAssertion();
        }
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @param direction
     * @return
     */
    public boolean checkXDRResponsePolicy(RegistryResponseType message, AssertionType assertion, String senderHCID, String receiverHCID, String direction) {
        createLogger().debug("Entering checkXDRResponsePolicy");

        XDRResponseEventType policyCheckReq = createXDRResponseEventType(message, assertion, senderHCID, receiverHCID, direction);

        boolean isPolicyValid = false;

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();

        CheckPolicyRequestType policyReq = policyChecker.checkPolicyXDRResponse(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            isPolicyValid = true;
        }


        createLogger().debug("Exiting checkXDRResponsePolicy");

        return isPolicyValid;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @param direction
     * @return
     */
    private XDRResponseEventType createXDRResponseEventType(RegistryResponseType message, AssertionType assertion, String senderHCID, String receiverHCID, String direction) {
        XDRResponseEventType policyCheckReq = new XDRResponseEventType();
        XDRResponseMessageType policyMsg = new XDRResponseMessageType();

        policyCheckReq.setDirection(direction);

        HomeCommunityType senderHC = new HomeCommunityType();
        senderHC.setHomeCommunityId(senderHCID);

        policyCheckReq.setSendingHomeCommunity(senderHC);
        HomeCommunityType receiverHC = new HomeCommunityType();

        receiverHC.setHomeCommunityId(receiverHCID);
        policyCheckReq.setReceivingHomeCommunity(receiverHC);

        policyMsg.setAssertion(assertion);
        policyMsg.setRegistryResponse(message);

        policyCheckReq.setMessage(policyMsg);

        return policyCheckReq;
    }
}

