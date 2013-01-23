/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery;

import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import org.alembic.aurion.common.nhinccommonentity.FindAuditEventsRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditQuery {

    private static Log log = LogFactory.getLog(EntityAuditQuery.class);

    /**
     * This method will query the audit log repository at a specified home community for audit
     * log records that match a specified criteria.
     *
     * @param auditQueryReq The search criteria for the audit log query
     * @param homeCommunityId The home community to perform an audit log query to.
     * @return A list of audit log records that match the specified criteria along with a list of referenced
     * communities from these records.
     */
    public FindCommunitiesAndAuditEventsResponseType query(FindAuditEventsRequestType auditQueryReq) {
        log.debug("Entering EntityAuditQuery.query...");
        
        FindCommunitiesAndAuditEventsRequestType request = new FindCommunitiesAndAuditEventsRequestType();
        request.setAssertion(auditQueryReq.getAssertion());
        request.setFindAuditEvents(auditQueryReq.getFindAuditEvents());

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        log.debug("Exiting EntityAuditQuery.query...");
        return proxy.auditQuery(request);
    }

}
