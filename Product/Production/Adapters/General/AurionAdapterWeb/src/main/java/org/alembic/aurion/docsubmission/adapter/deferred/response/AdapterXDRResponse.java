/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.response;

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
@WebService(serviceName = "AdapterXDRResponse_Service", portName = "AdapterXDRResponse_Port", endpointInterface = "org.alembic.aurion.adapterxdrresponse.AdapterXDRResponsePortType", targetNamespace = "urn:org:alembic:aurion:adapterxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponse/AdapterXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterXDRResponseSoapHandler.xml")
@Addressing(enabled = true)
public class AdapterXDRResponse
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(org.alembic.aurion.common.nhinccommonadapter.AdapterRegistryResponseType body)
    {
        return new AdapterXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }
}
