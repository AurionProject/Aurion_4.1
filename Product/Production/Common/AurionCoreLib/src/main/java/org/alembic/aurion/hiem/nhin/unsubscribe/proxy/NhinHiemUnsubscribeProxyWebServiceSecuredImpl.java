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
package org.alembic.aurion.hiem.nhin.unsubscribe.proxy;

import com.sun.xml.ws.developer.WSBindingProvider;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManager;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author rayj
 */
public class NhinHiemUnsubscribeProxyWebServiceSecuredImpl implements NhinHiemUnsubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "SubscriptionManagerService";
    private static final String PORT_LOCAL_PART = "SubscriptionManagerPort";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2/SubscriptionManager/UnsubscribeRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinHiemUnsubscribeProxyWebServiceSecuredImpl() {
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
    protected SubscriptionManager getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        SubscriptionManager port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), SubscriptionManager.class);
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

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        Element responseElement = null;
        UnsubscribeResponse response = null;
        String url = null;

        log.debug("In NhinSubscribeWebserviceProxy.subscribe()");

        try {
            log.debug("Before target system URL look up.");
            url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);

            log.debug("After target system URL look up. URL for service: " + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME + " is: " + url);
            SubscriptionManager port = getPort(url, NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (port != null) {
                log.debug("attaching reference parameter headers");
                SoapUtil soapUtil = new SoapUtil();
                soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                log.debug("unmarshalling unsubscribe element");
                WsntUnsubscribeMarshaller marshaller = new WsntUnsubscribeMarshaller();
                Unsubscribe unsubscribe = marshaller.unmarshal(unsubscribeElement);

                log.debug("invoking unsubscribe port");
                response = (UnsubscribeResponse)oProxyHelper.invokePort(port, SubscriptionManager.class, "unsubscribe", unsubscribe);

                log.debug("marshalling unsubscribe response");
                WsntUnsubscribeResponseMarshaller responseMarshaller = new WsntUnsubscribeResponseMarshaller();
                responseElement = responseMarshaller.marshal(response);
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return responseElement;
    }

    
}
