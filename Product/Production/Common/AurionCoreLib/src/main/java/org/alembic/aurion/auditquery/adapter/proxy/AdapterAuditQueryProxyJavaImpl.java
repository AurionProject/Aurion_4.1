package org.alembic.aurion.auditquery.adapter.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import org.alembic.aurion.auditquery.adapter.AdapterAuditLogQueryOrchImpl;
import org.alembic.aurion.common.nhinccommon.AssertionType;

/**
 *
 * @author Neil Webb
 */
public class AdapterAuditQueryProxyJavaImpl implements AdapterAuditQueryProxy {

    public FindAuditEventsResponseType auditQuery(FindAuditEventsType queryRequest, AssertionType assertion) {
        return new AdapterAuditLogQueryOrchImpl().performAuditLogQuery(queryRequest, assertion);
    }

}
