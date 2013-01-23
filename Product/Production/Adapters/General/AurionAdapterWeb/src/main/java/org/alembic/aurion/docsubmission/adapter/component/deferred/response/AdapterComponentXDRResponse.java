/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.component.deferred.response;

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
@WebService(serviceName = "AdapterComponentXDRResponse_Service", portName = "AdapterComponentXDRResponse_Port", endpointInterface = "org.alembic.aurion.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterComponentXDRResponse/AdapterComponentXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterComponentXDRResponseSoapHandler.xml")
@Addressing(enabled=true)
public class AdapterComponentXDRResponse {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(org.alembic.aurion.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        return new AdapterComponentXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }

}
