package org.alembic.aurion.docretrieve.nhin.proxy;

import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NhinDocRetrieveProxyNoMTOMWebServiceSecuredImpl implements NhinDocRetrieveProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Retrieve_Service_NoMTOM";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieveNoMTOM.wsdl";
    // NOTE: The WS_ADDRESSING_ACTION does not match the pattern defined. BUT it does match the .wsdl
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayRetrieve";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinDocRetrieveProxyNoMTOMWebServiceSecuredImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Retrieve the document(s) specified in the request.
     *
     * @param request The identifier(s) of the document(s) to be retrieved.
     * @param targetSystem The target system where the message is being sent to.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request,
                                                                                 AssertionType assertion,
                                                                                 NhinTargetSystemType targetSystem)
    {
        log.debug("Begin NhinDocRetrieveProxyNoMTOMWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;

        try
        {
            if (request != null)
            {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystem(targetSystem, sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url))
                {
                    RespondingGatewayRetrievePortType port = getPort(url, NhincConstants.DOC_RETRIEVE_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (RetrieveDocumentSetResponseType) oProxyHelper.invokePort(port, RespondingGatewayRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", request);
                }
                else
                {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            }
            else
            {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  " +
                      "Exception: " + e.getMessage(), e);
            RegistryResponseType regResp = new RegistryResponseType();

            regResp.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document retrieve");
            registryError.setErrorCode("XDSRepostoryError");
            registryError.setSeverity("Error");
            regResp.getRegistryErrorList().getRegistryError().add(registryError);
            response.setRegistryResponse(regResp);
            
        }
        log.debug("End NhinDocRetrieveProxyNoMTOMWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
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
    protected RespondingGatewayRetrievePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        RespondingGatewayRetrievePortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayRetrievePortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

}
