/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.unsubscribe.proxy;

import com.sun.xml.ws.developer.WSBindingProvider;

import org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterSubscriptionManagerPortSecuredType;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
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
public class AdapterHiemUnsubscribeProxyWebServiceSecuredImpl implements AdapterHiemUnsubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "AdapterSubscriptionManagerSecured";
    private static final String PORT_LOCAL_PART = "AdapterSubscriptionManagerPortSoap";
    private static final String WSDL_FILE = "AdapterSubscriptionManagementSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured:UnsubscribeRequestSecuredMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterHiemUnsubscribeProxyWebServiceSecuredImpl() {
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
    protected AdapterSubscriptionManagerPortSecuredType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterSubscriptionManagerPortSecuredType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterSubscriptionManagerPortSecuredType.class);
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

    public UnsubscribeResponse unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion) {
        UnsubscribeResponse response = new UnsubscribeResponse();

        log.debug("start secured unsubscribe");
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_SECURED_NAME);

            AdapterSubscriptionManagerPortSecuredType port = getPort(url, NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (port != null) {
                log.debug("attaching reference parameter headers");
                SoapUtil soapUtil = new SoapUtil();
                soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                log.debug("unmarshalling unsubscribe element");
                WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
                Unsubscribe unubscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

                log.debug("invoking unsubscribe port");
                response = (UnsubscribeResponse) oProxyHelper.invokePort(port, AdapterSubscriptionManagerPortSecuredType.class, "unubscribe", unubscribe);

            } else {
                throw new RuntimeException("Unable to create adapter port");
            }
        } catch (Exception ex) {
            log.error("Error calling subscribe: " + ex.getMessage(), ex);
        }

        log.debug("end secured unsubscribe");
        return response;
    }

    
}
