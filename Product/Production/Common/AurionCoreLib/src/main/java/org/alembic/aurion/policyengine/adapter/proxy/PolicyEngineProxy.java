/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;

/**
 *
 * @author Jon Hoppesch
 */
public interface PolicyEngineProxy
{

    /**
     * Checks the policy for an operation.
     *
     * @param request Generic Policy Check request message
     * @return The policy check results
     */
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion);
}
