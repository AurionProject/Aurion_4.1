/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import javax.xml.ws.soap.Addressing;

@WebService(serviceName = "EntityDocQuerySecured", portName = "EntityDocQuerySecuredPortSoap", endpointInterface = "org.alembic.aurion.entitydocquery.EntityDocQuerySecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQuerySecured/EntityDocQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocQuerySoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocQuerySecured {

    @Resource
    private WebServiceContext context;

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        AdhocQueryResponse response = null;

        EntityDocQueryImpl serviceImpl = getEntityDocQueryImpl();
        if(serviceImpl != null)
        {
            response =  serviceImpl.respondingGatewayCrossGatewayQuerySecured(body, getWebServiceContext());
        }
        return response;
    }

    protected EntityDocQueryImpl getEntityDocQueryImpl()
    {
        return new EntityDocQueryImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
