/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunityType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 *
 * @author dunnek
 */
public class AdminDistributionPolicyChecker {

    private Log log = null;
    public AdminDistributionPolicyChecker()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public boolean checkOutgoingPolicy (RespondingGatewaySendAlertMessageType request,String target) {
        log.debug("checking the policy engine for the new request to a target community");

        org.alembic.aurion.transform.policy.AdminDistributionTransformHelper policyHelper;

        policyHelper = new org.alembic.aurion.transform.policy.AdminDistributionTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformEntityAlertToCheckPolicy(request, target);

        return invokePolicyEngine(checkPolicyRequest);
    }
    public boolean checkIncomingPolicy (EDXLDistribution request, AssertionType assertion) {
        log.debug("checking the policy engine for the new request to a target community");

        org.alembic.aurion.transform.policy.AdminDistributionTransformHelper policyHelper;

        policyHelper = new org.alembic.aurion.transform.policy.AdminDistributionTransformHelper();

        CheckPolicyRequestType checkPolicyRequest = policyHelper.transformNhinAlertToCheckPolicy(request, assertion);

        return invokePolicyEngine(checkPolicyRequest);
    }
    protected boolean invokePolicyEngine(CheckPolicyRequestType policyCheckReq) {
        boolean policyIsValid = false;

        log.debug("start invokePolicyEngine");
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
            log.debug("Policy engine check returned permit.");
            policyIsValid = true;
        } else {
            log.debug("Policy engine check returned deny.");
            policyIsValid = false;
        }

        return policyIsValid;
    }

}
