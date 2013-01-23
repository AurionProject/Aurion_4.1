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
import javax.jws.HandlerChain;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "org.alembic.aurion.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentmpi", wsdlLocation = "WEB-INF/wsdl/AdapterComponentMpi/AdapterComponentMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterComponentMpiSoapHandler.xml")
public class AdapterComponentMpi
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
        AdapterComponentMpiImpl oImpl = new AdapterComponentMpiImpl();
        return oImpl.findCandidates(false, findCandidatesRequest, context);
    }
}
