/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.unsubscribe.proxy;

import com.sun.xml.ws.developer.WSBindingProvider;
import org.alembic.aurion.adaptersubscriptionmanagement.AdapterSubscriptionManagerPortType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.UnsubscribeRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AdapterHiemUnsubscribeProxyWebServiceUnsecuredImpl implements AdapterHiemUnsubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adaptersubscriptionmanagement";
    private static final String SERVICE_LOCAL_PART = "AdapterSubscriptionManager";
    private static final String PORT_LOCAL_PART = "AdapterSubscriptionManagerPortSoap";
    private static final String WSDL_FILE = "AdapterSubscriptionManagement.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adaptersubscriptionmanagement:UnsubscribeRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterHiemUnsubscribeProxyWebServiceUnsecuredImpl() {
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
    protected AdapterSubscriptionManagerPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterSubscriptionManagerPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterSubscriptionManagerPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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

    public UnsubscribeResponse unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion) {
        UnsubscribeResponse response = new UnsubscribeResponse();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME);
            AdapterSubscriptionManagerPortType port = getPort(url, NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (port != null) {
                log.debug("attaching reference parameter headers");
                SoapUtil soapUtil = new SoapUtil();
                soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                log.debug("unmarshalling unsubscribe element");
                WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
                Unsubscribe unsubscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

                log.debug("building unsubscribe message");
                UnsubscribeRequestType adapterUnsubscribeRequest = new UnsubscribeRequestType();
                adapterUnsubscribeRequest.setUnsubscribe(unsubscribe);
                adapterUnsubscribeRequest.setAssertion(assertion);

                log.debug("invoking unsubscribe port");
                response = (UnsubscribeResponse) oProxyHelper.invokePort(port, AdapterSubscriptionManagerPortType.class, "unsubscribe", adapterUnsubscribeRequest);
            } else {
                throw new RuntimeException("Unable to create adapter port");
            }
        } catch (Exception ex) {
            log.error("Error calling unsubscribe: " + ex.getMessage(), ex);
        }
        return response;
    }

}
