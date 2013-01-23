/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter.component;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.policyengine.adapter.orchestrator.AdapterPolicyEngineOrchestratorImpl;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentPolicyEngineImpl
{

    private Log log = null;

    public AdapterComponentPolicyEngineImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected AdapterPolicyEngineOrchestratorImpl getAdapterPolicyEngineOrchestratorImpl()
    {
        return new AdapterPolicyEngineOrchestratorImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
    {
        // TODO: Extract message ID from the web service context for logging.
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest, WebServiceContext context)
    {
        CheckPolicyResponseType checkPolicyResp = null;

        AdapterPolicyEngineOrchestratorImpl oOrchestrator = getAdapterPolicyEngineOrchestratorImpl();
        try
        {
            AssertionType assertion = checkPolicyRequest.getAssertion();
            loadAssertion(assertion, context);
            getSoapLogger().logRawAssertion(assertion);

            checkPolicyResp = oOrchestrator.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineOrchestratorLib.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        return checkPolicyResp;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
