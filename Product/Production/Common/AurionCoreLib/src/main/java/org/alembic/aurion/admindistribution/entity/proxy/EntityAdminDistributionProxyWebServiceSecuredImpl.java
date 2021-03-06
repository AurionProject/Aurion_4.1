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

package org.alembic.aurion.admindistribution.entity.proxy;
import org.alembic.aurion.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.entityadmindistribution.AdministrativeDistributionSecuredPortType;
import org.alembic.aurion.entityadmindistribution.AdministrativeDistributionSecuredService;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenCreator;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistributionProxyWebServiceSecuredImpl {
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:entityadmindistribution";
    private static final String SERVICE_LOCAL_PART = "AdministrativeDistributionSecured_Service";
    private static final String PORT_LOCAL_PART = "AdministrativeDistributionSecured_PortType";
    private static final String WSDL_FILE = "EntityAdminDistSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:entityadmindistribution:SendAlertMessage_Message";

    private Log log = null;
    static AdministrativeDistributionSecuredService service = null;
    public EntityAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        service = getWebService();
    }
    protected AdministrativeDistributionSecuredService getWebService()
    {
        return new AdministrativeDistributionSecuredService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdministrativeDistributionSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdministrativeDistributionSecuredPortType port = service.getAdministrativeDistributionSecuredPortType();

        log.info("Setting endpoint address to Entity Administrative DIstribution Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        log.debug("begin sendAlert()");


        AdminDistributionHelper helper = new AdminDistributionHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            AdministrativeDistributionSecuredPortType port = getPort(url, NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            WebServiceProxyHelper webServiceHelper = new WebServiceProxyHelper();
            webServiceHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.ADMIN_DIST_ACTION, WS_ADDRESSING_ACTION, assertion);;

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();
            message.setEDXLDistribution(body);
            message.setNhinTargetCommunities(target);

            try
            {
                log.debug("invoke port");
                getWebServiceProxyHelper().invokePort(port, AdministrativeDistributionSecuredPortType.class, "sendAlertMessage", body);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }

    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

}
