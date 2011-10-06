/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.adapter.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.alembic.aurion.adapterauditlogquery.AdapterAuditLogQueryPortType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindAuditEventsRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryProxyWebServiceUnsecuredImpl implements AdapterAuditQueryProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:adapterauditlogquery";
    private static final String SERVICE_LOCAL_PART = "AdapterAuditLogQuery";
    private static final String PORT_LOCAL_PART = "AdapterAuditLogQueryPortSoap";
    private static final String WSDL_FILE = "AdapterAuditLogQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:adapterauditlogquery:findAuditEventsRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterAuditQueryProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
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
    protected AdapterAuditLogQueryPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterAuditLogQueryPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterAuditLogQueryPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public FindAuditEventsResponseType auditQuery(FindAuditEventsType queryRequest, AssertionType assertion) {
        FindAuditEventsResponseType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_LOG_ADAPTER_SERVICE_NAME);
            AdapterAuditLogQueryPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(queryRequest == null)
            {
                log.error("Message was null");
            }
            else if(assertion == null)
            {
                log.error("assertion was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                FindAuditEventsRequestType request = new FindAuditEventsRequestType();
                request.setFindAuditEvents(queryRequest);
                request.setAssertion(assertion);
                FindAuditEventsResponseType resp = port.findAuditEvents(request);
                response = (FindAuditEventsResponseType)oProxyHelper.invokePort(port, AdapterAuditLogQueryPortType.class, "findAuditEvents", request);
            }
        } catch (Exception ex) {
            log.error("Error calling adapter audit query unsecure service: " + ex.getMessage(), ex);
        }

        return response;
    }

}
