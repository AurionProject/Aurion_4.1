/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentSubscriptionReferenceRepositoryService", portName = "NhincComponentSubscriptionReferenceRepositoryPortTypeBindingPort", endpointInterface = "org.alembic.aurion.nhinccomponentsubscriptionreferencerepository.NhincComponentSubscriptionReferenceRepositoryPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentsubscriptionreferencerepository", wsdlLocation = "WEB-INF/wsdl/GatewaySubscriptionReferenceRepository/NhincComponentSubscriptionReferenceRepository.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "GatewaySubscriptionRepositorySoapHeaderHandler.xml")
public class GatewaySubscriptionReferenceRepository
{
    private static Log log = LogFactory.getLog(GatewaySubscriptionReferenceRepository.class);

    public org.alembic.aurion.common.subscription.SubscriptionReferenceType storeSubscriptionReference(org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItem)
    {
        org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReference = null;
        try
        {
            subscriptionReference = new SubscriptionReferenceRepositoryHelper().storeSubscription(subscriptionItem);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Return empty reference until fault handling is implemented
            subscriptionReference = new org.alembic.aurion.common.subscription.SubscriptionReferenceType();
        }
        return subscriptionReference;
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType deleteSubscriptionReference(org.alembic.aurion.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage)
    {
        org.alembic.aurion.common.nhinccommon.AcknowledgementType ack = null;
        try
        {
            if(deleteSubscriptionMessage != null)
            {
                ack = new SubscriptionReferenceRepositoryHelper().deleteSubscription(deleteSubscriptionMessage.getSubscriptionReference());
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
            subscriptionItems = new SubscriptionReferenceRepositoryHelper().retrieveByCriteria(subscriptionCriteria);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new org.alembic.aurion.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(org.alembic.aurion.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceMessage)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItem = null;
        try
        {
            if(retrieveBySubscriptionReferenceMessage != null)
            {
                subscriptionItem = new SubscriptionReferenceRepositoryHelper().retrieveBySubscriptionReference(retrieveBySubscriptionReferenceMessage.getSubscriptionReference());
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

    public org.alembic.aurion.common.subscription.SubscriptionItemsType retrieveByParentSubscriptionReference(org.alembic.aurion.common.subscription.RetrieveByParentSubscriptionReferenceMessageType retrieveByParentSubscriptionReferenceMessage)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try
        {
            if(retrieveByParentSubscriptionReferenceMessage != null)
            {
                subscriptionItems = new SubscriptionReferenceRepositoryHelper().retrieveByParentSubscriptionReference(retrieveByParentSubscriptionReferenceMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new org.alembic.aurion.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

}
