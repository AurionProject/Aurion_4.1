package org.alembic.aurion.auditquery.adapter;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;

public class AdapterAuditLogQueryOrchImpl {

	private Log log = null;
	
	public AdapterAuditLogQueryOrchImpl() {
		log = createLogger();
	}
	
    protected Log createLogger() {
		return LogFactory.getLog(getClass());
	}

	protected AuditRepositoryLogger createAuditRepositoryLogger() {
		return new AuditRepositoryLogger();
	}
	
	protected AuditRepositoryProxyObjectFactory createAuditRepositoryProxyObjectFactory() {
		return new AuditRepositoryProxyObjectFactory();
	}
	
	/**
     * This method will perform an audit log query to the local audit repository.  A list of audit
     * log records will be returned to the calling community that match the search criteria.
     *
     * @param auditQueryMessage The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType performAuditLogQuery(com.services.nhinc.schema.auditmessage.FindAuditEventsType auditQueryMessage, AssertionType assertion)
    {
        log.debug("Entering AdapterAuditQueryImpl.queryAdapter method...");

        log.debug("incomming adapter audit query request: " + auditQueryMessage.toString());

        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        // Set up the audit query request message for the nhinc audit repository manager
        org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType findAuditEventsRequest =
                new org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType();
        findAuditEventsRequest.setAssertion(assertion);
        findAuditEventsRequest.setFindAuditEvents(auditQueryMessage);

        // Audit the Audit Log Query Request Message received on the Adapter Interface
        audit(auditQueryMessage, findAuditEventsRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        AuditRepositoryProxyObjectFactory auditRepoFactory = createAuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        FindCommunitiesAndAuditEventsResponseType result  = proxy.auditQuery(findAuditEventsRequest);

        if (result.getFindAuditEventResponse() != null &&
                NullChecker.isNotNullish(result.getFindAuditEventResponse().getFindAuditEventsReturn())) {
           for (AuditMessageType auditMsg:result.getFindAuditEventResponse().getFindAuditEventsReturn()) {
               response.getFindAuditEventsReturn().add(auditMsg);
           }
        }

        log.debug("outgoing adapter audit query response: " + response);

        log.debug("Exiting AdapterAuditQueryImpl.queryAdapter method...");
        return response;
    }

    private void audit(com.services.nhinc.schema.auditmessage.FindAuditEventsType auditQueryMsg, AssertionType assertion, String direction, String messageInterface)
    {
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = createAuditRepositoryLogger();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        org.alembic.aurion.common.auditlog.FindAuditEventsMessageType message = new org.alembic.aurion.common.auditlog.FindAuditEventsMessageType();
 
        message.setAssertion(assertion);
        message.setFindAuditEvents(auditQueryMsg);

        LogEventRequestType auditLogMsg = auditLogger.logFindAuditEvents(message, direction, messageInterface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = createAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            proxy.auditLog(auditLogMsg, assertion);
        }
    }

}
