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

package org.alembic.aurion.docretrieve.adapter.component.deferred.response;

import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import org.alembic.aurion.docretrieve.adapter.deferred.response.AdapterDocRetrieveDeferredResponseImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentDocRetrieveResponseService", portName = "AdapterComponentDocRetrieveResponsePortSoap", endpointInterface = "org.alembic.aurion.adaptercomponentdocretrievedeferredresp.AdapterComponentDocRetrieveDeferredRespPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentdocretrievedeferredresp", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocRetrieveDeferredResponse/AdapterComponentDocumentRetrieveDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocRetrieveDeferredResponse {
    @Resource
    private WebServiceContext context;

    public DocRetrieveAcknowledgementType retrieveDocumentSetResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        return new AdapterComponentDocRetrieveDeferredResponseImpl().crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
    }

}
