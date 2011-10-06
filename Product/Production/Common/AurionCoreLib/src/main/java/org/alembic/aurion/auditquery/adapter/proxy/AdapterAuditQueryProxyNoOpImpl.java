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
public class AdapterAuditQueryProxyNoOpImpl implements AdapterAuditQueryProxy {

    public FindAuditEventsResponseType auditQuery(FindAuditEventsType queryRequest, AssertionType assertion) {
        return new FindAuditEventsResponseType();
    }

}
