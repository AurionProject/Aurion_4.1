/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.util;

import org.alembic.aurion.subscription.repository.service.HiemSubscriptionRepositoryService;

/**
 * Implementation class for HiemSubscriptionRepositoryUtilService
 * 
 * @author Neil Webb
 */
public class HiemSubscriptionRepositoryUtilServiceImpl
{
    public org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType getSubscriptionCount(org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountRequestType getSubscriptionCountRequest)
    {
        org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType response = new org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType();
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
        response.setSubscriptionCount(service.subscriptionCount());
        return response;
    }

    public org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType response = new org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType();
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
        service.emptyRepository();
        return response;
    }
}
