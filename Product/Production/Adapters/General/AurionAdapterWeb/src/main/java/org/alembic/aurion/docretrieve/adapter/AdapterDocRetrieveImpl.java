/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import org.alembic.aurion.util.soap.SoapLogger;


/**
 *
 * @author svalluripalli
 */
public class AdapterDocRetrieveImpl
{
    private static Log log = LogFactory.getLog(AdapterDocRetrieveImpl.class);

    /**
     * Perform Doc Retrieve.
     *
     * @param bIsSecure  TRUE if this is being called from a secure web service.
     * @param respondingGatewayCrossGatewayRetrieveRequest The information about the document that is being retrieved.
     * @param context The web service context information.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieveUnsecured(RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest, WebServiceContext context)
    {
        log.debug("Entering AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        AssertionType assertion = null;
        RetrieveDocumentSetRequestType request = null;
        if (respondingGatewayCrossGatewayRetrieveRequest != null)
        {
            request = respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
            assertion = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
        }
        getSoapLogger().logRawAssertion(assertion);

        RetrieveDocumentSetResponseType response = callOrchestrator(request, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;

    }

    /**
     * Perform Doc Retrieve.
     *
     * @param body The information about the document that is being retrieved.
     * @param context The web service context information.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieveSecured(RetrieveDocumentSetRequestType body, WebServiceContext context)
    {
        log.debug("Entering AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        AssertionType assertion = null;
        if (context != null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = new AssertionType();
        }
        getSoapLogger().logRawAssertion(assertion);

        RetrieveDocumentSetResponseType response = callOrchestrator(body, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

    /**
     * Calls the orchestrator.
     * 
     * @param body The message to be sent to the orchestrator.
     * @param assertion The assertion information.
     * @return The response from the orchestrator.
     */
    private RetrieveDocumentSetResponseType callOrchestrator(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        AdapterDocRetrieveOrchImpl oOrchestrator = new AdapterDocRetrieveOrchImpl();
        RetrieveDocumentSetResponseType response = oOrchestrator.respondingGatewayCrossGatewayRetrieve(body, assertion);
        return response;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
