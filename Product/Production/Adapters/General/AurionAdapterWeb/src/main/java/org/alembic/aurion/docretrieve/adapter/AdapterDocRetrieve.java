/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "AdapterDocRetrieve", portName = "AdapterDocRetrievePortSoap", endpointInterface = "org.alembic.aurion.adapterdocretrieve.AdapterDocRetrievePortType", targetNamespace = "urn:org:alembic:aurion:adapterdocretrieve", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieve/AdapterDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocRetrieveSoapHandler.xml")
public class AdapterDocRetrieve
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new AdapterDocRetrieveImpl().respondingGatewayCrossGatewayRetrieveUnsecured(respondingGatewayCrossGatewayRetrieveRequest, context);
    }
}
