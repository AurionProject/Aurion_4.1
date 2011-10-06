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
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterXDRRequestError_Service", portName = "AdapterXDRRequestError_Port", endpointInterface = "org.alembic.aurion.adapterxdrrequesterror.AdapterXDRRequestErrorPortType", targetNamespace = "urn:org:alembic:aurion:adapterxdrrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestError/AdapterXDRRequestError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterXDRRequestError
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType body)
    {
        return new AdapterXDRSecuredRequestErrorImpl().provideAndRegisterDocumentSetBRequestError(body, context);
    }
}
