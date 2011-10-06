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
package org.alembic.aurion.auditquery;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.FindAuditEventsMessageType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommonentity.FindAuditEventsRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditLog {

    private static Log log = LogFactory.getLog(EntityAuditLog.class);

    /**
     * This method will log Audit Query Requests received on the Entity Interface
     *
     * @param auditMsg The Audit Query Request message to be audit logged.
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    public AcknowledgementType audit(FindAuditEventsRequestType auditMsg) {
        log.debug("Entering EntityAuditLog.audit (entity)...");

        FindAuditEventsMessageType auditReqMsg = new FindAuditEventsMessageType();
        auditReqMsg.setAssertion(auditMsg.getAssertion());
        auditReqMsg.setFindAuditEvents(auditMsg.getFindAuditEvents());

        AcknowledgementType ack = logAuditQuery(auditReqMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        log.debug("Exiting EntityAuditLog.audit (entity)...");
        return ack;
    }

    /**
     * This method will log Audit Query Requests sent on the Nhin Interface
     *
     * @param auditMsg The Audit Query Request message to be audit logged.
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    public AcknowledgementType audit(
            org.alembic.aurion.common.nhinccommonproxy.FindAuditEventsRequestType auditMsg) {
        log.debug("Entering EntityAuditLog.audit (proxy)...");

        FindAuditEventsMessageType auditReqMsg = new FindAuditEventsMessageType();
        auditReqMsg.setAssertion(auditMsg.getAssertion());
        auditReqMsg.setFindAuditEvents(auditMsg.getFindAuditEvents());

        AcknowledgementType ack = logAuditQuery(auditReqMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting EntityAuditLog.audit (proxy)...");
        return ack;
    }

    /**
     * This method will log Audit Query Requests received/sent on a particular public interface
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return An acknowledgement of whether or not the message was successfully logged.
     */
    private AcknowledgementType logAuditQuery(FindAuditEventsMessageType message, String direction, String _interface) {
        AcknowledgementType ack = new AcknowledgementType();
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logFindAuditEvents(message, direction, _interface);

        if (auditLogMsg != null) {
            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
            ack = proxy.auditLog(auditLogMsg, message.getAssertion());
        }

        return ack;
    }
}
