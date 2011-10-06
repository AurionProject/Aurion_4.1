/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityAuditLogQuery", portName = "EntityAuditLogQueryPortSoap", endpointInterface = "org.alembic.aurion.entityauditlogquery.EntityAuditLogQueryPortType", targetNamespace = "urn:org:alembic:aurion:entityauditlogquery", wsdlLocation = "WEB-INF/wsdl/EntityAuditQuery/EntityAuditLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityAuditQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(org.alembic.aurion.common.nhinccommonentity.FindAuditEventsRequestType findAuditEventsRequest) {
        return new EntityAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
