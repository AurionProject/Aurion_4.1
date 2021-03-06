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

package org.alembic.aurion.auditrepository.nhinc.proxy;

import org.alembic.aurion.nhinccomponentauditrepository.AuditRepositoryManagerPortType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.auditlog.LogEventSecureRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;

import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryProxyWebServiceUnsecuredImpl implements AuditRepositoryProxy{
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhinccomponentauditrepository";
    private static final String SERVICE_LOCAL_PART = "AuditRepositoryManagerService";
    private static final String PORT_LOCAL_PART = "AuditRepositoryManagerPort";
    private static final String WSDL_FILE = "NhincComponentAuditRepository.wsdl";
    private static final String WS_ADDRESSING_ACTION_LOG = "urn:org:alembic:aurion:nhinccomponentauditrepository:LogEventRequest";
    private static final String WS_ADDRESSING_ACTION_FIND = "urn:org:alembic:aurion:nhinccomponentauditrepository:QueryAuditEventsRequest";
    private Log log = null;

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();


    public AuditRepositoryProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        String url = null;
        FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();

        try
        {
            if (request != null)
            {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.AUDIT_REPO_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url))
                {
                    AuditRepositoryManagerPortType port = getPort(url, NhincConstants.AUDIT_REPO_SERVICE_NAME, WS_ADDRESSING_ACTION_FIND, request.getAssertion());
                    result = (FindCommunitiesAndAuditEventsResponseType) oProxyHelper.invokePort(port, AuditRepositoryManagerPortType.class, "queryAuditEvents", request);
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SERVICE_NAME + ").  The URL is null.");
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        return result;
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        log.debug("Entering AuditRepositoryProxyWebServiceUnsecuredImpl.auditLog(...)");
        String url = null;
        AcknowledgementType result = new AcknowledgementType();

        if(request.getAuditMessage() == null)
        {
            log.error("Audit Request is null");
        }

        if (request.getAssertion() == null)
        {
            request.setAssertion(assertion);
        }

        try
        {
            if (request != null)
            {

                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.AUDIT_REPO_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.AUDIT_REPO_SERVICE_NAME + " is: " + url);
                if (NullChecker.isNotNullish(url))
                {
                    AuditRepositoryManagerPortType port = getPort(url, NhincConstants.AUDIT_REPO_SERVICE_NAME, WS_ADDRESSING_ACTION_LOG, assertion);
                    result = (AcknowledgementType) oProxyHelper.invokePort(port, AuditRepositoryManagerPortType.class, "logEvent", request);
                }
                else
                {
                    log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SERVICE_NAME + ").  The URL is null.");
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + NhincConstants.AUDIT_REPO_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
        }

        log.debug("In AuditRepositoryProxyWebServiceUnsecuredImpl.auditLog(...) - completed called to ConnectionManager to retrieve endpoint.");

        return result;
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
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AuditRepositoryManagerPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AuditRepositoryManagerPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AuditRepositoryManagerPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

}
