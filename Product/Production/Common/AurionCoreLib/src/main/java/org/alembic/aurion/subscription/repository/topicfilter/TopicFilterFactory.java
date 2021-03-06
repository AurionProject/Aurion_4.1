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

import org.alembic.aurion.subscription.repository.dialectalgorithms.full.FullDialectTopicFilterStrategy;
import org.alembic.aurion.subscription.repository.roottopicextractor.RootTopicExtractor;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;

/**
 *
 * @author rayj
 */
public class TopicFilterFactory {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TopicFilterFactory.class);

    public static ITopicFilterStrategy getTopicFilterStrategy(String dialect) throws SubscriptionRepositoryException {
        ITopicFilterStrategy topicFilterStategy = null;
        if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE)) {
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_SIMPLE + "'");
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE)) {
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_CONCRETE + "'");
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL)) {
            topicFilterStategy = new FullDialectTopicFilterStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_FULL + "'");
            topicFilterStategy = new FullDialectTopicFilterStrategy();
        } else {
            throw new SubscriptionRepositoryException("Unknown dialect + '" + dialect + "'");
        }
        return topicFilterStategy;
    }
}
