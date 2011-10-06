/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter.pep;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterPEPServiceImpl
{
    private Log log = null;

    public AdapterPEPServiceImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected AdapterPEPImpl getAdapterPEPImpl()
    {
        return new AdapterPEPImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
    {
        // TODO: Extract message ID from the web service context for logging.
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest, WebServiceContext context) {
        CheckPolicyResponseType checkPolicyResp = null;

        AdapterPEPImpl adapterPEPImpl = getAdapterPEPImpl();

        try {
            AssertionType assertion = checkPolicyRequest.getAssertion();
            loadAssertion(assertion, context);

            checkPolicyResp = adapterPEPImpl.checkPolicy(checkPolicyRequest, assertion);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterPEPImpl.checkPolicy.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return checkPolicyResp;
    }

}
