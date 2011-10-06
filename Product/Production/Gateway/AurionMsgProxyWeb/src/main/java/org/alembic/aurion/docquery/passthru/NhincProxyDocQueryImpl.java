/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.service.WebServiceHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocQueryImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocQueryImpl.class);

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());
        String interfaceName = getServiceNameFromContext(context);
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        AdhocQueryResponse oResponse = new PassthruDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), body.getAssertion(), body.getNhinTargetSystem());
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        return oResponse;
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType body, WebServiceContext context) {
        WebServiceHelper oHelper = new WebServiceHelper();
        AssertionType assertion = getAssertion(context, null);
        String interfaceName = getServiceNameFromContext(context);
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        AdhocQueryResponse response = new PassthruDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), assertion, body.getNhinTargetSystem());
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
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
