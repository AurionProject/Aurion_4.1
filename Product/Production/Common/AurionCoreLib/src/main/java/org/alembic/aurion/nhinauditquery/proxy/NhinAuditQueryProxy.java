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

package org.alembic.aurion.nhinauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import org.alembic.aurion.common.nhinccommonproxy.FindAuditEventsRequestType;

/**
 *
 * @author Jon Hoppesch
 */
public interface NhinAuditQueryProxy {

    /**
     * Performs a query to the audit repository.
     *
     * @param request Audit query search criteria.
     * @return List of Audit records that match the search criteria along with a list of referenced communities.
     */
    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request);
}
