package org.alembic.aurion.auditquery.adapter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.FindAuditEventsMessageType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;

public class AdapterAuditLogQueryOrchImplTest {

	private AdapterAuditLogQueryOrchImpl sut;
	private AuditRepositoryLogger mockAuditRepositoryLogger;
	private AuditRepositoryProxyObjectFactory mockAuditRepositoryProxyObjectFactory;
	private Log mockLog;
	private AuditRepositoryProxy mockAuditRepositoryProxy;

	@Before
	public void setUp() {
		mockLog = mock(Log.class);
		mockAuditRepositoryLogger = mock(AuditRepositoryLogger.class);
		mockAuditRepositoryProxyObjectFactory = mock(AuditRepositoryProxyObjectFactory.class);
		
		sut = new AdapterAuditLogQueryOrchImpl() {
		    protected Log createLogger() {
				return mockLog;
			}
			protected AuditRepositoryLogger createAuditRepositoryLogger() {
				return mockAuditRepositoryLogger;
			}
			protected AuditRepositoryProxyObjectFactory createAuditRepositoryProxyObjectFactory() {
				return mockAuditRepositoryProxyObjectFactory;
			}
		};
		
		// Set up the proxy object for sending messages to the audit repository.
		mockAuditRepositoryProxy = mock(AuditRepositoryProxy.class);
		when(mockAuditRepositoryProxyObjectFactory.getAuditRepositoryProxy()).thenReturn(mockAuditRepositoryProxy);
		
	}
	
	@Test
	public void testAuditQueryWithTwoResponseMessages() {
		
		setResponseForAuditEvents();
		
		// Build a response message for the repository query. This response has two audit messages.
		FindCommunitiesAndAuditEventsResponseType queryResponse = new FindCommunitiesAndAuditEventsResponseType();
		FindAuditEventsResponseType auditEventsResponse = new FindAuditEventsResponseType();
		auditEventsResponse.getFindAuditEventsReturn().add(new AuditMessageType());
		auditEventsResponse.getFindAuditEventsReturn().add(new AuditMessageType());
		queryResponse.setFindAuditEventResponse(auditEventsResponse);
		when(mockAuditRepositoryProxy.auditQuery(any(FindCommunitiesAndAuditEventsRequestType.class))).thenReturn(queryResponse);
		
		// Call the audit query operation
		FindAuditEventsResponseType response = sut.performAuditLogQuery(new FindAuditEventsType() , new AssertionType());
		
		// Verify the results
		assertNotNull("Response was null.", response);
		assertEquals("Audit event count.", 2, response.getFindAuditEventsReturn().size());
	}
	
	@Test
	public void testAuditQueryNullAuditEventResponse() {
		
		setResponseForAuditEvents();
		
		// Build a response message for the repository query. This response has two audit messages.
		FindCommunitiesAndAuditEventsResponseType queryResponse = new FindCommunitiesAndAuditEventsResponseType();
		FindAuditEventsResponseType auditEventsResponse = null;
		queryResponse.setFindAuditEventResponse(auditEventsResponse);
		when(mockAuditRepositoryProxy.auditQuery(any(FindCommunitiesAndAuditEventsRequestType.class))).thenReturn(queryResponse);
		
		// Call the audit query operation
		FindAuditEventsResponseType response = sut.performAuditLogQuery(new FindAuditEventsType() , new AssertionType());
		
		// Verify the results
		assertNotNull("Response was null.", response);
		assertEquals("Audit event count.", 0, response.getFindAuditEventsReturn().size());
	}
	
	@Test
	public void testAuditQueryNoResponseMessages() {
		
		setResponseForAuditEvents();
		
		// Build a response message for the repository query. This response has two audit messages.
		FindCommunitiesAndAuditEventsResponseType queryResponse = new FindCommunitiesAndAuditEventsResponseType();
		FindAuditEventsResponseType auditEventsResponse = new FindAuditEventsResponseType();
		queryResponse.setFindAuditEventResponse(auditEventsResponse);
		when(mockAuditRepositoryProxy.auditQuery(any(FindCommunitiesAndAuditEventsRequestType.class))).thenReturn(queryResponse);
		
		// Call the audit query operation
		FindAuditEventsResponseType response = sut.performAuditLogQuery(new FindAuditEventsType() , new AssertionType());
		
		// Verify the results
		assertNotNull("Response was null.", response);
		assertEquals("Audit event count.", 0, response.getFindAuditEventsReturn().size());
	}
	
	@Test
	public void testAuditQueryNullLogEventRequest() {
		
		// Set the return for logFindAuditEvents to null
		when(mockAuditRepositoryLogger.logFindAuditEvents(any(FindAuditEventsMessageType.class),any(String.class), any(String.class))).thenReturn(null);
		
		// Build a response message for the repository query. This response has two audit messages.
		FindCommunitiesAndAuditEventsResponseType queryResponse = new FindCommunitiesAndAuditEventsResponseType();
		FindAuditEventsResponseType auditEventsResponse = new FindAuditEventsResponseType();
		auditEventsResponse.getFindAuditEventsReturn().add(new AuditMessageType());
		auditEventsResponse.getFindAuditEventsReturn().add(new AuditMessageType());
		queryResponse.setFindAuditEventResponse(auditEventsResponse);
		when(mockAuditRepositoryProxy.auditQuery(any(FindCommunitiesAndAuditEventsRequestType.class))).thenReturn(queryResponse);
		
		// Call the audit query operation
		FindAuditEventsResponseType response = sut.performAuditLogQuery(new FindAuditEventsType() , new AssertionType());
		
		// Verify the results
		assertNotNull("Response was null.", response);
		assertEquals("Audit event count.", 2, response.getFindAuditEventsReturn().size());
		
		// Verify that the audit log was not called
		verify(mockAuditRepositoryProxy, never()).auditLog(any(LogEventRequestType.class), any(AssertionType.class));
	}
	
	private void setResponseForAuditEvents() {
		// Set up response message for logging the audit log request
		when(mockAuditRepositoryLogger.logFindAuditEvents(any(FindAuditEventsMessageType.class),any(String.class), any(String.class))).thenReturn(new LogEventRequestType() );
	}

}
