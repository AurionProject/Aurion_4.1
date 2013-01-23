/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestSecured", portName = "AdapterDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocretrievedeferredreqsecured.AdapterDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocretrievedeferredreqsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredRequestSecured/AdapterDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocRetrieveDeferredRequestSoapHandler.xml")
public class AdapterDocRetrieveDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return new AdapterDocRetrieveDeferredRequestImpl().crossGatewayRetrieveRequest(body, context);
    }

}
