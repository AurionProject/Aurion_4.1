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

package org.alembic.aurion.docquery.passthru.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseSecuredType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhincproxydocquerydeferredresponsesecured.NhincProxyDocQueryDeferredResponseSecuredPortType;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseProxyWebServiceSecuredImpl implements PassthruDocQueryDeferredResponseProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhincproxydocquerydeferredresponsesecured";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQueryDeferredResponseSecured";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQueryDeferredResponseSecuredPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQueryDeferredResponseSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:nhincproxydocquerydeferredresponsesecured:RespondingGateway_CrossGatewayQueryResponseSecured";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocQueryDeferredResponseProxyWebServiceSecuredImpl()
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
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected NhincProxyDocQueryDeferredResponseSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyDocQueryDeferredResponseSecuredPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocQueryDeferredResponseSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
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

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin respondingGatewayCrossGatewayQuery");
        DocQueryAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PASSTHRU_DOCUMENT_QUERY_DEFERRED_RESP_SECURED_SERVICE_NAME);
            NhincProxyDocQueryDeferredResponseSecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(assertion == null)
            {
                log.error("AssertionType was null");
            }
            else if(target == null)
            {
                log.error("NhinTargetCommunitiesType was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RespondingGatewayCrossGatewayQueryResponseSecuredType request = new RespondingGatewayCrossGatewayQueryResponseSecuredType();
                request.setAdhocQueryResponse(msg);
                request.setNhinTargetSystem(target);

                response = (DocQueryAcknowledgementType)oProxyHelper.invokePort(port, NhincProxyDocQueryDeferredResponseSecuredPortType.class, "respondingGatewayCrossGatewayQuery", request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            response = new DocQueryAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}