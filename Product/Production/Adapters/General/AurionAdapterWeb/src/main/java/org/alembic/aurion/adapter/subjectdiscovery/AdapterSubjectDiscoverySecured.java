/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adapter.subjectdiscovery;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterSubjectDiscoverySecured", portName = "AdapterSubjectDiscoverySecuredPortSoap", endpointInterface = "org.alembic.aurion.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecuredPortType", targetNamespace = "urn:org:alembic:aurion:adaptersubjectdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/AdapterSubjectDiscoverySecured/AdapterSubjectDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterSubjectDiscoverySoapHeaderHandler.xml")
public class AdapterSubjectDiscoverySecured {

    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest, context);
    }

}
