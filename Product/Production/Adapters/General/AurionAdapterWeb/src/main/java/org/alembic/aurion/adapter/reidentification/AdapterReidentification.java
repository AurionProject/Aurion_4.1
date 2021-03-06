/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adapter.reidentification;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterReidentification", portName = "AdapterReidentificationBindingSoap", endpointInterface = "org.alembic.aurion.adapterreidentification.AdapterReidentificationPortType", targetNamespace = "urn:org:alembic:aurion:adapterreidentification", wsdlLocation = "WEB-INF/wsdl/AdapterReidentification/AdapterReidentification.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterReidentificationSoapHeaderHandler.xml")
public class AdapterReidentification {

    public org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType getRealIdentifierRequest) {
        AdapterReidentificationImpl impl = new AdapterReidentificationImpl();
        return impl.getRealIdentifier(getRealIdentifierRequest);
    }

}
