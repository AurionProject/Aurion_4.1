/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.notify.proxy;

import com.sun.xml.ws.Closeable;
import com.sun.xml.ws.developer.WSBindingProvider;
import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;
import org.w3c.dom.Element;
import org.alembic.aurion.common.eventcommon.NotifyEventType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.policyengine.PolicyEngineChecker;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxy;
import org.alembic.aurion.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.alembic.aurion.xmlCommon.XmlUtility;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.alembic.aurion.hiem.dte.SoapUtil;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemNotifyProxyWebServiceSecuredImpl implements NhinHiemNotifyProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "http://docs.oasis-open.org/wsn/bw-2";
    private static final String SERVICE_LOCAL_PART = "NotificationConsumerService";
    private static final String PORT_LOCAL_PART = "NotificationConsumerPort";
    private static final String WSDL_FILE = "NhinSubscription.wsdl";
    private static final String WS_ADDRESSING_ACTION = "http://docs.oasis-open.org/wsn/bw-2:Notify";
    private WebServiceProxyHelper oProxyHelper = null;

    public NhinHiemNotifyProxyWebServiceSecuredImpl() {
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
    protected NotificationConsumer getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        NotificationConsumer port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NotificationConsumer.class);
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

   public void notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,AssertionType assertion, NhinTargetSystemType target) {
        String url = null;

        log.debug("Notify element received in NhinHiemNotifyWebServiceProxy: " + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        try
        {
            log.debug("Before target system URL look up.");
            url = oProxyHelper.getUrlFromTargetSystem(target, NhincConstants.HIEM_NOTIFY_SERVICE_NAME);

            log.debug("After target system URL look up. URL for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME + " is: " + url);

            if (NullChecker.isNotNullish(url)) {

                log.debug("unmarshaling notify message");
                WsntSubscribeMarshaller notifyMarshaller = new WsntSubscribeMarshaller();
                Notify notify = notifyMarshaller.unmarshalNotifyRequest(notifyElement);

//                Element reMarshalled = notifyMarshaller.marshalNotifyRequest(notify);
//                log.debug("REMARSHALLED: " + XmlUtility.serializeElementIgnoreFaults(reMarshalled));

                // Policy check
                log.debug("Calling checkPolicy");
                if(checkPolicy(notify, assertion))
                {

                    NotificationConsumer port = getPort(url, NhincConstants.NOTIFY_ACTION, WS_ADDRESSING_ACTION, assertion);

                    log.debug("attaching reference parameter headers");
                    SoapUtil soapUtil = new SoapUtil();
                    soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                    auditInputMessage(notify, assertion);

                    try {
                        log.debug("Calling notification consumer port in NhinHiemWebServiceProxy.");
                        oProxyHelper.invokePort(port, NotificationConsumer.class, "notify", notify);
                    } catch (Exception ex) {
                        log.error("Error occurred while trying to invoke notify", ex);
                    }

                    ((Closeable)port).close();
                }
                else
                {
                    log.error("Failed policy check on send NHIN notify message");
                }
            } else {
                log.error("The URL for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME + " is null");
            }
        }
        catch(Throwable t)
        {
            // TODO: Figure out what to do with the exception
            log.error("Error sending notify to remote gateway: " + t.getMessage(), t);
        }

    }

    private boolean checkPolicy(Notify notify, AssertionType assertion) {
        log.debug("In NhinHiemNotifyWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        org.alembic.aurion.common.eventcommon.NotifyMessageType request = new org.alembic.aurion.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notify);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();

        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemNotifyWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private void auditInputMessage(Notify notify, AssertionType assertion) {
        log.debug("In NhinHiemNotifyWebServiceProxy.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            org.alembic.aurion.common.nhinccommoninternalorch.NotifyRequestType message = new org.alembic.aurion.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notify);

            LogEventRequestType auditLogMsg = auditLogger.logNotifyRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

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

}
