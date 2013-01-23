/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.entity;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.UrlInfoType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.service.WebServiceHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class EntityDocSubmissionImpl {

    private Log log = null;

    public EntityDocSubmissionImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper() {
        return new WebServiceHelper();
    }

    RegistryResponseType provideAndRegisterDocumentSetBUnsecured(RespondingGatewayProvideAndRegisterDocumentSetRequestType request, WebServiceContext context) {
        log.info("Begin EntityDocSubmissionImpl.provideAndRegisterDocumentSetBUnsecured(RespondingGatewayProvideAndRegisterDocumentSetRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;
        String interfaceName = getServiceNameFromContext(context);
        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                getSoapLogger().logRawAssertion(assertIn);
                UrlInfoType urlInfo = request.getUrl();
                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = (RegistryResponseType) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(),
                        "provideAndRegisterDocumentSetB", msg, assertIn, targets, urlInfo, context);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() +
                        ".provideAndRegisterDocumentSetB).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() +
                    ".provideAndRegisterDocumentSetB).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        log.info("End EntityDocSubmissionImpl.provideAndRegisterDocumentSetBUnsecured with response: " + response);
        return response;
    }

    RegistryResponseType provideAndRegisterDocumentSetBSecured(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, WebServiceContext context) {
        log.info("Begin EntityDocSubmissionImpl.provideAndRegisterDocumentSetBSecured(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocSubmissionOrchImpl implOrch = createEntityDocSubmissionOrchImpl();
        RegistryResponseType response = null;
        String interfaceName = getServiceNameFromContext(context);
        try {
            if (request != null) {
                ProvideAndRegisterDocumentSetRequestType msg = request.getProvideAndRegisterDocumentSetRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                UrlInfoType urlInfo = request.getUrl();
                AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = (RegistryResponseType) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(),
                        "provideAndRegisterDocumentSetB", msg, targets, urlInfo, context);
                oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() +
                        ".provideAndRegisterDocumentSetB).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() +
                    ".provideAndRegisterDocumentSetB).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        log.info("End EntityDocSubmissionImpl.provideAndRegisterDocumentSetBSecured with response: " + response);

        return response;
    }

    private EntityDocSubmissionOrchImpl createEntityDocSubmissionOrchImpl() {
        return new EntityDocSubmissionOrchImpl();
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
