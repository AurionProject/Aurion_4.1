/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "ProxyXDRSecuredAsyncRequest_Service", portName = "ProxyXDRSecuredAsyncRequest_Port", endpointInterface = "org.alembic.aurion.nhincproxyxdrsecured.async.request.ProxyXDRSecuredAsyncRequestPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxyxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionDeferredRequestSecured/NhincProxyXDRSecuredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruDocSubmissionDeferredRequestSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocSubmissionDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterAsyncReqRequest) {
        return new PassthruDocSubmissionDeferredRequestImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
