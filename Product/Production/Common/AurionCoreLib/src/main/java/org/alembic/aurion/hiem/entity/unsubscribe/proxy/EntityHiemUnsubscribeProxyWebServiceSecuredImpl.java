/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.unsubscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author Sai Vallurpalli
 */
public class EntityHiemUnsubscribeProxyWebServiceSecuredImpl implements EntityHiemUnsubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:entitysubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "EntitySubscriptionManagerSecured";
    private static final String PORT_LOCAL_PART = "EntitySubscriptionManagerSecuredPortSoap";
    private static final String WSDL_FILE = "EntitySubscriptionManagementSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:entitysubscriptionmanagementsecured:UnsubscribeRequestSecuredMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityHiemUnsubscribeProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected EntitySubscriptionManagerSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntitySubscriptionManagerSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntitySubscriptionManagerSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, ReferenceParametersElements referenceParametersElements) throws org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault, org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault {
        log.debug("Begin EntityUnsubscribeProxyWebserviceSecuredImpl.Unsubscribe");
        UnsubscribeResponse response = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
            EntitySubscriptionManagerSecuredPortType port = getPort(url, NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null) {
                response = (UnsubscribeResponse) oProxyHelper.invokePort(port, EntitySubscriptionManagerSecuredPortType.class, "unsubscribe", unsubscribeRequest);
            } else {
                log.error("EntitySubscriptionManagerSecuredPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End EntityUnsubscribeProxyWebserviceSecuredImpl.Unsubscribe");
        return response;
    }
}
