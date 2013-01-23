/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru.deferred.response;

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
@WebService(serviceName = "NhincProxyDocQueryDeferredResponse", portName = "NhincProxyDocQueryDeferredResponsePortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocquerydeferredresponse.NhincProxyDocQueryDeferredResponsePortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredResponseUnsecured/NhincProxyDocQueryDeferredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "PassthruDocQueryDeferredResponseSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        return new PassthruDocQueryDeferredResponseUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
