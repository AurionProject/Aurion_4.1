/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemSubscribeProxyWebServiceUnsecuredImpl implements PassthruHiemSubscribeProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement";
    private static final String SERVICE_LOCAL_PART = "NhincProxyNotificationProducer";
    private static final String PORT_LOCAL_PART = "NhincProxyNotificationProducerPortSoap";
    private static final String WSDL_FILE = "NhincProxySubscriptionManagement.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement:SubscribeRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruHiemSubscribeProxyWebServiceUnsecuredImpl() {
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
    protected NhincProxyNotificationProducerPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NhincProxyNotificationProducerPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyNotificationProducerPortType.class);
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

    public SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetSystemType target, Element subscribeElement) throws NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, TopicNotSupportedFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, InvalidMessageContentExpressionFault, ResourceUnknownFault {
        log.debug("Begin PassthruHiemSubscribeProxyWebServiceUnsecuredImpl.subscribe");
        SubscribeResponse response = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_SUBSCRIBE_PROXY_SERVICE_NAME);
            NhincProxyNotificationProducerPortType port = getPort(url, NhincConstants.SUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null)
            {
                SubscribeRequestType subscribeMsg = new SubscribeRequestType();
                subscribeMsg.setAssertion(assertion);
                subscribeMsg.setSubscribe(subscribeRequest);
                subscribeMsg.setNhinTargetSystem(target);

                response = (SubscribeResponse) oProxyHelper.invokePort(port, NhincProxyNotificationProducerPortType.class, "subscribe", subscribeMsg);
            }
            else
            {
                log.error("NhincProxyNotificationProducerPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End PassthruHiemSubscribeProxyWebServiceUnsecuredImpl.subscribe");
        return response;
    }

}
