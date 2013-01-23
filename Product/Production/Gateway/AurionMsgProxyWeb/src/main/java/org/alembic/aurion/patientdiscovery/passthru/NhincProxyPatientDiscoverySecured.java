/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.passthru;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecured", portName = "NhincProxyPatientDiscoverySecuredPort", endpointInterface = "org.alembic.aurion.nhincproxypatientdiscoverysecured.NhincProxyPatientDiscoverySecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxypatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecured/NhincProxyPatientDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyPatientDiscoverySoapHeaderHandler.xml")
public class NhincProxyPatientDiscoverySecured {

    @Resource
    private WebServiceContext context;

    protected NhincProxyPatientDiscoverySecuredImpl getNhincProxyPatientDiscoverySecuredImpl() {
        return new NhincProxyPatientDiscoverySecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType proxyPRPAIN201305UVProxyRequest) {

        return getNhincProxyPatientDiscoverySecuredImpl().proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest, getWebServiceContext());
    }
}
