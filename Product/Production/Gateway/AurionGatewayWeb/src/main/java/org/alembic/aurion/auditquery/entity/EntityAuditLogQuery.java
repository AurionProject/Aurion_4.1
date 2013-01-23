/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.entity;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityAuditLogQuerySamlService", portName = "EntityAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "org.alembic.aurion.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType", targetNamespace = "urn:org:alembic:aurion:entityauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/EntityAuditLogQuery/EntityAuditLogQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityAuditLogQuerySoapHeaderHandler.xml")
public class EntityAuditLogQuery {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(org.alembic.aurion.common.nhinccommonentity.FindAuditEventsSecuredRequestType findAuditEventsRequest)
    {
        return new EntityAuditLogImpl().findAuditEvents(findAuditEventsRequest, context);
    }

}
