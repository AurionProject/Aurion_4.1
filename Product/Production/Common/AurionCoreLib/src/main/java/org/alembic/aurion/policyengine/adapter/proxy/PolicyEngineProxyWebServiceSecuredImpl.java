/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter.proxy;

import org.alembic.aurion.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestSecuredType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PolicyEngineProxyWebServiceSecuredImpl implements PolicyEngineProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adapterpolicyenginesecured";
    private static final String SERVICE_LOCAL_PART = "AdapterPolicyEngineSecured";
    private static final String PORT_LOCAL_PART = "AdapterPolicyEngineSecuredPortSoap";
    private static final String WSDL_FILE = "AdapterPolicyEngineSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adapterpolicyenginesecured:body";

    private WebServiceProxyHelper oProxyHelper = null;

    public PolicyEngineProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    private AdapterPolicyEngineSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterPolicyEngineSecuredPortType port = null;

        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPolicyEngineSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.POLICY_ENGINE_ACTION, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin PolicyEngineWebServiceProxySecuredImpl.checkPolicy");
        CheckPolicyResponseType response = null;
        String serviceName = NhincConstants.POLICYENGINE_SERVICE_SECURED_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if(log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                CheckPolicyRequestSecuredType securedRequest = new CheckPolicyRequestSecuredType();
                if(checkPolicyRequest != null)
                {
                    securedRequest.setRequest(checkPolicyRequest.getRequest());
                }
                AdapterPolicyEngineSecuredPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (CheckPolicyResponseType)oProxyHelper.invokePort(port, AdapterPolicyEngineSecuredPortType.class, "checkPolicy", securedRequest);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.POLICYENGINE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        log.debug("End PolicyEngineWebServiceProxySecuredImpl.checkPolicy");
        return response;
    }

}
