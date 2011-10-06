/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository;

import org.alembic.aurion.subscription.repository.data.SubscriptionType;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecord;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecordList;
import org.alembic.aurion.subscription.repository.data.SubscriptionReference;

/**
 * Helper class for subscription reference repository operations
 * 
 * @author Neil Webb
 */
public class SubscriptionReferenceRepositoryHelper extends BaseSubscriptionRepositoryHelper
{
    public SubscriptionReferenceRepositoryHelper() throws SubscriptionRepositoryException
    {
        super();
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemsType retrieveByParentSubscriptionReference(org.alembic.aurion.common.subscription.SubscriptionReferenceType parentSubscriptionReferenceType)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemsType subscriptionItemsType = new org.alembic.aurion.common.subscription.SubscriptionItemsType();

        // Transform to subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(parentSubscriptionReferenceType);
        
        // Retrieve the subscription item
        SubscriptionRecordList subscriptionRecords = subscriptionRepositoryService.retrieveByParentSubscriptionReference(subscriptionReference, getSubscriptionType());
        
        // Transform to subscription item type
        if(subscriptionRecords != null)
        {
            for(SubscriptionRecord record : subscriptionRecords)
            {
                if((record != null) && (record.getSubscription() != null))
                {
                    org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
                    if(subscriptionItemType != null)
                    {
                        subscriptionItemsType.getSubscriptionItem().add(subscriptionItemType);
                    }
                }
            }
        }
        return subscriptionItemsType;
    }

    @Override
    protected SubscriptionType getSubscriptionType()
    {
        return SubscriptionType.SUBSCRIPTION_REFERENCE;
    }
}
