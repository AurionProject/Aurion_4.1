/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredRequestSecured", portName = "AdapterDocQueryDeferredRequestSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocquerydeferredrequestsecured.AdapterDocQueryDeferredRequestSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocquerydeferredrequestsecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestSecured/AdapterDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocQueryDeferredRequestSoapHeaderHandler.xml")
public class AdapterDocQueryDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureRequestType body) {
        return new AdapterDocQueryDeferredRequestSecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
