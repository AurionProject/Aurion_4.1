/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository;

import org.alembic.aurion.subscription.repository.data.SubscriptionType;

/**
 * Helper class for subscrption repository operations
 * 
 * @author Neil Webb
 */
public class SubscriptionRepositoryHelper extends BaseSubscriptionRepositoryHelper
{
    public SubscriptionRepositoryHelper() throws SubscriptionRepositoryException
    {
        super();
    }

    @Override
    protected SubscriptionType getSubscriptionType()
    {
        return SubscriptionType.SUBSCRIPTION;
    }
}
