package org.alembic.aurion.auditquery.adapter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.junit.Test;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;

public class AdapterSecuredAuditLogQueryTest extends AdapterAuditLogQueryBaseTest {

    @Test
    public void testProcessAuditQueryHappyPath() {

        // Subject under test
        AdapterSecuredAuditLogQuery sut = new AdapterSecuredAuditLogQuery() {
            @Override
            protected AdapterAuditLogQueryImpl getOrchestrationImpl() {
                return auditQueryImpl;
            }
        };

        // Create response object and register it with the call on the mock object
        FindAuditEventsResponseType auditResponse = new FindAuditEventsResponseType();
        when(mockOrchestrationImpl.performAuditLogQuery(any(FindAuditEventsType.class), any(AssertionType.class))).thenReturn(auditResponse );

        FindAuditEventsType request = new FindAuditEventsType();
        // Prepare request and call the service method
        FindAuditEventsResponseType response = sut.findAuditEvents(request  );

        // Assert the response is not null
        assertNotNull(response);
    }
}
