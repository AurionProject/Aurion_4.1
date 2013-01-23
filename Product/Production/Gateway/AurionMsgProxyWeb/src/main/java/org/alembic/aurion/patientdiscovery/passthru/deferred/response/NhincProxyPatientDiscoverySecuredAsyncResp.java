/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.passthru.deferred.response;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecuredAsyncResp", portName = "NhincProxyPatientDiscoverySecuredAsyncRespPortType", endpointInterface = "org.alembic.aurion.nhincproxypatientdiscoverysecuredasyncresp.NhincProxyPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxypatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecuredAsyncResp/NhincProxyPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyPatientDiscoveryAsyncRespSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class NhincProxyPatientDiscoverySecuredAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType proxyProcessPatientDiscoveryAsyncRespRequest) {
        return new NhincProxyPatientDiscoveryAsyncRespImpl().proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest, context);
    }

}
