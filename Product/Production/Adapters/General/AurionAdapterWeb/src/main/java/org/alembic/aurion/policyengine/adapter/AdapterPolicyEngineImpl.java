/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterPolicyEngineImpl
{
    private Log log = null;

    public AdapterPolicyEngineImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
    {
        // TODO: Extract message ID from the web service context for logging.
    }

    public CheckPolicyResponseType checkPolicy(org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType request, WebServiceContext context)
    {
        log.debug("Begin AdapterPolicyEngineImpl.checkPolicy (unsecure)");
        CheckPolicyResponseType checkPolicyResp = null;

        org.alembic.aurion.policyengine.adapter.AdapterPolicyEngineProcessorImpl oPolicyEngine = new org.alembic.aurion.policyengine.adapter.AdapterPolicyEngineProcessorImpl();
        try
        {
            AssertionType assertion = request.getAssertion();
            loadAssertion(assertion, context);
            getSoapLogger().logRawAssertion(assertion);
            org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest = new org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType();
            checkPolicyRequest.setAssertion(assertion);
            checkPolicyRequest.setRequest(request.getRequest());
            checkPolicyResp = oPolicyEngine.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineImpl.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        log.debug("End AdapterPolicyEngineImpl.checkPolicy (unsecure)");
        return checkPolicyResp;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
