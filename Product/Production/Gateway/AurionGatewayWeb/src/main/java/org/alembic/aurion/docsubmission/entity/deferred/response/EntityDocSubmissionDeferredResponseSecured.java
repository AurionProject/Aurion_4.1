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

package org.alembic.aurion.docsubmission.entity.deferred.response;

import javax.annotation.Resource;
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
@Addressing(enabled=true)
public class EntityDocSubmissionDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredAsyncRespRequest) {
        return new EntityDocSubmissionDeferredResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredAsyncRespRequest, context);
    }

}
