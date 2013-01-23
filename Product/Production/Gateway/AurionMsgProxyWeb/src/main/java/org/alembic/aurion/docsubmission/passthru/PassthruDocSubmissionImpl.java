/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
public class PassthruDocSubmissionImpl {

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context) {
        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = extractAssertionFromContext(context, null);
        getSoapLogger().logRawAssertion(assertion);
        String interfaceName = getServiceNameFromContext(context);
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        RegistryResponseType oResponse = new PassthruDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        return oResponse;
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, body.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        String interfaceName = getServiceNameFromContext(context);
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        RegistryResponseType oResponse = new PassthruDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        return oResponse;
    }

    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
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

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
