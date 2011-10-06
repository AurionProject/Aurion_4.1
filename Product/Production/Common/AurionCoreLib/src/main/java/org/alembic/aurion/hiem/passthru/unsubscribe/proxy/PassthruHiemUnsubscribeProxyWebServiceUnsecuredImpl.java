/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.unsubscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType;
import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemUnsubscribeProxyWebServiceUnsecuredImpl implements PassthruHiemUnsubscribeProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement";
    private static final String SERVICE_LOCAL_PART = "NhincProxySubscriptionManager";
    private static final String PORT_LOCAL_PART = "NhincProxySubscriptionManagerPortSoap";
    private static final String WSDL_FILE = "NhincProxySubscriptionManagement.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement:UnsubscribeRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruHiemUnsubscribeProxyWebServiceUnsecuredImpl() {
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
    protected NhincProxySubscriptionManagerPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NhincProxySubscriptionManagerPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxySubscriptionManagerPortType.class);
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

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        log.debug("Begin PassthruHiemUnsubscribeProxyWebServiceUnsecuredImpl.Unsubscribe");
        UnsubscribeResponse response = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_UNSUBSCRIBE_PROXY_SERVICE_NAME);
            NhincProxySubscriptionManagerPortType port = getPort(url, NhincConstants.UNSUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null) {
                UnsubscribeRequestType unsubscribeMsg = new UnsubscribeRequestType();
                unsubscribeMsg.setAssertion(assertion);
                unsubscribeMsg.setUnsubscribe(unsubscribeRequest);
                response = (UnsubscribeResponse) oProxyHelper.invokePort(port, NhincProxySubscriptionManagerPortType.class, "unsubscribe", unsubscribeMsg);
            } else {
                log.error("NhincProxySubscriptionManagerPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End PassthruHiemUnsubscribeProxyWebServiceUnsecuredImpl.Unsubscribe");
        return response;
    }

}
