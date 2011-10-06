/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.NotifyRequestSecuredType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhincproxynotificationconsumersecured.NhincProxyNotificationConsumerSecuredPortType;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemNotifyProxyWebServiceSecuredImpl implements PassthruHiemNotifyProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincproxynotificationconsumersecured";
    private static final String SERVICE_LOCAL_PART = "NhincProxyNotificationConsumerSecured";
    private static final String PORT_LOCAL_PART = "NhincProxyNotificationConsumerPortSoap";
    private static final String WSDL_FILE = "NhincProxyNotificationConsumerSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincproxynotificationconsumersecured:NotifyRequestSecuredMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruHiemNotifyProxyWebServiceSecuredImpl() {
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
    protected NhincProxyNotificationConsumerSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NhincProxyNotificationConsumerSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyNotificationConsumerSecuredPortType.class);
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

    public void notify(Notify notifyRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements, Element notifyElement) {
        log.debug("Begin EntityNotifyProxyWebserviceUnsecuredImpl.notify");
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED);
            NhincProxyNotificationConsumerSecuredPortType port = getPort(url, NhincConstants.NOTIFY_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null)
            {
                NotifyRequestSecuredType notifyMsg = new NotifyRequestSecuredType();
                notifyMsg.setNotify(notifyRequest);
                notifyMsg.setNhinTargetSystem(target);
                oProxyHelper.invokePort(port, NhincProxyNotificationConsumerSecuredPortType.class, "notify", notifyMsg);
            }
            else
            {
                log.error("EntityNotificationConsumerPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End EntityNotifyProxyWebserviceUnsecuredImpl.notify");
        return;
    }

}
