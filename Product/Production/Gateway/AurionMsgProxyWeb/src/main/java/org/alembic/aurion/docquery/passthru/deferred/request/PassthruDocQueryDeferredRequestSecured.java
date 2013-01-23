/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author patlollav
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredRequestSecured", portName = "NhincProxyDocQueryDeferredRequestSecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocquerydeferredrequestsecured.NhincProxyDocQueryDeferredRequestSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocquerydeferredrequestsecured", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredRequestSecured/NhincProxyDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruDocQueryDeferredRequestSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocQueryDeferredRequestSecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType crossGatewayQueryRequest)
    {
        PassthruDocQueryDeferredRequestSecuredImpl oImpl = new PassthruDocQueryDeferredRequestSecuredImpl();
        return oImpl.crossGatewayQueryRequest(crossGatewayQueryRequest, context);
    }
}
