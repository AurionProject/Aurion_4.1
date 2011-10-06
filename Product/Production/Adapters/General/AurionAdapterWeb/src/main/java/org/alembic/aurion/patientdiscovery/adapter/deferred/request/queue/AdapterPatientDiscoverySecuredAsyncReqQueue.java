/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.patientdiscovery.adapter.deferred.request.queue;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecuredAsyncReqQueue", portName = "AdapterPatientDiscoverySecuredAsyncReqQueuePortSoap", endpointInterface = "org.alembic.aurion.adapterpatientdiscoverysecuredasyncreqqueue.AdapterPatientDiscoverySecuredAsyncReqQueuePortType", targetNamespace = "urn:org:alembic:aurion:adapterpatientdiscoverysecuredasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecuredAsyncReqQueue/AdapterPatientDiscoverySecuredAsyncReqQueue.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoverySecuredAsyncReqQueue {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterPatientDiscoverySecuredAsyncReqQueueImpl().addPatientDiscoveryAsyncReqSecured(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
