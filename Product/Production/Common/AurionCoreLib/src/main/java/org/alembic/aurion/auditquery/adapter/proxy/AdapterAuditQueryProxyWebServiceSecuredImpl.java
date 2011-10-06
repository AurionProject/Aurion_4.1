package org.alembic.aurion.auditquery.adapter.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.alembic.aurion.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterAuditQueryProxyWebServiceSecuredImpl implements AdapterAuditQueryProxy{

    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adapterauditlogquerysaml";
    private static final String SERVICE_LOCAL_PART = "AdapterAuditLogQuerySamlService";
    private static final String PORT_LOCAL_PART = "AdapterAuditLogQuerySamlPortTypeBindingPort";
    private static final String WSDL_FILE = "AdapterAuditLogQuerySaml.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adapterauditlogquerysaml:FindAuditEventsRequestMessage";

    public AdapterAuditQueryProxyWebServiceSecuredImpl() {
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

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterAuditLogQuerySamlPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterAuditLogQuerySamlPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterAuditLogQuerySamlPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public FindAuditEventsResponseType auditQuery(FindAuditEventsType queryRequest, AssertionType assertion) {
        FindAuditEventsResponseType results = new FindAuditEventsResponseType();

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ADAPTER_AUDIT_QUERY_SECURED_SERVICE_NAME);
            AdapterAuditLogQuerySamlPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(queryRequest == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                FindAuditEventsType request = new FindAuditEventsType();
                if (NullChecker.isNotNullish(queryRequest.getPatientId())) {
                    request.setPatientId(queryRequest.getPatientId());
                }

                if (NullChecker.isNotNullish(queryRequest.getUserId())) {
                    request.setUserId(queryRequest.getUserId());
                }

                if (queryRequest.getBeginDateTime() != null) {
                    request.setBeginDateTime(queryRequest.getBeginDateTime());
                }

                if (queryRequest.getEndDateTime() != null) {
                    request.setEndDateTime(queryRequest.getEndDateTime());
                }

                results = (FindAuditEventsResponseType)oProxyHelper.invokePort(port, AdapterAuditLogQuerySamlPortType.class, "findAuditEvents", request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter audit query secured service: " + ex.getMessage(), ex);
        }

        return results;
    }

}
