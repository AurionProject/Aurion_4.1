/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.subscribe.proxy;

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
import org.alembic.aurion.hiem.dte.marshallers.SubscribeResponseMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.alembic.aurion.xmlCommon.XmlUtility;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotificationProducer;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeCreationFailedFaultType;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemSubscribeProxyWebServiceSecuredImpl implements NhinHiemSubscribeProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "NotificationProducerService";
    private static final String PORT_LOCAL_PART = "NotificationProducerPort";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinHiemSubscribeProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected NotificationProducer getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NotificationProducer port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NotificationProducer.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        Element responseElement = null;
        SubscribeResponse response = null;
        String url = null;

        log.debug("In NhinSubscribeWebserviceProxy.subscribe()");

        try {
            log.debug("Before target system URL look up.");
            url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);

            log.debug("After target system URL look up. URL for service: " + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME + " is: " + url);

            if (NullChecker.isNotNullish(url)) {
                NotificationProducer port = getPort(url, NhincConstants.SUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);

                WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
                Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);

                if (checkPolicy(subscribe, assertion)) {
                    auditInputMessage(subscribe, assertion);
                    response = (SubscribeResponse)oProxyHelper.invokePort(port, NotificationProducer.class, "subscribe", subscribe);
                    auditResponseMessage(response, assertion);
                } else {
                    SubscribeCreationFailedFaultType faultInfo = null;
                    throw new SubscribeCreationFailedFault("Policy check failed", faultInfo);
                }

                SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
                responseElement = responseMarshaller.marshal(response);
                log.debug(XmlUtility.serializeElementIgnoreFaults(responseElement));
            } else {
                log.error("The URL for service: " + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME + " is null");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        log.debug("Exit NhinSubscribeWebserviceProxy.subscribe()");
        return responseElement;
    }

    private boolean checkPolicy(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        org.alembic.aurion.common.eventcommon.SubscribeMessageType request = new org.alembic.aurion.common.eventcommon.SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemSubscribeWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private void auditInputMessage(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.auditInputMessage");
        AcknowledgementType ack = null;
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.auditResponseMessage");
        AcknowledgementType ack = null;
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.hiemauditlog.SubscribeResponseMessageType message = new org.alembic.aurion.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe response message: " + t.getMessage(), t);
        }
    }
}
