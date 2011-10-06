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
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewaySendAlertMessageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhincadmindistribution.NhincAdminDistPortType;
import org.alembic.aurion.nhincadmindistribution.NhincAdminDistService;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyWebServiceUnsecuredImpl implements PassthruAdminDistributionProxy{
    private Log log = null;
    private static NhincAdminDistService service = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = null;

    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincadmindistribution";
    private static final String SERVICE_LOCAL_PART = "NhincAdminDistService";
    private static final String PORT_LOCAL_PART = "NhincAdminDist_PortType";
    private static final String WSDL_FILE = "NhincAdminDist.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincadmindistribution:SendAlertMessage_Message";

    public PassthruAdminDistributionProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        service = getWebService();
        proxyHelper =  getWebServiceProxyHelper();
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected NhincAdminDistService getWebService()
    {
        return new NhincAdminDistService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
    protected NhincAdminDistPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        NhincAdminDistPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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
        String url = helper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            NhincAdminDistPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
            RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
            message.setAssertion(assertion);
            message.setEDXLDistribution(body);
            message.setNhinTargetSystem(target);
            try
            {
                proxyHelper.invokePort(port, NhincAdminDistPortType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }

    }
}
