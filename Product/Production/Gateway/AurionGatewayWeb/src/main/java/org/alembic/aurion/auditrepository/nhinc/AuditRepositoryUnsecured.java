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

package org.alembic.aurion.auditrepository.nhinc;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "AuditRepositoryManagerService", portName = "AuditRepositoryManagerPort", endpointInterface = "org.alembic.aurion.nhinccomponentauditrepository.AuditRepositoryManagerPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentauditrepository", wsdlLocation = "WEB-INF/wsdl/AuditRepositoryManagerService/NhincComponentAuditRepository.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")

public class AuditRepositoryUnsecured {
    @Resource
    private WebServiceContext context;
    protected AuditRepositoryUnsecuredImpl getAuditRepositoryUnsecuredImpl() {
        return new AuditRepositoryUnsecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    public org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest) {
        //TODO implement this method
       return getAuditRepositoryUnsecuredImpl().queryAuditEvents(queryAuditEventsRequest, getWebServiceContext());
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType logEvent(org.alembic.aurion.common.auditlog.LogEventRequestType logEventRequest) {
        //TODO implement this method
       return getAuditRepositoryUnsecuredImpl().logEvent(logEventRequest, getWebServiceContext());
    }

}
