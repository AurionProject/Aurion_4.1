/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.mpi.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * This class is the implementation of the Unsecured AdapterMPI service.
 *
 * @author Sai Valluripalli, Les Westberg
 */
@WebService(serviceName = "AdapterMpiService", portName = "AdapterMpiPort", endpointInterface = "org.alembic.aurion.adaptermpi.AdapterMpiPortType", targetNamespace = "urn:org:alembic:aurion:adaptermpi", wsdlLocation = "WEB-INF/wsdl/AdapterMpi/AdapterMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterMpiSoapHandler.xml")
public class AdapterMpi {

    @Resource
    private WebServiceContext context;

    /**
     * This method takes the input and peforms a query against the MPI via the AdapterComponentMpi services
     * and returns the response.
     *
     * @param findCandidatesRequest The query data.
     * @return The results from the MPI query.
     */
    public PRPAIN201306UV02 findCandidates(RespondingGatewayPRPAIN201305UV02RequestType findCandidatesRequest)
    {
        AdapterMpiImpl oImpl = new AdapterMpiImpl();
        PRPAIN201306UV02 oResponse = oImpl.query(false, findCandidatesRequest.getPRPAIN201305UV02(), findCandidatesRequest.getAssertion(), context);
        return oResponse;
    }

}
