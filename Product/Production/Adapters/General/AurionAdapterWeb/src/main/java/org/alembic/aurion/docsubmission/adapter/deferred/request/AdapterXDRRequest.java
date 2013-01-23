/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequest_Service", portName = "AdapterXDRRequest_Port", endpointInterface = "org.alembic.aurion.adapterxdrrequest.AdapterXDRRequestPortType", targetNamespace = "urn:org:alembic:aurion:adapterxdrrequest", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequest/AdapterXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterXDRRequestSoapHandler.xml")
@Addressing(enabled=true)
public class AdapterXDRRequest {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

}
