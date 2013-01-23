/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.entity.deferred.response;

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
@WebService(serviceName = "EntityXDRSecuredAsyncResponse_Service", portName = "EntityXDRSecuredAsyncResponse_Port", endpointInterface = "org.alembic.aurion.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType", targetNamespace = "urn:org:alembic:aurion:nhincentityxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionDeferredResponseSecured/EntityXDRSecuredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocSubmissionDeferredResponseSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocSubmissionDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredAsyncRespRequest) {
        return new EntityDocSubmissionDeferredResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredAsyncRespRequest, context);
    }

}
