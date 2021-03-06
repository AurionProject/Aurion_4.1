/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.component.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentDocQueryDeferredRequest", portName = "AdapterComponentDocQueryDeferredRequestPortSoap", endpointInterface = "org.alembic.aurion.adaptercomponentdocquerydeferredrequest.AdapterComponentDocQueryDeferredRequestPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentdocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocQueryDeferredRequestUnsecured/AdapterComponentDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterComponentDocQueryDeferredRequestSoapHeaderHandler.xml")
public class AdapterComponentDocQueryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterComponentDocQueryDeferredRequestUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
