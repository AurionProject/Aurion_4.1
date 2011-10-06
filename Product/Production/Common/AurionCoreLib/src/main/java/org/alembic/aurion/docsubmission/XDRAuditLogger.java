/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission;

import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class XDRAuditLogger {
    private static Log log = null;

    public XDRAuditLogger()
    {
        log= createLogger();
    }
    
    public AcknowledgementType auditNhinXDR(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        log.debug("begin auditNhinXDR()");
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            } else {
                log.error("auditLogMsg.getAuditMessage() is null");
            }
        } else {
            log.error("auditLogMsg is null");
        }

        log.debug("begin auditNhinXDR()");
        log.debug("Ack message = " + ack.getMessage());
        return ack;
    }
    
    public AcknowledgementType auditXDR(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction)
    {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }

        }
        return ack;
    }

    public AcknowledgementType auditEntityXDR(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRReq(request, assertion, direction);

        if (auditLogMsg != null) {
            ack = audit(auditLogMsg, assertion);
        }

        return ack;
    }

    public AcknowledgementType auditEntityXDRResponse(RegistryResponseType response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponse(response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditEntityXDRResponseRequest(org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityXDRResponseRequest(response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditNhinXDRResponse (RegistryResponseType Response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRResponse(Response, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditNhinXDRResponseRequest (org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponseRequest(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * This method logs the acknowledgement returned from XDR Request and Response services
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param action
     * @return
     */
    public AcknowledgementType auditAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action, String _interface) {

        createLogger().debug("Start auditAcknowledgement for " + _interface);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAcknowledgement(acknowledgement, assertion, direction, action, _interface);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }
    
    public AcknowledgementType auditEntityAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {

        createLogger().debug("Start auditAcknowledgement for " + action);
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logEntityAcknowledgement(acknowledgement, assertion, direction, action);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }

    public AcknowledgementType auditXDRAdapterReq(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        log.debug("begin auditXDRAdapterReq()");
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            } else {
                log.error("auditLogMsg.getAuditMessage() is null");
            }
        } else {
            log.error("auditLogMsg is null");
        }

        log.debug("begin auditXDRAdapterReq()");
        log.debug("Ack message = " + ack.getMessage());
        return ack;
    }

    public AcknowledgementType auditAdapterXDRResponse(RegistryResponseType Response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRResponse(Response, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        if (auditLogMsg != null) {
            if (auditLogMsg.getAuditMessage() != null) {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }
}