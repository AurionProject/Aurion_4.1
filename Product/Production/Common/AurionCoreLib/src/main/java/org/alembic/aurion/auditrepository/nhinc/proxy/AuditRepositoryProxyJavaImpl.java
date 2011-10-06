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

package org.alembic.aurion.auditrepository.nhinc.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.auditlog.LogEventSecureRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import org.alembic.aurion.auditrepository.nhinc.AuditRepositoryOrchImpl;
/**
 *
 * @author mflynn02
 */
public class AuditRepositoryProxyJavaImpl implements AuditRepositoryProxy {
   private Log log = null;

    public AuditRepositoryProxyJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected AuditRepositoryOrchImpl getAuditRepositoryOrchImpl() {
        return new AuditRepositoryOrchImpl();
    }
    /**
     * Performs a query to the audit repository.
     *
     * @param request Audit query search criteria.
     * @return List of Audit records that match the search criteria along with a list of referenced communities.
     */
    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        return getAuditRepositoryOrchImpl().findAudit(request.getFindAuditEvents(), request.getAssertion());
    }

    /**
     * Logs an audit record to the audit repository.
     *
     * @param request Audit record
     * @return Repsonse that is a simple ack.
     */
    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {

        LogEventSecureRequestType securedRequest = new LogEventSecureRequestType();
        securedRequest.setAuditMessage(request.getAuditMessage());
        securedRequest.setDirection(request.getDirection());
        securedRequest.setInterface(request.getInterface());
        
        return getAuditRepositoryOrchImpl().logAudit(securedRequest, assertion);

    }


}
