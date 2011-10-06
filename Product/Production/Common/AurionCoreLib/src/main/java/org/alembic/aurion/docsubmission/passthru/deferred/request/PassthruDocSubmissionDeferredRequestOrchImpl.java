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

package org.alembic.aurion.docsubmission.passthru.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.nhin.deferred.request.proxy.NhinDocSubmissionDeferredRequestProxy;
import org.alembic.aurion.docsubmission.nhin.deferred.request.proxy.NhinDocSubmissionDeferredRequestProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredRequestOrchImpl {
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    /**
     * 
     */
    public PassthruDocSubmissionDeferredRequestOrchImpl()
    {
        log = createLogger();
        auditLogger = createAuditLogger();
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param targetSystem
     * @return XDRAcknowledgementType
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        provideAndRegisterRequestRequest.setNhinTargetSystem(targetSystem);
        provideAndRegisterRequestRequest.setProvideAndRegisterDocumentSetRequest(request);
        if (isNhinAudit) {
            logRequest(provideAndRegisterRequestRequest, assertion);
        }

        NhinDocSubmissionDeferredRequestProxy proxy = createNhinProxy();

        log.debug("Calling NHIN proxy");
        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());
        if (isNhinAudit) {
            logResponse(response, assertion);
        }

        log.debug("End provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType, AssertionType)");
        return response;
    }

    /**
     * 
     * @param request
     * @param assertion
     */
    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        log.debug("Begin logRequest");
        auditLogger.auditXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    /**
     * 
     * @param response
     * @param assertion
     */
    private void logResponse(XDRAcknowledgementType response, AssertionType assertion) {
        log.debug("Begin logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("End logResponse");
    }

    /**
     *
     * @return XDRAuditLogger
     */
    protected XDRAuditLogger createAuditLogger()
    {
        return new XDRAuditLogger();
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * 
     * @return NhinDocSubmissionDeferredRequestProxy
     */
    protected NhinDocSubmissionDeferredRequestProxy createNhinProxy()
    {
        NhinDocSubmissionDeferredRequestProxyObjectFactory factory = new NhinDocSubmissionDeferredRequestProxyObjectFactory();
        return factory.getNhinDocSubmissionDeferredRequestProxy();
    }

    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
