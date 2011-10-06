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

package org.alembic.aurion.docsubmission.passthru.deferred.response;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.nhin.deferred.response.proxy.NhinDocSubmissionDeferredResponseProxy;
import org.alembic.aurion.docsubmission.nhin.deferred.response.proxy.NhinDocSubmissionDeferredResponseProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredResponseOrchImpl {
    private Log log = null;
    private XDRAuditLogger auditLogger = null;

    /**
     * 
     */
    public PassthruDocSubmissionDeferredResponseOrchImpl()
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
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        provideAndRegisterResponseRequest.setNhinTargetSystem(targetSystem);
        provideAndRegisterResponseRequest.setRegistryResponse(request);
        if (isNhinAudit) {
            logRequest(provideAndRegisterResponseRequest, assertion);
        }

        NhinDocSubmissionDeferredResponseProxy proxy = createNhinProxy();

        log.debug("Calling NHIN Proxy");
        response = proxy.provideAndRegisterDocumentSetBDeferredResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());
        if (isNhinAudit) {
            logResponse(response, assertion);
        }

        log.debug("End provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType, AssertionType)");
        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     */
    private void logRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request, AssertionType assertion) {
        log.debug("Begin logRequest");
        auditLogger.auditNhinXDRResponseRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    /**
     *
     * @param response
     * @param assertion
     */
    private void logResponse(XDRAcknowledgementType response, AssertionType assertion) {
        log.debug("Begin logResponse");
        auditLogger.auditAcknowledgement(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
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
     * @return NhinDocSubmissionDeferredResponseProxy
     */
    protected NhinDocSubmissionDeferredResponseProxy createNhinProxy()
    {
        NhinDocSubmissionDeferredResponseProxyObjectFactory factory = new NhinDocSubmissionDeferredResponseProxyObjectFactory();
        return factory.getNhinDocSubmissionDeferredResponseProxy();
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
