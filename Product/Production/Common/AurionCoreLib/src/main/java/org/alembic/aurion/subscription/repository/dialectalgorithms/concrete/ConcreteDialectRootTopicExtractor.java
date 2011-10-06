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
package org.alembic.aurion.subscription.repository.dialectalgorithms.concrete;

import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.subscription.repository.roottopicextractor.IRootTopicExtractionStrategy;
import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class ConcreteDialectRootTopicExtractor implements IRootTopicExtractionStrategy {

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        String rootTopic = null;
        String topicValue = XmlUtility.getNodeValue(topicExpression);

        String[] topicParts = topicValue.split("/");

        for (String topicPart : topicParts) {
            String cleanedTopicPart = RootTopicExtractorHelper.ReplaceNamespacePrefixesWithNamespaces(topicPart, topicExpression);
            if (NullChecker.isNullish(rootTopic)) {
                rootTopic = "";
            } else {
                rootTopic = rootTopic.concat("/");
            }
            rootTopic = rootTopic.concat(cleanedTopicPart);
        }

        return rootTopic;
    }
}
