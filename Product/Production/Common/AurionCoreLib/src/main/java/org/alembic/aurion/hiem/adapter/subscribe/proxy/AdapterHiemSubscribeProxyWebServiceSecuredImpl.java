/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;

import org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType;

import org.alembic.aurion.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;

import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterHiemSubscribeProxyWebServiceSecuredImpl implements AdapterHiemSubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterNotificationProducerSecured";
    private static final String PORT_LOCAL_PART = "AdapterNotificationProducerPortSecuredSoap";
    private static final String WSDL_FILE = "AdapterSubscriptionManagementSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured:SubscribeRequestSecuredMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterHiemSubscribeProxyWebServiceSecuredImpl() {
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
    protected AdapterNotificationProducerPortSecuredType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterNotificationProducerPortSecuredType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterNotificationProducerPortSecuredType.class);
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

    public SubscribeResponse subscribe(Element subscribeElement, AssertionType assertion) throws Exception {
        SubscribeResponse response = null;

        log.debug("start secured subscribe");
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SECURED_SERVICE_NAME);
            AdapterNotificationProducerPortSecuredType port = getPort(url, NhincConstants.SUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);

            WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
            Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);

            response = (SubscribeResponse) oProxyHelper.invokePort(port, AdapterNotificationProducerPortSecuredType.class, "subscribe", subscribe);
        } catch (Exception ex) {
            log.error("Error calling subscribe: " + ex.getMessage(), ex);
        }

        log.debug("end secured subscribe");

        return response;
    }
}
