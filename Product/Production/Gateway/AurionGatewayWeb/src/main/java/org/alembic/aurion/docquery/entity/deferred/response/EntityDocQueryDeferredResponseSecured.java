/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityDocQueryDeferredResponseSecured", portName = "EntityDocQueryDeferredResponseSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitydocquerydeferredresponsesecured.EntityDocQueryDeferredResponseSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitydocquerydeferredresponsesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredResponseSecured/EntityDocQueryDeferredResponseSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "EntityDocQueryDeferredResponseSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocQueryDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryResponse(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseSecuredType crossGatewayQueryResponse) {
        return new EntityDocQueryDeferredResponseSecuredImpl().crossGatewayQueryResponse(crossGatewayQueryResponse, context);
    }

}
