/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocRetrieveImpl.class);

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, null);
            NhincProxyDocRetrieveOrchImpl orchImpl = new NhincProxyDocRetrieveOrchImpl();
            String interfaceName = getServiceNameFromContext(context);
            AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
            PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion, body.getNhinTargetSystem());
            oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
            PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        }

        return response;
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveRequestType body, WebServiceContext context) {
        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, body.getAssertion());
            NhincProxyDocRetrieveOrchImpl orchImpl = new NhincProxyDocRetrieveOrchImpl();
            String interfaceName = getServiceNameFromContext(context);
            AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
            PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion, body.getNhinTargetSystem());
            oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
            PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        }
        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
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
}