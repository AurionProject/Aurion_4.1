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
@WebService(serviceName = "AdapterDocQueryDeferredRequestErrorSecured", portName = "AdapterDocQueryDeferredRequestErrorSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocquerydeferredrequesterrorsecured.AdapterDocQueryDeferredRequestErrorSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocquerydeferredrequesterrorsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestErrorSecured/AdapterDocQueryDeferredRequestErrorSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocQueryDeferredRequestErrorSoapHandler.xml")
public class AdapterDocQueryDeferredRequestErrorSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorSecuredType body) {
        return new AdapterDocQueryDeferredRequestErrorSecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
