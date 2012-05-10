/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author shawc
 */
public class EntityPatientDiscoverySecuredImpl {

    private Log log = null;

    public EntityPatientDiscoverySecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor() {
        return new EntityPatientDiscoveryOrchImpl();
    }

    protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        log.debug("Entering EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if (request == null) {
            log.error("The incomming request was null.");
        } else if (context == null) {
            log.error("The incomming WebServiceContext parameter was null.");
            return null;
        } else {
            AssertionType assertion = extractAssertion(context);
            setMessageID (assertion, context);

            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if (processor != null) {
                String interfaceName = getServiceNameFromContext(context);
                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = processor.respondingGatewayPRPAIN201305UV02(request, assertion);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } else {
                log.error("The EntityPatientDiscoveryProcessor was null.");
            }
        }

        log.debug("Exiting EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        return response;
    }

    protected AssertionType extractAssertion(WebServiceContext context) {
        return SamlTokenExtractor.GetAssertion(context);
    }

    /**
     * This method returns the interface name from WebServiceContext
     * @param context
     * @return String
     */
    private String getServiceNameFromContext(WebServiceContext context) {
        String interfaceName = null;
        if (null != context && null != context.getMessageContext() && null != context.getMessageContext().get(MessageContext.WSDL_SERVICE)) {
            QName qName = (QName) context.getMessageContext().get(MessageContext.WSDL_SERVICE);
            interfaceName = qName.getLocalPart();
        }
        return interfaceName;
    }
}
