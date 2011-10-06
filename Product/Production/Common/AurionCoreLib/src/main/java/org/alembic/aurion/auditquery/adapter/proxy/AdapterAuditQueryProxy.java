/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.adapter.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import org.alembic.aurion.common.nhinccommon.AssertionType;

/**
 *
 * @author Jon Hoppesch
 */
public interface AdapterAuditQueryProxy {

    /**
     * Performs a query to the audit repository.
     *
     * @param request Audit query search criteria.
     * @return List of Audit records that match the search criteria along with a list of referenced communities.
     */
    public FindAuditEventsResponseType auditQuery(FindAuditEventsType queryRequest, AssertionType assertion);

}
