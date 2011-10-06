package org.alembic.aurion.auditquery.adapter;

import static org.mockito.Mockito.*;

import javax.xml.ws.WebServiceContext;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class AdapterAuditLogQueryBaseTest {

    protected AdapterAuditLogQueryOrchImpl mockOrchestrationImpl;
    protected AdapterAuditLogQueryImpl auditQueryImpl;

    @Before
    public void setUp() {

        mockOrchestrationImpl = mock(AdapterAuditLogQueryOrchImpl.class);

        auditQueryImpl = new AdapterAuditLogQueryImpl() {
            @Override
            protected AdapterAuditLogQueryOrchImpl getAdapterAuditLogQueryOrchImpl() {
                return mockOrchestrationImpl;
            }
            @Override
            protected AssertionType extractAssertionFromContext(WebServiceContext context) {
                return new AssertionType();
            }
        };
    }

}