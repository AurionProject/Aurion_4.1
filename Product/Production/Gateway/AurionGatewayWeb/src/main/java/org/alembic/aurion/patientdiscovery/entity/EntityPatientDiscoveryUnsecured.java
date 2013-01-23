/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import javax.xml.ws.soap.Addressing;
/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscovery", portName = "EntityPatientDiscoveryPortSoap", endpointInterface = "org.alembic.aurion.entitypatientdiscovery.EntityPatientDiscoveryPortType", targetNamespace = "urn:org:alembic:aurion:entitypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryUnsecured/EntityPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityPatientDiscoverySoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityPatientDiscoveryUnsecured
{
    @Resource
    private WebServiceContext context;
    
    protected EntityPatientDiscoveryUnsecuredImpl getEntityPatientDiscoveryUnsecuredImpl()
    {
        return new EntityPatientDiscoveryUnsecuredImpl();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        EntityPatientDiscoveryUnsecuredImpl impl = getEntityPatientDiscoveryUnsecuredImpl();
        if(impl != null)
        {
            response = impl.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, context);
        }

        return response;
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }

}
