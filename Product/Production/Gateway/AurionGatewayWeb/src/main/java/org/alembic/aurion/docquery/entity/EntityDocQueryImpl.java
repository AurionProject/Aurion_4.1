/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.service.WebServiceHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class EntityDocQueryImpl {

    private Log log = null;

    public EntityDocQueryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper() {
        return new WebServiceHelper();
    }

    protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }
    }

    AdhocQueryResponse respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;
        String interfaceName = getServiceNameFromContext(context);

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        setMessageID (assertion, context);

        try {
            if (request != null) {
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();

                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = (AdhocQueryResponse) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(), "respondingGatewayCrossGatewayQuery", adhocQueryRequest, targets, context);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    AdhocQueryResponse respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQueryRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;
        String interfaceName = getServiceNameFromContext(context);
        setMessageID (request.getAssertion(), context);
        try {
            if (request != null) {
                getSoapLogger().logRawAssertion(request.getAssertion());
                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                response = (AdhocQueryResponse) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(), "respondingGatewayCrossGatewayQuery", adhocQueryRequest, assertIn, targets, context);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    private EntityDocQueryOrchImpl createEntityDocQueryOrchImpl() {
        return new EntityDocQueryOrchImpl();
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
