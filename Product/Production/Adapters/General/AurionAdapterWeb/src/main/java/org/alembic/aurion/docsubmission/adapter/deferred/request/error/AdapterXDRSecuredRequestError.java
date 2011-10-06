/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDRRequestErrorSecured_Service", portName = "AdapterXDRRequestErrorSecured_Port_Soap", endpointInterface = "org.alembic.aurion.adapterxdrrequesterrorsecured.AdapterXDRRequestErrorSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterxdrrequesterrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRSecuredRequestError/AdapterXDRRequestSecuredError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterXDRSecuredRequestError
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType body)
    {
        return new AdapterXDRSecuredRequestErrorImpl().provideAndRegisterDocumentSetBRequestError(body, context);
    }
}
