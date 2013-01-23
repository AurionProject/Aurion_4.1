/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.passthru;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "NhincProxyPatientDiscovery", portName = "NhincProxyPatientDiscoveryPort", endpointInterface = "org.alembic.aurion.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscovery/NhincProxyPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyPatientDiscoverySoapHeaderHandler.xml")
public class NhincProxyPatientDiscovery {

    @Resource
    private WebServiceContext context;

    protected NhincProxyPatientDiscoveryImpl getNhincProxyPatientDiscoveryImpl() {
        return new NhincProxyPatientDiscoveryImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyPRPAIN201305UVProxyRequest) {

        return getNhincProxyPatientDiscoveryImpl().proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest, getWebServiceContext());
    }
}
