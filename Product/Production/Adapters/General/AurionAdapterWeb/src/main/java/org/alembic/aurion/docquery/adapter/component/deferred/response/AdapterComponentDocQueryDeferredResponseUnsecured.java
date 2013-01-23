/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.component.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentDocQueryDeferredResponse", portName = "AdapterComponentDocQueryDeferredResponsePortSoap", endpointInterface = "org.alembic.aurion.adaptercomponentdocquerydeferredresponse.AdapterComponentDocQueryDeferredResponsePortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentdocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocQueryDeferredResponseUnsecured/AdapterComponentDocQueryDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterComponentDocQueryDeferredResponseSoapHeaderHandler.xml")
public class AdapterComponentDocQueryDeferredResponseUnsecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterComponentDocQueryDeferredResponseUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
