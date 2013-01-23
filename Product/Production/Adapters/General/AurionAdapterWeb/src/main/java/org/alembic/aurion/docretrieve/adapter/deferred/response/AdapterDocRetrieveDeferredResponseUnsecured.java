/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredResponse", portName = "AdapterDocRetrieveDeferredResponsePortSoap", endpointInterface = "org.alembic.aurion.adapterdocretrievedeferredresp.AdapterDocRetrieveDeferredResponsePortType", targetNamespace = "urn:org:alembic:aurion:adapterdocretrievedeferredresp", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredResponseUnsecured/AdapterDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocRetrieveDeferredResponseSoapHandler.xml")
public class AdapterDocRetrieveDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        return new AdapterDocRetrieveDeferredResponseImpl().crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
    }

}
