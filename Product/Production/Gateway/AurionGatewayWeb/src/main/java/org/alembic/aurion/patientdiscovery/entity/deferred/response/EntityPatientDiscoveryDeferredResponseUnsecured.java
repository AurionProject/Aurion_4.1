/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscoveryAsyncResp", portName = "EntityPatientDiscoveryAsyncRespPortSoap", endpointInterface = "org.alembic.aurion.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:org:alembic:aurion:entitypatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredResponseUnsecured/EntityPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityPatientDiscoveryDeferredResponseUnsecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest)
    {
        return new EntityPatientDiscoveryDeferredResponseImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest, context);
    }

}
