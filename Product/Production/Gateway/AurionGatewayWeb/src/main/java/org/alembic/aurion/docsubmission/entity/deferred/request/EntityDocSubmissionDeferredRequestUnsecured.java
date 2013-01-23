/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.entity.deferred.request;

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
@WebService(serviceName = "EntityXDRAsyncRequest_Service", portName = "EntityXDRAsyncRequest_Port", endpointInterface = "org.alembic.aurion.nhincentityxdr.async.request.EntityXDRAsyncRequestPortType", targetNamespace = "urn:org:alembic:aurion:nhincentityxdr:async:request", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionDeferredRequestUnsecured/EntityXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocSubmissionDeferredRequestSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocSubmissionDeferredRequestUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest) {
        return new EntityDocSubmissionDeferredRequestImpl().provideAndRegisterDocumentSetBAsyncRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
