/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adaptermpi;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterMpiSecuredService", portName = "AdapterMpiSecuredPortType", endpointInterface = "org.alembic.aurion.adaptermpi.AdapterMpiSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adaptermpi", wsdlLocation = "WEB-INF/wsdl/AdapterMpiSecured/AdapterMpiSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterMpiSecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
        return new AdapterMpiSecuredImpl().findCandidates(findCandidatesRequest, context);
    }

}
