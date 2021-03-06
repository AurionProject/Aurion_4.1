/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditrepository.nhinc;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.auditlog.LogEventSecureRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.util.soap.SoapLogger;


public class AuditRepositorySecuredImpl {

    private Log log = null;

    public AuditRepositorySecuredImpl() {
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

    public AcknowledgementType logAudit(LogEventSecureRequestType mess, WebServiceContext context) {
        log.info("Entering AuditRepositoryImpl.logAudit");
        AcknowledgementType response = null;

        if (mess == null) {
            log.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
                    getSoapLogger().logRawAssertion(assertion);
                    loadAssertion(assertion, context);

                    response = processor.logAudit(mess, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryImpl.logAudit. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryOrchImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryImpl.logAudit");
        return response;
    }

    public FindCommunitiesAndAuditEventsResponseType findAudit(FindAuditEventsType query, WebServiceContext context) {
        log.info("Entering AuditRepositoryImpl.findAudit");
        FindCommunitiesAndAuditEventsResponseType response = null;

        if (query == null) {
            log.warn("request was null.");
        } else {
            AuditRepositoryOrchImpl processor = getAuditRepositoryOrchImpl();
            if (processor != null) {
                try {
                    AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
                    getSoapLogger().logRawAssertion(assertion);
                    loadAssertion(assertion, context);

                    response = processor.findAudit(query, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling AuditRepositoryImpl.findAudit. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("AuditRepositoryOrchImpl was null.");
            }
        }

        log.info("Exiting AuditRepositoryImpl.findAudit");
        return response;

    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
