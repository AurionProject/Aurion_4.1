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

package org.alembic.aurion.admindistribution;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import org.alembic.aurion.nhinclib.NhincConstants;

/**
 *
 * @author dunnek
 */
public class AdminDistributionAuditLogger {
    private Log log = null;

    /**
     * Constructor
     */
    public AdminDistributionAuditLogger() {
        log = createLogger();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     *
     * @param auditLogMsg
     * @param assertion
     * @return AcknowledgementType
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        log.debug("begin audit()");

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param direction
     * @return AcknowledgementType
     */
    public AcknowledgementType auditEntityAdminDist(RespondingGatewaySendAlertMessageType request, AssertionType assertion, String direction) {
        log.debug("begin auditEntityAdminDist()");
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAdminDist(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    /**
     * 
     * @param body
     * @param assertion
     * @param target
     * @param direction
     * @return AcknowledgementType
     */
    public AcknowledgementType auditNhincAdminDist(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target, String direction) {
        log.debug("begin auditNhincAdminDist()");
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();




        LogEventRequestType auditLogMsg = auditLogger.logNhincAdminDist(body, assertion, target, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            log.warn("Ack was null");
        }
        return ack;
    }

    /**
     * 
     * @param body
     * @param assertion
     * @param direction
     * @return AcknowledgementType
     */
    public AcknowledgementType auditNhinAdminDist(EDXLDistribution body, AssertionType assertion, String direction) {
        log.debug("begin auditNhinAdminDist()");
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        log.debug("body == null = " + body == null);
        log.debug("assertion == null = " + assertion == null);

        LogEventRequestType auditLogMsg = auditLogger.logAdminDist(body, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            log.warn("Ack was null");
        }
        return ack;
    }

    /**
     * 
     * @param body
     * @param assertion
     * @param direction
     * @return AcknowledgementType
     */
    public AcknowledgementType auditAdapterAdminDist(EDXLDistribution body, AssertionType assertion, String direction) {
        log.debug("begin auditAdapterAdminDist()");
        AcknowledgementType ack = null;
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        log.debug("body == null = " + body == null);
        log.debug("assertion == null = " + assertion == null);

        LogEventRequestType auditLogMsg = auditLogger.logAdminDist(body, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        } else {
            log.warn("Ack was null");
        }
        return ack;
    }
 
}
