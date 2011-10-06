/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.docretrieve.entity;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityDocRetrieve", portName = "EntityDocRetrievePortSoap", endpointInterface = "org.alembic.aurion.entitydocretrieve.EntityDocRetrievePortType", targetNamespace = "urn:org:alembic:aurion:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieve/EntityDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocRetrieve {
    
    @Resource
    private WebServiceContext context;
    
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
//todo: pass in context
        return getImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest(), respondingGatewayCrossGatewayRetrieveRequest.getAssertion(),context);
    }
    protected EntityDocRetreiveImpl getImpl()
    {
        return new EntityDocRetreiveImpl();
    }
}
