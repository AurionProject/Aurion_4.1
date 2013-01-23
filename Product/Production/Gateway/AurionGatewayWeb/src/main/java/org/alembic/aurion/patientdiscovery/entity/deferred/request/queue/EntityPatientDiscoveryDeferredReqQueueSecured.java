/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.request.queue;

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
@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncReqQueue", portName = "EntityPatientDiscoverySecuredAsyncReqQueuePortSoap", endpointInterface = "org.alembic.aurion.entitypatientdiscoverysecuredasyncreqqueue.EntityPatientDiscoverySecuredAsyncReqQueuePortType", targetNamespace = "urn:org:alembic:aurion:entitypatientdiscoverysecuredasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredReqQueueSecured/EntityPatientDiscoverySecuredAsyncReqQueue.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityPatientDiscoveryDeferredReqQueueSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityPatientDiscoveryDeferredReqQueueSecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoverySecuredDeferredReqQueueImpl().addPatientDiscoveryAsyncReq(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
