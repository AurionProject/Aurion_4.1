/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.docsubmission.passthru.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "ProxyXDRAsyncResponse_Service", portName = "ProxyXDRAsyncResponse_Port", endpointInterface = "org.alembic.aurion.nhincproxyxdr.async.response.ProxyXDRAsyncResponsePortType", targetNamespace = "urn:org:alembic:aurion:nhincproxyxdr:async:response", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionDeferredResponseUnsecured/NhincProxyXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PassthruDocSubmissionDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterAsyncRespRequest) {
        return new PassthruDocSubmissionDeferredResponseImpl().provideAndRegisterDocumentSetBResponseOrch(provideAndRegisterAsyncRespRequest, context);
    }

}
