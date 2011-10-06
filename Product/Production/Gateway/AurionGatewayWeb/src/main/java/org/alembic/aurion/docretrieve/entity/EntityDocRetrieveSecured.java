/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveSecured", portName = "EntityDocRetrieveSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitydocretrievesecured.EntityDocRetrieveSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveSecured/EntityDocRetrieveSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocRetrieveSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)
    {
        //return new EntityDocRetrieveSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
        return getImpl().respondingGatewayCrossGatewayQuery(body, context);
    }
    protected EntityDocRetreiveImpl getImpl()
    {
        return new EntityDocRetreiveImpl();
    }
}
