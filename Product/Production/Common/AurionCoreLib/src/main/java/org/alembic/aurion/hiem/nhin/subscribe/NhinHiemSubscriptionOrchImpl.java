/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.subscribe;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.eventcommon.SubscribeEventType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import java.io.ByteArrayOutputStream;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import org.alembic.aurion.hiem.processor.nhin.NhinSubscribeProcessor;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;

/**
 *
 * @author jhoppesc
 */
public class NhinHiemSubscriptionOrchImpl {

    private static Log log = LogFactory.getLog(NhinHiemSubscriptionOrchImpl.class);

    public SubscribeResponse subscribe(Subscribe subscribeRequest, Element soapMessage, AssertionType assertion) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault  {
        log.debug("Entering NhinHiemSubscriptionOrchImpl.subscribe");

        NhinSubscribeProcessor subscribeProcessor = new NhinSubscribeProcessor();

        // Audit the input message
        auditInputMessage(subscribeRequest, assertion);

        SubscribeResponse response = null;
        if(checkPolicy(subscribeRequest, assertion))
        {
            response = subscribeProcessor.processNhinSubscribe(soapMessage, assertion);
        }
        else
        {
            SubscribeCreationFailedFaultType faultInfo = null;
            throw new SubscribeCreationFailedFault("Policy check failed", faultInfo);
        }

        // Audit the response message
        auditResponseMessage(response, assertion);

        log.debug("Exiting NhinHiemSubscriptionOrchImpl.subscribe");
        return response;
    }

    private void auditInputMessage(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscriptionOrchImpl.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion) {
        log.debug("In NhinHiemSubscriptionOrchImpl.auditResponseMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.hiemauditlog.SubscribeResponseMessageType message = new org.alembic.aurion.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Error loging subscription response: " + t.getMessage(), t);
        }
    }

    private boolean checkPolicy(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscriptionOrchImpl.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        org.alembic.aurion.common.eventcommon.SubscribeMessageType request = new org.alembic.aurion.common.eventcommon.SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemSubscriptionOrchImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

}
