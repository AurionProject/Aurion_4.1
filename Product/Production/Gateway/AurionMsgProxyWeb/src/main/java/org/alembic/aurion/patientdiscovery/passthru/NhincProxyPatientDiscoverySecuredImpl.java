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
package org.alembic.aurion.patientdiscovery.passthru;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredImpl {

    private static Log log = LogFactory.getLog(NhincProxyPatientDiscoverySecuredImpl.class);

    protected NhincPatientDiscoveryOrchImpl getNhincPatientDiscoveryProcessor() {
        return new NhincPatientDiscoveryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
        if (processor != null) {
            try {

                AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
                loadAssertion(assertion, context);
                String interfaceName = getServiceNameFromContext(context);
                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = processor.proxyPRPAIN201305UV(request, assertion);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } catch (Exception ex) {
                String message = "Error occurred calling NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV. Error: " +
                        ex.getMessage();
                log.error(message, ex);
                throw new RuntimeException(message, ex);
            }
        } else {
            log.warn("NhincPatientDiscoveryProcessor was null.");
        }

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        return response;
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
