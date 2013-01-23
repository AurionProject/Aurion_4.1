/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncResp", portName = "EntityPatientDiscoverySecuredAsyncRespPortSoap", endpointInterface = "org.alembic.aurion.entitypatientdiscoverysecuredasyncresp.EntityPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:org:alembic:aurion:entitypatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredResponseSecured/EntityPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityPatientDiscoveryDeferredResponseSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityPatientDiscoveryDeferredResponseSecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType processPatientDiscoveryAsyncRespAsyncRequest)
    {
        return new EntityPatientDiscoveryDeferredResponseImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest, context);
    }

}
