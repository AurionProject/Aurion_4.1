/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditrepository.nhinc;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.auditlog.LogEventSecureRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import org.alembic.aurion.util.soap.SoapLogger;
/**
 *
 * @author mflynn02
 */
public class AuditRepositoryUnsecuredImpl {
    private Log log = null;

    public AuditRepositoryUnsecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected AuditRepositoryOrchImpl getAuditRepositoryOrchImpl() {
        return new AuditRepositoryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }
    public AcknowledgementType logEvent(LogEventRequestType logEventRequest, WebServiceContext context) {
        log.info("Entering AuditRepositoryUnsecuredImpl.logAudit");
        AcknowledgementType response = null;

        if (logEventRequest == null) {
            log.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = logEventRequest.getAssertion();
                    getSoapLogger().logRawAssertion(assertion);
                    loadAssertion(assertion, context);

                    LogEventSecureRequestType secureRequest = new LogEventSecureRequestType();
                    secureRequest.setAuditMessage(logEventRequest.getAuditMessage());
                    secureRequest.setDirection(logEventRequest.getDirection());
                    secureRequest.setInterface(logEventRequest.getInterface());

                    response = processor.logAudit(secureRequest, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryImpl.logAudit. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryUnsecuredImpl.logAudit");
        return response;
    }
    public FindCommunitiesAndAuditEventsResponseType queryAuditEvents(FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest, WebServiceContext context) {
        log.info("Entering AuditRepositoryUnsecuredImpl.queryAuditEvents");
        FindCommunitiesAndAuditEventsResponseType response = null;

        if (queryAuditEventsRequest == null) {
            log.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = queryAuditEventsRequest.getAssertion();
                    getSoapLogger().logRawAssertion(assertion);
                    loadAssertion(assertion, context);

                    response = processor.findAudit(queryAuditEventsRequest.getFindAuditEvents(), assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryUnsecuredImpl.queryAuditEvents. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryUnsecuredImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryUnsecuredImpl.queryAuditEvents");
        return response;

    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
