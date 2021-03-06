/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.service;

import org.alembic.aurion.subscription.repository.data.SubscriptionCriteria;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecord;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecordList;
import org.alembic.aurion.subscription.repository.data.SubscriptionReference;
import org.alembic.aurion.subscription.repository.data.SubscriptionType;

/**
 * Interface for access to a subscription repository
 * 
 * @author Neil Webb
 */
public interface SubscriptionRepositoryService 
{
    /**
     * Store a subscription item in the repository.
     *  
     * @param subscriptionRecord Object to store
     * @return The subscription reference to the newly created subscription record
     */
    public SubscriptionReference storeSubscription(SubscriptionRecord subscriptionRecord);
    
    /**
     * Remove a subscription from the repository.
     * 
     * @param subscriptionRecord Subscription record to delete.
     */
    public void deleteSubscription(SubscriptionRecord subscriptionRecord);
    
    /**
     * Retrieve matching subscriptions matching the provided criteria. Both 
     * the subscribee and subscriber patients must match (may be null) and the
     * criterion values provided must all be found in the stored criterion.
     * 
     * @param criteria Subscription criteria to use in matching.
     * @param subscriptionType Subscription type
     * @return A list of subscription records matching the provided criteria.
     */
    public SubscriptionRecordList retrieveByCriteria(SubscriptionCriteria criteria, SubscriptionType subscriptionType);
    
    /**
     * Retrieve a subscription by subscription reference.
     * 
     * @param subscriptionReference Subscription reference to the stored subscription record.
     * @param subscriptionType Subscription type
     * @return Matching subscription record
     */
    public SubscriptionRecord retrieveBySubscriptionReference(SubscriptionReference subscriptionReference, SubscriptionType subscriptionType);
    
    /**
     * Retrieve all subscriptions that match the provided parent subscription reference.
     * 
     * @param parentSubscriptionReference Parent subscription reference.
     * @param subscriptionType Subscription type
     * @return Subscription records matching the provided parent subscription reference.
     */
    public SubscriptionRecordList retrieveByParentSubscriptionReference(SubscriptionReference parentSubscriptionReference, SubscriptionType subscriptionType);
    
}
