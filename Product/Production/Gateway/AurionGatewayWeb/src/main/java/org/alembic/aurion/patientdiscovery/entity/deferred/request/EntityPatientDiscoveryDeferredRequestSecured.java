/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncReq", portName = "EntityPatientDiscoverySecuredAsyncReqPortSoap", endpointInterface = "org.alembic.aurion.entitypatientdiscoverysecuredasyncreq.EntityPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:org:alembic:aurion:entitypatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredRequestSecured/EntityPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityPatientDiscoveryDeferredRequestSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityPatientDiscoveryDeferredRequestSecured
{

    @Resource
    private WebServiceContext context;

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02SecuredRequestType request)
    {
        MCCIIN000002UV01 response = null;

        EntityPatientDiscoveryDeferredRequestImpl serviceImpl = getEntityPatientDiscoveryDeferredRequestImpl();
        if (serviceImpl != null)
        {
            response = serviceImpl.processPatientDiscoveryAsyncRequestSecured(request, getWebServiceContext());
        }
        return response;
    }

    protected EntityPatientDiscoveryDeferredRequestImpl getEntityPatientDiscoveryDeferredRequestImpl()
    {
        return new EntityPatientDiscoveryDeferredRequestImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
