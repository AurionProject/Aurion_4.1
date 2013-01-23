/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru.deferred.response;

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
@WebService(serviceName = "ProxyXDRSecuredAsyncResponse_Service", portName = "ProxyXDRSecuredAsyncResponse_Port", endpointInterface = "org.alembic.aurion.nhincproxyxdrsecured.async.response.ProxyXDRSecuredAsyncResponsePortType", targetNamespace = "urn:org:alembic:aurion:nhincproxyxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionDeferredResponseSecured/NhincProxyXDRSecuredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruDocSubmissionDeferredResponseSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocSubmissionDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterAsyncRespRequest) {
        return new PassthruDocSubmissionDeferredResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterAsyncRespRequest, context);
    }

}
