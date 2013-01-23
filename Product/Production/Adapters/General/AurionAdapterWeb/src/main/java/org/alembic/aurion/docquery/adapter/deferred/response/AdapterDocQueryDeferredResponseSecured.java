/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredResponseSecured", portName = "AdapterDocQueryDeferredResponseSecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocquerydeferredresponsesecured.AdapterDocQueryDeferredResponseSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterDocQueryDeferredResponseSecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredResponseSecured/AdapterDocQueryDeferredResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterDocQueryDeferredResponseSoapHandler.xml")
public class AdapterDocQueryDeferredResponseSecured
{

    @Resource
    private WebServiceContext context;
    

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureResponseType respondingGatewayCrossGatewayQueryRequest)
    {
        return new AdapterDocQueryDeferredResponseSecuredImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest, context);
    }
}
