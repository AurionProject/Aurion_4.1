/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru.deferred.request;

import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredRequest", portName = "NhincProxyDocRetrieveDeferredRequestPortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequestPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocretrievedeferredrequest", wsdlLocation = "WEB-INF/wsdl/PassthruDocRetrieveDeferredRequestUnsecured/NhincProxyDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruDocRetrieveDeferredRequestSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocRetrieveDeferredRequestUnsecured extends PassthruDocRetrieveDeferredRequestImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
    }
}
