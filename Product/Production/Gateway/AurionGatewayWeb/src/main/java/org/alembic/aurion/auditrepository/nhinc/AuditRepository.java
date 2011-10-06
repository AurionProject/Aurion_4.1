/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditrepository.nhinc;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AuditRepositoryManagerSecuredService", portName = "AuditRepositoryManagerSecuredPort", endpointInterface = "org.alembic.aurion.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentauditrepository", wsdlLocation = "WEB-INF/wsdl/AuditRepository/NhincComponentAuditRepositorySecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AuditRepository
{
    @Resource
    private WebServiceContext context;
    protected AuditRepositorySecuredImpl getAuditRepositorySecuredImpl() {
        return new AuditRepositorySecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }
    public org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest)
    {
        return getAuditRepositorySecuredImpl().findAudit(queryAuditEventsRequest.getFindAuditEvents(), getWebServiceContext());
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType logEvent(org.alembic.aurion.common.auditlog.LogEventSecureRequestType logEventRequest)
    {
       return getAuditRepositorySecuredImpl().logAudit(logEventRequest, getWebServiceContext());
    }

}
