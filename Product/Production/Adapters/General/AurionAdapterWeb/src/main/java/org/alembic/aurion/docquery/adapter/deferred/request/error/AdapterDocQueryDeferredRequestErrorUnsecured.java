/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredRequestError", portName = "AdapterDocQueryDeferredRequestErrorPortSoap", endpointInterface = "org.alembic.aurion.adapterdocquerydeferredrequesterror.AdapterDocQueryDeferredRequestErrorPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocquerydeferredrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestErrorUnsecured/AdapterDocQueryDeferredRequestError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocQueryDeferredRequestErrorSoapHandler.xml")
public class AdapterDocQueryDeferredRequestErrorUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterDocQueryDeferredRequestErrorUnsecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }

}
