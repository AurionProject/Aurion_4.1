/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReqError", portName = "AdapterPatientDiscoveryAsyncReqErrorPortSoap", endpointInterface = "org.alembic.aurion.adapterpatientdiscoveryasyncreqerror.AdapterPatientDiscoveryAsyncReqErrorPortType", targetNamespace = "urn:org:alembic:aurion:adapterpatientdiscoveryasyncreqerror", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryDeferredRequestErrorUnsecured/AdapterPatientDiscoveryAsyncReqError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "AdapterPatientDiscoveryDeferredRequestErrorSoapHandler.xml")
public class AdapterPatientDiscoveryDeferredRequestErrorUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType processPatientDiscoveryAsyncReqErrorRequest) {
        return new AdapterPatientDiscoverySecuredDeferredRequestErrorImpl().processPatientDiscoveryAsyncReqError(processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201305UV02(), processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201306UV02(), processPatientDiscoveryAsyncReqErrorRequest.getAssertion(), processPatientDiscoveryAsyncReqErrorRequest.getErrorMsg(), context);
    }

}
