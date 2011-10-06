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
package org.alembic.aurion.hiem.adapter.notify.proxy;

import com.sun.xml.ws.developer.WSBindingProvider;
import org.alembic.aurion.adapternotificationconsumersecured.AdapterNotificationConsumerPortSecureType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.dte.SoapUtil;

import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.*;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterHiemNotifyProxyWebServiceSecuredImpl implements AdapterHiemNotifyProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adapternotificationconsumersecured";
    private static final String SERVICE_LOCAL_PART = "AdapterNotificationConsumerSecured";
    private static final String PORT_LOCAL_PART = "AdapterNotificationConsumerPortSecureType";
    private static final String WSDL_FILE = "AdapterNotificationConsumerSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adapternotificationconsumersecured:NotifyRequestSecured";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterHiemNotifyProxyWebServiceSecuredImpl() {
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
    protected AdapterNotificationConsumerPortSecureType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterNotificationConsumerPortSecureType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterNotificationConsumerPortSecureType.class);
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

    public AcknowledgementType notify(Element notifyElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion) throws Exception {
        AcknowledgementType response = null;

        log.debug("start secured notify");

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_SECURED_NAME);
            AdapterNotificationConsumerPortSecureType port = getPort(url, NhincConstants.NOTIFY_ACTION, WS_ADDRESSING_ACTION, assertion);

            WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
            Notify notify = subscribeMarshaller.unmarshalNotifyRequest(notifyElement);

            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

            response = (AcknowledgementType) oProxyHelper.invokePort(port, AdapterNotificationConsumerPortSecureType.class, "notify", notify);
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }

        log.debug("end secured notify");

        return response;
    }

    public AcknowledgementType notifySubscribersOfDocument(Element docNotify, AssertionType assertion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AcknowledgementType notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
