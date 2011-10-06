/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.admindistribution.passthru.proxy;

import org.alembic.aurion.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhincadmindistribution.NhincAdminDistSecuredPortType;
import org.alembic.aurion.nhincadmindistribution.NhincAdminDistSecuredService;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewaySendAlertMessageSecuredType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.saml.extraction.SamlTokenCreator;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyWebServiceSecuredImpl implements PassthruAdminDistributionProxy {
    private Log log = null;
    private static NhincAdminDistSecuredService service = null;
    private static Service cachedService = null;

    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincadmindistribution";
    private static final String SERVICE_LOCAL_PART = "NhincAdminDistSecuredService";
    private static final String PORT_LOCAL_PART = "NhincAdminDistSecured_PortType";
    private static final String WSDL_FILE = "NhincAdminDistSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincadmindistribution:SendAlertMessageSecured_Message";

    public PassthruAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        service = getWebService();
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected NhincAdminDistSecuredService getWebService()
    {
        return new NhincAdminDistSecuredService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
    protected NhincAdminDistSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        NhincAdminDistSecuredPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistSecuredPortType.class);

            WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target)
    {
        AdminDistributionHelper helper = getHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            NhincAdminDistSecuredPortType port = getPort(url, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);
            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();

            message.setEDXLDistribution(body);
            message.setNhinTargetSystem(target);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            try
            {
                WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
                oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
                ((BindingProvider) port).getRequestContext().putAll(requestContext);


                WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();
                proxyHelper.invokePort(port, RespondingGatewaySendAlertMessageSecuredType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }

}
