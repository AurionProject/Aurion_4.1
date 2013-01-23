/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */

package org.alembic.aurion.auditquery.adapter;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuerySamlService", portName = "AdapterAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "org.alembic.aurion.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType", targetNamespace = "urn:org:alembic:aurion:adapterauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/AdapterSecuredAuditLogQuery/AdapterAuditLogQuerySaml.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterAuditQueryServiceSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class AdapterSecuredAuditLogQuery {

    @Resource
    private WebServiceContext context;

    protected AdapterAuditLogQueryImpl getOrchestrationImpl() {
    	return new AdapterAuditLogQueryImpl();
    }
    
    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(com.services.nhinc.schema.auditmessage.FindAuditEventsType findAuditEventsRequest) {
        return getOrchestrationImpl().queryAdapter(findAuditEventsRequest, context);
    }

}
