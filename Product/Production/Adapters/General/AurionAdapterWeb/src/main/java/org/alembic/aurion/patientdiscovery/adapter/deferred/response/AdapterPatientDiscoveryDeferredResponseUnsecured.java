/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncResp", portName = "AdapterPatientDiscoveryAsyncRespPortSoap", endpointInterface = "org.alembic.aurion.adapterpatientdiscoveryasyncresp.AdapterPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:org:alembic:aurion:adapterpatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredResponseUnsecured/AdapterPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "AdapterPatientDiscoveryDeferredResponseSoapHandler.xml")
public class AdapterPatientDiscoveryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new AdapterPatientDiscoverySecuredDeferredResponseImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest.getPRPAIN201306UV02(), processPatientDiscoveryAsyncRespAsyncRequest.getAssertion(), context);
    }

}
