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

import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;

/**
 *
 * @author jhoppesc
 */
public class AuditRepositoryProxyNoOpImpl implements AuditRepositoryProxy {

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        return new FindCommunitiesAndAuditEventsResponseType();
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        return new AcknowledgementType();
    }

}
