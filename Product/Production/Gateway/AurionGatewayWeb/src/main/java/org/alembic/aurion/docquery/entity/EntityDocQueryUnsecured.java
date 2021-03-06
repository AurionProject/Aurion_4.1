/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import javax.xml.ws.soap.Addressing;

@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap", endpointInterface = "org.alembic.aurion.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:org:alembic:aurion:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryUnsecured/EntityDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocQuerySoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocQueryUnsecured
{

    @Resource
    private WebServiceContext context;

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request)
    {
        AdhocQueryResponse response = null;

        EntityDocQueryImpl impl = getEntityDocQueryImpl();
        if (impl != null)
        {
            response = impl.respondingGatewayCrossGatewayQueryUnsecured(request, getWebServiceContext());
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
