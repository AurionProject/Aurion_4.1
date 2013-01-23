/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.adapter;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuery", portName = "AdapterAuditLogQueryPortSoap", endpointInterface = "org.alembic.aurion.adapterauditlogquery.AdapterAuditLogQueryPortType", targetNamespace = "urn:org:alembic:aurion:adapterauditlogquery", wsdlLocation = "WEB-INF/wsdl/AdapterAuditQueryService/AdapterAuditLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterAuditQueryServiceSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class AdapterAuditQueryService {

    protected AdapterAuditLogQueryImpl getOrchestrationImpl() {
    	return new AdapterAuditLogQueryImpl();
    }
    
    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(org.alembic.aurion.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest)
    {
        return getOrchestrationImpl().queryAdapter(findAuditEventsRequest.getFindAuditEvents(), findAuditEventsRequest.getAssertion());
    }

}
