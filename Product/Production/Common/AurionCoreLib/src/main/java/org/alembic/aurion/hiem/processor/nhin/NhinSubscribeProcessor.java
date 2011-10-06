/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.processor.nhin;

import org.alembic.aurion.hiem.processor.faults.ConfigurationException;
import org.alembic.aurion.hiem.adapter.subscribe.proxy.AdapterHiemSubscribeProxy;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.hiem.configuration.ConfigurationManager;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import org.alembic.aurion.hiem.dte.Namespaces;
import org.alembic.aurion.hiem.dte.marshallers.SubscribeResponseMarshaller;
import org.alembic.aurion.hiem.processor.common.HiemProcessorConstants;
import org.alembic.aurion.hiem.processor.common.PatientIdExtractor;
import org.alembic.aurion.hiem.processor.faults.SoapFaultFactory;
import org.alembic.aurion.hiem.processor.nhin.handler.SubscriptionHandler;
import org.alembic.aurion.hiem.processor.nhin.handler.SubscriptionHandlerFactory;
import org.alembic.aurion.hiem.adapter.subscribe.proxy.AdapterHiemSubscribeProxyObjectFactory;
import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractor;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 * Class used to process an NHIN Subscribe message
 * 
 * @author Neil Webb
 */
public class NhinSubscribeProcessor {

    private static Log log = LogFactory.getLog(NhinSubscribeProcessor.class);

    /**
     * Perform processing for an NHIN subscribe message.
     *
     * @param subscribe NHIN subscribe message
     * @param assertion Assertion information extracted from the SOAP header
     * @param rawSubscribeXml Raw subscribe message received in the SOAP message
     * @return Subscribe response message
     * @throws java.lang.Exception
     */
    public SubscribeResponse processNhinSubscribe(Element soapMessage, AssertionType assertion) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault {
        log.debug("In processNhinSubscribe");

        log.debug("extract subscribe from soapmessage");
        Element subscribe = XmlUtility.getSingleChildElement(soapMessage, Namespaces.WSNT, "Subscribe");
        SubscribeResponse subscribeResponse = null;

        String serviceMode;
        try {
            ConfigurationManager config = new ConfigurationManager();
            serviceMode = config.getSubscriptionServiceMode();
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getUnknownSubscriptionServiceMode("Configuration occurred - unable to determine service mode.");
        }

        log.debug("serviceMode=" + serviceMode);
        if (HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH.equals(serviceMode)) {
            log.debug("In passthrough mode");
            subscribeResponse = passthroughMode(subscribe, assertion);
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED.equals(serviceMode)) {
            log.debug("Subscriptions are not supported");
            throw new SoapFaultFactory().getSubscriptionsNotSupported();
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED.equalsIgnoreCase(serviceMode)) {
            log.debug("Subscriptions are supported. Processing subscribe message");
            subscribeResponse = nhinSubscribe(subscribe, assertion);
        } else {
            log.error("Unknown subscription service mode: " + serviceMode);
            throw new SoapFaultFactory().getUnknownSubscriptionServiceMode(serviceMode);
        }
        return subscribeResponse;
    }

    private TopicConfigurationEntry getTopicConfiguration(Element subscribeElement) throws TopicNotSupportedFault, InvalidTopicExpressionFault, ConfigurationException {
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();

        Element topic;
        try {
            log.debug("finding topic from message");
            topic = rootTopicExtractor.extractTopicExpressionElementFromSubscribeElement(subscribeElement);
            log.debug("complete with finding topic.  found=" + (topic != null));
        } catch (XPathExpressionException ex) {
            throw new SoapFaultFactory().getUnableToParseTopicExpressionFromSubscribeFault(ex);
        }

//        if (topic == null) {
//            log.debug("topic not found from message, so using reverse compatibility");
//            RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
//            topic = compat.buildReverseCompatTopicExpression(subscribeElement);
//            log.debug("complete with RootTopicExtractorReverseCompat.  found=" + (topic != null));
//        }

        TopicConfigurationEntry topicConfig;
        topicConfig = TopicConfigurationManager.getInstance().getTopicConfiguration(topic);

        if (topicConfig == null) {
            throw new SoapFaultFactory().getUnknownTopic(topic);
        }
        if (!topicConfig.isSupported()) {
            throw new SoapFaultFactory().getKnownTopicNotSupported(topic);
        }

        return topicConfig;
    }

    private SubscribeResponse nhinSubscribe(Element subscribe, AssertionType assertion) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault, ResourceUnknownFault {
        log.debug("Begin nhinSubscribe");
        SubscribeResponse response = null;

        TopicConfigurationEntry topicConfig;
        try {
            log.debug("determine topic configuration");
            topicConfig = getTopicConfiguration(subscribe);
            log.debug("getTopicConfiguration complete.  isnull=" + (topicConfig == null));

            if (topicConfig == null) {
                throw new SoapFaultFactory().getUnknownTopic(null);
            }
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getTopicConfigurationException(ex);
        }

        QualifiedSubjectIdentifierType patientIdentifier = new PatientIdExtractor().extractPatientIdentifier(subscribe, topicConfig);
        performPolicyCheck(subscribe, assertion, patientIdentifier);

        SubscriptionHandler subscriptionHandler;
        try {
            log.debug("creating subscription handler [SubscriptionHandlerFactory().getSubscriptionHandler();]");
            subscriptionHandler = new SubscriptionHandlerFactory().getSubscriptionHandler();
            log.debug("create subscription handler complete.  isnull=" + (subscriptionHandler == null));
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getConfigurationException(ex);
        }

        log.debug("Sending subscribe message to message handler");
        response = subscriptionHandler.handleSubscribe(subscribe);

        log.debug("End nhinSubscribe");
        return response;
    }

    private void performPolicyCheck(Element subscribe, AssertionType assertion, QualifiedSubjectIdentifierType patientIdentifier) {
        // TODO: Call policy check
    }

    private SubscribeResponse passthroughMode(Element subscribe, AssertionType assertion) throws SubscribeCreationFailedFault {
        log.info("initialize HIEM subscribe adapter proxy");
        AdapterHiemSubscribeProxyObjectFactory factory = new AdapterHiemSubscribeProxyObjectFactory();
        AdapterHiemSubscribeProxy proxy = factory.getAdapterHiemSubscribeProxy();

        SubscribeResponse subscribeResponse = null;
        log.info("invoke HIEM subscribe adapter proxy");
        try {
            subscribeResponse =  proxy.subscribe(subscribe, assertion);
        } catch (Exception ex) {
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        log.info("complete with invoke HIEM subscribe adapter proxy");

        return subscribeResponse;
    }
}
