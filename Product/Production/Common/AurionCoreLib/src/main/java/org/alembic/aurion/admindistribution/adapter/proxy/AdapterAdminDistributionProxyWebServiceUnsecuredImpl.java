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

package org.alembic.aurion.admindistribution.adapter.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.adapteradmindistribution.AdapterAdministrativeDistributionPortType;
import org.alembic.aurion.adapteradmindistribution.AdapterAdministrativeDistribution;
import org.alembic.aurion.admindistribution.AdminDistributionHelper;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.BindingProvider;
/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyWebServiceUnsecuredImpl implements AdapterAdminDistributionProxy{
    private Log log = null;
    static AdapterAdministrativeDistribution adapterService = null;
    public AdapterAdminDistributionProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        adapterService = getWebService();
    }
    protected AdapterAdministrativeDistribution getWebService()
    {
        return new AdapterAdministrativeDistribution();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion)
    {
        log.debug("Begin sendAlertMessage");
        String url = null;

        url = getUrl();

        if (NullChecker.isNotNullish(url))
        {
            AdapterAdministrativeDistributionPortType port = getPort(url);
            RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();

            message.setEDXLDistribution(body);
            message.setAssertion(assertion);

            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);

            try
            {
                getWebServiceProxyHelper().invokePort(port, AdapterAdministrativeDistributionPortType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected AdapterAdministrativeDistributionPortType getPort(String url) {
        AdapterAdministrativeDistributionPortType port = adapterService.getAdapterAdministrativeDistributionPortType();

        log.info("Setting endpoint address to Adapter Administrative DIstribution Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected String getUrl() {
        String url = null;
        AdminDistributionHelper helper = getHelper();


        String target = helper.getLocalCommunityId();


        if (target != null) {
            try {

                url = ConnectionManagerCache.getEndpointURLByServiceName(target, NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }


    
}
