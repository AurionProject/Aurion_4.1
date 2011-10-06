/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeRequestType;
import org.alembic.aurion.entitysubscriptionmanagement.EntityNotificationProducerPortType;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidFilterFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityHiemSubscribeProxyWebServiceUnsecuredImpl implements EntityHiemSubscribeProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:entitysubscriptionmanagement";
    private static final String SERVICE_LOCAL_PART = "EntityNotificationProducer";
    private static final String PORT_LOCAL_PART = "EntityNotificationProducerPortSoap";
    private static final String WSDL_FILE = "EntitySubscriptionManagement.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:common:nhinccommonentity:SubscribeRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityHiemSubscribeProxyWebServiceUnsecuredImpl() {
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
    protected EntityNotificationProducerPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityNotificationProducerPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityNotificationProducerPortType.class);
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
    
    public SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, Element subscribeElement) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        log.debug("Begin EntitySubscribeProxyWebserviceUnsecuredImpl.subscribe");
        SubscribeResponse response = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME);
            EntityNotificationProducerPortType port = getPort(url, NhincConstants.SUBSCRIBE_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null)
            {
                SubscribeRequestType subscribeMsg = new SubscribeRequestType();
                subscribeMsg.setAssertion(assertion);
                subscribeMsg.setSubscribe(subscribeRequest);
                subscribeMsg.setNhinTargetCommunities(targets);

                response = (SubscribeResponse) oProxyHelper.invokePort(port, EntityNotificationProducerPortType.class, "subscribe", subscribeMsg);
            }
            else
            {
                log.error("EntityNotificationProducerPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End EntitySubscribeProxyWebserviceUnsecuredImpl.subscribe");
        return response;
    }

}
