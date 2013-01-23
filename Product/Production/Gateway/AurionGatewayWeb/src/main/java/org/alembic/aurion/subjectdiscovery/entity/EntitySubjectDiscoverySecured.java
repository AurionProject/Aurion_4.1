/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subjectdiscovery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntitySubjectDiscoverySecured", portName = "EntitySubjectDiscoverySecuredPortSoap", endpointInterface = "org.alembic.aurion.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitysubjectdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/EntitySubjectDiscoverySecured/EntitySubjectDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntitySubjectDiscoverySoapHeaderHandler.xml")
public class EntitySubjectDiscoverySecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVSecuredRequestType body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType body)
    {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(body, context);
    }

}
