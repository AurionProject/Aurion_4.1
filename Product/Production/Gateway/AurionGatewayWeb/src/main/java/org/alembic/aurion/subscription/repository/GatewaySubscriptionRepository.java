/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentSubscriptionRepositoryService", portName = "NhincComponentSubscriptionRepositoryPort", endpointInterface = "org.alembic.aurion.nhinccomponentsubscriptionrepository.NhincComponentSubscriptionRepositoryPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentsubscriptionrepository", wsdlLocation = "WEB-INF/wsdl/GatewaySubscriptionRepository/NhincComponentSubscriptionRepository.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class GatewaySubscriptionRepository
{
    private static Log log = LogFactory.getLog(GatewaySubscriptionRepository.class);

    public org.alembic.aurion.common.subscription.SubscriptionReferenceType storeSubscription(org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItem)
    {
        org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReference = null;
        try
        {
            subscriptionReference = new SubscriptionRepositoryHelper().storeSubscription(subscriptionItem);
        }
        catch(Throwable t)
        {
            log.error(t.getMessage(), t);
            // Return empty reference until fault handling is implemented
            subscriptionReference = new org.alembic.aurion.common.subscription.SubscriptionReferenceType();
        }
        return subscriptionReference;
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType deleteSubscription(org.alembic.aurion.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage)
    {
        org.alembic.aurion.common.nhinccommon.AcknowledgementType ack = null;
        try
        {
            if(deleteSubscriptionMessage != null)
            {
                ack = new SubscriptionRepositoryHelper().deleteSubscription(deleteSubscriptionMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create an ack here until proper fault handling is established.
            ack = new org.alembic.aurion.common.nhinccommon.AcknowledgementType();
        }
        return ack;
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemsType retrieveByCriteria(org.alembic.aurion.common.subscription.SubscriptionCriteriaType subscriptionCriteria)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try
        {
            subscriptionItems = new SubscriptionRepositoryHelper().retrieveByCriteria(subscriptionCriteria);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new org.alembic.aurion.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(org.alembic.aurion.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceRequest)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItem = null;
        try
        {
            if(retrieveBySubscriptionReferenceRequest != null)
            {
                subscriptionItem = new SubscriptionRepositoryHelper().retrieveBySubscriptionReference(retrieveBySubscriptionReferenceRequest.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItem = new org.alembic.aurion.common.subscription.SubscriptionItemType();
        }
        return subscriptionItem;
    }

}
