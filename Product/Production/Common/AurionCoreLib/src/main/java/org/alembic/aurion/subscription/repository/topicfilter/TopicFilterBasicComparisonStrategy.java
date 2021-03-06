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
package org.alembic.aurion.subscription.repository.topicfilter;

import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractor;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;
import org.alembic.aurion.subscription.repository.topicfilter.ITopicComparison;
import org.alembic.aurion.subscription.repository.topicfilter.ITopicFilterStrategy;
import org.alembic.aurion.subscription.repository.topicfilter.TopicComparisonExactMatchStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class TopicFilterBasicComparisonStrategy implements ITopicFilterStrategy {

    ITopicComparison topicComparisonStrategy;

    public TopicFilterBasicComparisonStrategy(ITopicComparison topicComparisonStrategy) {
        if (topicComparisonStrategy == null) {
            throw new IllegalArgumentException("Must supply a valid ITopicComparison instance.");
        }
        this.topicComparisonStrategy = topicComparisonStrategy;
    }

    public boolean MeetsCriteria(Element subscriptionTopicExpression, Element notificationMessageElement) {
        boolean meetsCriteria = false;
        try {
            RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
            Element notificationMessageTopic = rootTopicExtractor.extractTopicElementFromNotificationMessageElement(notificationMessageElement);
            meetsCriteria = topicComparisonStrategy.MeetsCriteria(subscriptionTopicExpression, notificationMessageTopic);
        } catch (XPathExpressionException ex) {
            meetsCriteria = true;
        }
        return meetsCriteria;
    }
}
