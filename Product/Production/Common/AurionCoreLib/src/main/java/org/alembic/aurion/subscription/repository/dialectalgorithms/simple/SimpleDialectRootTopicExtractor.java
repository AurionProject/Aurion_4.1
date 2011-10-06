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

package org.alembic.aurion.subscription.repository.dialectalgorithms.simple;

import org.alembic.aurion.subscription.repository.roottopicextractor.IRootTopicExtractionStrategy;
import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class SimpleDialectRootTopicExtractor   implements IRootTopicExtractionStrategy  {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SimpleDialectRootTopicExtractor.class);

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        log.debug("begin SimpleDialectRootTopicExtractor.extractRootTopicFromTopicExpressionNode topicExpression='" + XmlUtility.serializeNodeIgnoreFaults(topicExpression) + "'");
        String rootTopic = null;
        String topicValue = XmlUtility.getNodeValue(topicExpression);
        topicValue = RootTopicExtractorHelper.ReplaceNamespacePrefixesWithNamespaces(topicValue, topicExpression);
        rootTopic = topicValue;
        return rootTopic;
    }




}
