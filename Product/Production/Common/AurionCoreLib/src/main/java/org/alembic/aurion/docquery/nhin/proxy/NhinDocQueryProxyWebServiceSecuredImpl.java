/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.nhin.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryProxyWebServiceSecuredImpl implements NhinDocQueryProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Query_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Query_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQuery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    public NhinDocQueryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayQueryPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayQueryPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryPortType.class);
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

    /**
     * Calls the respondingGatewayCrossGatewayQuery method of the web service.
     *
     * @param request The information for the web service.
     * @return The response from the web service.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion, NhinTargetSystemType target) {
        AdhocQueryResponse response = new AdhocQueryResponse();

        try {
            String url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.DOC_QUERY_SERVICE_NAME);
            RespondingGatewayQueryPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (request == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("AssertionType was null");
            } else if (target == null) {
                log.error("target was null");
            } else if (port == null) {
                log.error("port was null");
            } else {
                response = (AdhocQueryResponse) oProxyHelper.invokePort(port, RespondingGatewayQueryPortType.class, "respondingGatewayCrossGatewayQuery", request);
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            RegistryErrorList registryErrorList = new RegistryErrorList();

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document query");
            registryError.setErrorCode("XDSRepostoryError");
            registryError.setSeverity("Error");
            registryErrorList.getRegistryError().add(registryError);
            response.setRegistryErrorList(registryErrorList);
        }
        return response;
    }
}
