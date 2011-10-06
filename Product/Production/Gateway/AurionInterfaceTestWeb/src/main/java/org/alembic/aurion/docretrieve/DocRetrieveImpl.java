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
package org.alembic.aurion.docretrieve;

import org.alembic.aurion.common.nhinccommoninternalorch.RespondingGatewayCrossGatewayRetrieveRequestType;
import org.alembic.aurion.nhincdocretrieve.NhincDocRetrievePortType;
import org.alembic.aurion.nhincdocretrieve.NhincDocRetrieveService;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class DocRetrieveImpl {

    private static Log log = LogFactory.getLog(DocRetrieveImpl.class);
    private static final String SERVICE_NAME = "mockdocumentretrieve";

    RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, WebServiceContext context) {
        log.debug("Entering DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        RetrieveDocumentSetResponseType resp = new RetrieveDocumentSetResponseType();

        RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        crossGatewayRetrieveRequest.setRetrieveDocumentSetRequest(body);
        crossGatewayRetrieveRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        NhincDocRetrieveService service = new NhincDocRetrieveService();
        NhincDocRetrievePortType port = service.getNhincDocRetrievePortTypeBindingPort();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

        resp = port.respondingGatewayCrossGatewayRetrieve(crossGatewayRetrieveRequest);
        log.debug("Exiting DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return resp;
    }
}
