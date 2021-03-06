/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredRequestErrorSecuredService", portName = "AdapterDocRetrieveDeferredRequestErrorSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocretrievedeferredreqerrorsecured.AdapterDocRetrieveDeferredRequestErrorSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocretrievedeferredreqerrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredRequestErrorSecured/AdapterDocRetrieveDeferredReqErrorSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocRetrieveDeferredRequestErrorSoapHandler.xml")
public class AdapterDocRetrieveDeferredRequestErrorSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType body) {
        return new AdapterDocRetrieveDeferredRequestErrorImpl().crossGatewayRetrieveRequestError(body, context);
    }

}
