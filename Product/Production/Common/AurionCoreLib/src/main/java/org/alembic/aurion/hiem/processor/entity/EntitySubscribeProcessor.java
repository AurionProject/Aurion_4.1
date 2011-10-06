/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.processor.entity;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.alembic.aurion.hiem.processor.faults.ConfigurationException;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import org.alembic.aurion.hiem.processor.entity.handler.EntitySubscribeHandler;
import org.alembic.aurion.hiem.processor.entity.handler.EntitySubscribeHandlerFactory;
import org.alembic.aurion.hiem.processor.common.PatientIdExtractor;
import org.alembic.aurion.hiem.processor.faults.SoapFaultFactory;
import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractor;
import org.alembic.aurion.xmlCommon.XmlUtility;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;

/**
 *
 *
 * @author Neil Webb
 */
public class EntitySubscribeProcessor
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntitySubscribeProcessor.class);

    public SubscribeResponse processSubscribe(Subscribe subscribe, Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault, ResourceUnknownFault
    {
        SubscribeResponse response = null;

        TopicConfigurationEntry topicConfig;
        try
        {
            log.debug("determine topic configuration");
            log.debug(XmlUtility.serializeElementIgnoreFaults(subscribeElement));
            topicConfig = getTopicConfiguration(subscribeElement);
            log.debug("getTopicConfiguration complete.  isnull=" + (topicConfig == null));

            if (topicConfig == null) {
                throw new SoapFaultFactory().getUnknownTopic(null);
            }
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getTopicConfigurationException(ex);
        }

        QualifiedSubjectIdentifierType patientIdentifier = new PatientIdExtractor().extractPatientIdentifier(subscribeElement, topicConfig);

        EntitySubscribeHandler subscribeHandler = new EntitySubscribeHandlerFactory().getEntitySubscribeHandler(topicConfig, patientIdentifier);
        response = subscribeHandler.handleSubscribe(topicConfig, subscribe, subscribeElement, assertion, targetCommunitites);

        return response;
    }

    private TopicConfigurationEntry getTopicConfiguration(Element subscribeElement) throws TopicNotSupportedFault, InvalidTopicExpressionFault, ConfigurationException {
        TopicConfigurationEntry topicConfig = null;

        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();

        Element topicElement;
        try
        {
            log.debug("finding topic from message");
            topicElement = rootTopicExtractor.extractTopicExpressionElementFromSubscribeElement(subscribeElement);
            log.debug("complete with finding topic.  found=" + (topicElement != null));
        }
        catch (XPathExpressionException ex)
        {
            throw new SoapFaultFactory().getUnableToParseTopicExpressionFromSubscribeFault(ex);
        }

        if(topicElement != null)
        {
            topicConfig = TopicConfigurationManager.getInstance().getTopicConfiguration(topicElement);
            if((topicConfig != null) && !topicConfig.isSupported())
            {
                throw new SoapFaultFactory().getKnownTopicNotSupported(topicElement);
            }
        }

        return topicConfig;
    }
}
