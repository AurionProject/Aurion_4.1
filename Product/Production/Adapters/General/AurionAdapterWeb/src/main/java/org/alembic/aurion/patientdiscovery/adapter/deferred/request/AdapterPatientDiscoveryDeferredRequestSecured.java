/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.adapter.deferred.request;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecuredAsyncReq", portName = "AdapterPatientDiscoverySecuredAsyncReqPortSoap", endpointInterface = "org.alembic.aurion.adapterpatientdiscoverysecuredasyncreq.AdapterPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:org:alembic:aurion:adapterpatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredRequestSecured/AdapterPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "AdapterPatientDiscoveryDeferredRequestSoapHandler.xml")
public class AdapterPatientDiscoveryDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterPatientDiscoveryDeferredRequestImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest.getPRPAIN201305UV02(), context);
    }

}
