/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.notify;

import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.eventcommon.NotifyEventType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.hiem.processor.nhin.NhinNotifyProcessor;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 *
 * @author jhoppesc
 */
public class NhinHiemNotifyOrchImpl {

    private static Log log = LogFactory.getLog(NhinHiemNotifyOrchImpl.class);

    public void notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("Entering NhinHiemNotifyOrchImpl.notify");
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        SoapUtil contextHelper = new SoapUtil();
        Element soapMessage = contextHelper.extractSoapMessageElement(context, NhincConstants.HIEM_NOTIFY_SOAP_HDR_ATTR_TAG);
        
        try {
            //String rawSoapMessage = extractSoapMessage(context, "notifySoapMessage");
            NhinNotifyProcessor notifyProcessor = new NhinNotifyProcessor();
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            if(isNhinAudit)
            auditInputMessage(notifyRequest, assertion);
            if(checkPolicy(notifyRequest, assertion))
            {
                notifyProcessor.processNhinNotify(soapMessage, assertion);
            }
            else
            {
                log.error("Failed policy check on notify message");
            }
        } catch (Throwable t) {
            log.debug("Exception encountered processing a notify message: " + t.getMessage(), t);
            // TODO: Add specific catch statements and throw the appropriate fault

        }

        log.debug("Exiting NhinHiemNotifyOrchImpl.notify");
    }

    private void auditInputMessage(Notify notifyRequest, AssertionType assertion) {
        log.debug("In NhinHiemNotifyOrchImpl.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.nhinccommoninternalorch.NotifyRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notifyRequest);

            LogEventRequestType auditLogMsg = auditLogger.logNotifyRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Failed to log notify message: " + t.getMessage(), t);
        }
    }

    private boolean checkPolicy(Notify notifyRequest, AssertionType assertion) {
        log.debug("In NhinHiemNotifyOrchImpl.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        org.alembic.aurion.common.eventcommon.NotifyMessageType request = new org.alembic.aurion.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notifyRequest);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemNotifyOrchImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }
    
    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private static boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
