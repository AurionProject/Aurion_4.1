/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.adapter;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 * This is the code that is for the unsecured PatientDiscovery service that is used
 * when running in pass through mode.
 *
 * @author Les Westberg
 */
@WebService(serviceName = "AdapterPatientDiscovery", portName = "AdapterPatientDiscoveryPortSoap", endpointInterface = "org.alembic.aurion.adapterpatientdiscovery.AdapterPatientDiscoveryPortType", targetNamespace = "urn:org:alembic:aurion:adapterpatientdiscovery", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscovery/AdapterPatientDiscovery.wsdl")
@HandlerChain(file = "AdapterPatientDiscoverySoapHandler.xml")
public class AdapterPatientDiscovery
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        return new AdapterPatientDiscoveryImpl().respondingGatewayPRPAIN201305UV02(false, respondingGatewayPRPAIN201305UV02Request, context);
    }
}
