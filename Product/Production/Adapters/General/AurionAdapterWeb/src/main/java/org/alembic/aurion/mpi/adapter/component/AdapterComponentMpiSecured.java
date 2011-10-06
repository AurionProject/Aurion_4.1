/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.mpi.adapter.component;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;


/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterComponentMpiSecuredService", portName = "AdapterComponentMpiSecuredPort", endpointInterface = "org.alembic.aurion.adaptercomponentmpi.AdapterComponentMpiSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentmpi", wsdlLocation = "WEB-INF/wsdl/AdapterComponentMpiSecured/AdapterComponentSecuredMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentMpiSecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
        AdapterComponentMpiImpl oImpl = new AdapterComponentMpiImpl();
        return oImpl.findCandidates(true, findCandidatesRequest, context);
    }
}
