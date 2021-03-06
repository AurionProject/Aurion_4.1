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
import org.alembic.aurion.policyengine.adapter.AdapterPolicyEngineProcessorImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PolicyEngineProxyJavaImpl implements PolicyEngineProxy
{
    private Log log = null;

    public PolicyEngineProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin PolicyEngineWebServiceProxyJavaImpl.checkPolicy");
        CheckPolicyResponseType response = null;
        AdapterPolicyEngineProcessorImpl policyEngine = new AdapterPolicyEngineProcessorImpl();
        try
        {
            response = policyEngine.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling PolicyEngineWebServiceProxyJavaImpl.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }
        log.debug("End PolicyEngineWebServiceProxyJavaImpl.checkPolicy");
        return response;
    }

}
