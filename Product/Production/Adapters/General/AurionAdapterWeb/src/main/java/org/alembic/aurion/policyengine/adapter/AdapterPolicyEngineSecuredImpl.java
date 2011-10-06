/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter;

import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterPolicyEngineSecuredImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterPolicyEngineSecuredImpl.class);

    public CheckPolicyResponseType checkPolicy(org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestSecuredType body, WebServiceContext context)
    {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        CheckPolicyResponseType checkPolicyResp = null;

        AdapterPolicyEngineProcessorImpl oPolicyEngine = new AdapterPolicyEngineProcessorImpl();
        try
        {
            org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest = new org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType();
            checkPolicyRequest.setAssertion(assertion);
            checkPolicyRequest.setRequest(body.getRequest());
            checkPolicyResp = oPolicyEngine.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sMessage = "Error occurred calling AdapterPolicyEngineImpl.checkPolicy.  Error: " +
                    e.getMessage();
            log.error(sMessage, e);
            throw new RuntimeException(sMessage, e);
        }
        return checkPolicyResp;
    }
}
