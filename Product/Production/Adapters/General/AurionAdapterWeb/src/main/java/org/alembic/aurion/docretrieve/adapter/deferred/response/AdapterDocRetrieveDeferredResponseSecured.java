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
@WebService(serviceName = "AdapterDocRetrieveDeferredResponseSecured", portName = "AdapterDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocretrievedeferredrespsecured.AdapterDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocretrievedeferredrespsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredResponseSecured/AdapterDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocRetrieveDeferredResponseSoapHandler.xml")
public class AdapterDocRetrieveDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        return new AdapterDocRetrieveDeferredResponseImpl().crossGatewayRetrieveResponse(body, context);
    }

}
