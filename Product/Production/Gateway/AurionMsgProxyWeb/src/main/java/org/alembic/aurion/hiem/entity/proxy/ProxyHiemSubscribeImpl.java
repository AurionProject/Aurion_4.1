/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestType;
import org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestSecuredType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.dte.marshallers.SubscribeResponseMarshaller;
import org.alembic.aurion.hiem.nhin.subscribe.proxy.NhinHiemSubscribeProxy;
import org.alembic.aurion.hiem.nhin.subscribe.proxy.NhinHiemSubscribeProxyObjectFactory;
import org.w3c.dom.Element;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.util.soap.SoapLogger;
import org.alembic.aurion.xmlCommon.XmlUtility;

/**
 *
 * @author Jon Hoppesch
 */
public class ProxyHiemSubscribeImpl {

    private static Log log = LogFactory.getLog(ProxyHiemSubscribeImpl.class);

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context) throws NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, TopicNotSupportedFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, InvalidMessageContentExpressionFault, ResourceUnknownFault {
        log.debug("Entering ProxyHiemSubscribeImpl.subscribe...");
        SubscribeResponse resp = null;

        AssertionType assertion = subscribeRequest.getAssertion();
        getSoapLogger().logRawAssertion(assertion);

        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(subscribeRequest);

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        Element responseElement = proxy.subscribe(subscribeElement, assertion, subscribeRequest.getNhinTargetSystem());

        SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
        resp = responseMarshaller.unmarshal(responseElement);

        log.debug("Exiting ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

    private AcknowledgementType audit(SubscribeRequestType subscribeRequest) {
        AcknowledgementType ack = null;
//        ConfigurationManager config = new ConfigurationManager();
//        if (config.isAuditEnabled()) {
//
//            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//
//            // Fix namespace issue
//            org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType();
//            message.setAssertion(subscribeRequest.getAssertion());
//            message.setSubscribe(subscribeRequest.getSubscribe());
//
//            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
//
//            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
//            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
//            ack = proxy.auditLog(auditLogMsg);
//        }

        return ack;
    }



    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequest, WebServiceContext context) throws NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, TopicNotSupportedFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, InvalidMessageContentExpressionFault, ResourceUnknownFault {
        log.debug("Entering Secured ProxyHiemSubscribeImpl.subscribe...");
        SubscribeResponse resp = null;

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        getSoapLogger().logRawAssertion(assertion);

        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(subscribeRequest);

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        Element responseElement = proxy.subscribe(subscribeElement, assertion, subscribeRequest.getNhinTargetSystem());

        SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
        resp = responseMarshaller.unmarshal(responseElement);

        log.debug("Response = " + XmlUtility.serializeElementIgnoreFaults(responseElement));
        
        log.debug("Exiting Secured ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

    private AcknowledgementType audit(SubscribeRequestSecuredType subscribeRequest) {
        AcknowledgementType ack = null;
//        ConfigurationManager config = new ConfigurationManager();
//        if (config.isAuditEnabled()) {
//
//            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
//
//            // Fix namespace issue
//            org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType();
//            message.setAssertion(subscribeRequest.getAssertion());
//            message.setSubscribe(subscribeRequest.getSubscribe());
//
//            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
//
//            AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
//            AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
//            ack = proxy.auditLog(auditLogMsg);
//        }

        return ack;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
