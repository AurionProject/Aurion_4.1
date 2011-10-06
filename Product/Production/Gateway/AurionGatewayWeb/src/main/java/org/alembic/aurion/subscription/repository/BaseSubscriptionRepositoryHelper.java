/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.subscription.KeyValuePairListType;
import org.alembic.aurion.common.subscription.KeyValuePairType;
import org.alembic.aurion.common.subscription.ReferenceParameterType;
import org.alembic.aurion.common.subscription.ReferenceParametersType;
import org.alembic.aurion.common.subscription.SubscriptionItemsType;
import org.alembic.aurion.common.subscription.SubscriptionPolicyType;
import org.alembic.aurion.common.subscription.TopicExpressionType;
import org.alembic.aurion.subscription.repository.data.Community;
import org.alembic.aurion.subscription.repository.data.Criterion;
import org.alembic.aurion.subscription.repository.data.Patient;
import org.alembic.aurion.subscription.repository.data.ReferenceParameter;
import org.alembic.aurion.subscription.repository.data.SubscriptionCriteria;
import org.alembic.aurion.subscription.repository.data.SubscriptionItem;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecord;
import org.alembic.aurion.subscription.repository.data.SubscriptionReference;
import org.alembic.aurion.subscription.repository.data.SubscriptionParticipant;
import org.alembic.aurion.subscription.repository.data.SubscriptionPolicy;
import org.alembic.aurion.subscription.repository.data.SubscriptionPolicyItem;
import org.alembic.aurion.subscription.repository.data.SubscriptionRecordList;
import org.alembic.aurion.subscription.repository.data.SubscriptionType;
import org.alembic.aurion.subscription.repository.data.TopicExpression;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryFactory;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for subscription repository helpers
 * 
 * @author Neil Webb
 */
public abstract class BaseSubscriptionRepositoryHelper
{
    private static Log log = LogFactory.getLog(BaseSubscriptionRepositoryHelper.class);
    protected SubscriptionRepositoryService subscriptionRepositoryService = null;
    protected org.alembic.aurion.common.subscription.ObjectFactory subscriptionRepositoryObjFact = null;
    public BaseSubscriptionRepositoryHelper() throws SubscriptionRepositoryException
    {
        subscriptionRepositoryObjFact = new org.alembic.aurion.common.subscription.ObjectFactory();
        subscriptionRepositoryService = new SubscriptionRepositoryFactory().getSubscriptionRepositoryService();
    }

    public org.alembic.aurion.common.subscription.SubscriptionReferenceType storeSubscription(org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItem)
    {
        // Create subscription record
        SubscriptionRecord record = loadSubscriptionRecord(subscriptionItem);

        // Store the record
        SubscriptionReference subscriptionReference = subscriptionRepositoryService.storeSubscription(record);

        // Transform to subscription reference type
        org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReferenceType = loadSubscriptionReferenceType(subscriptionReference);

        return subscriptionReferenceType;
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType deleteSubscription(org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        // Create the subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(subscriptionReferenceType);

        // Retrieve the subscription record
        SubscriptionRecord record = subscriptionRepositoryService.retrieveBySubscriptionReference(subscriptionReference, getSubscriptionType());
        if (record != null)
        {
            // Delete the subscription
            subscriptionRepositoryService.deleteSubscription(record);
        }

        // Create the ack
        org.alembic.aurion.common.nhinccommon.AcknowledgementType ack = new AcknowledgementType();

        return ack;
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemsType retrieveByCriteria(org.alembic.aurion.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemsType subscriptionItemsType = new SubscriptionItemsType();
        if (subscriptionCriteriaType != null)
        {
            // Transform criteria
            SubscriptionCriteria subscriptionCriteria = loadSubscriptionCriteria(subscriptionCriteriaType);

            // Retrieve by criteria
            SubscriptionRecordList subscriptions = subscriptionRepositoryService.retrieveByCriteria(subscriptionCriteria, getSubscriptionType());

            // Transform results
            if (subscriptions != null)
            {
                for (SubscriptionRecord record : subscriptions)
                {
                    if ((record != null) && record.getSubscription() != null)
                    {
                        org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
                        if (subscriptionItemType != null)
                        {
                            subscriptionItemsType.getSubscriptionItem().add(subscriptionItemType);
                        }
                    }
                }
            }
        }
        return subscriptionItemsType;
    }

    public org.alembic.aurion.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType = null;

        // Transform to subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(subscriptionReferenceType);

        if(subscriptionReference != null)
        {
            if(log.isDebugEnabled())
            {
                log.debug("#### Retrieving subscription item by subscription reference.");
                log.debug("Subscription manager endpoint address: " + subscriptionReference.getSubscriptionManagerEndpointAddress());
                if(subscriptionReference.getReferenceParameters() != null)
                {
                    for(ReferenceParameter refParam : subscriptionReference.getReferenceParameters())
                    {
                        log.debug("Ref param namespace: " + refParam.getNamespace());
                        log.debug("Ref param namespace prefix: " + refParam.getNamespacePrefix());
                        log.debug("Ref param element name: " + refParam.getElementName());
                        log.debug("Ref param value: " + refParam.getValue());
                    }
                    
                }
            }
            
        }
        // Retrieve the subscription item
        SubscriptionRecord record = subscriptionRepositoryService.retrieveBySubscriptionReference(subscriptionReference, getSubscriptionType());

        // Transform to subscription item type
        if ((record != null) && (record.getSubscription() != null))
        {
            subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
            log.debug("Subscription item retrieved");
        }
        else
        {
            // Return an empty subscription item until fault handling is added
            subscriptionItemType = new org.alembic.aurion.common.subscription.SubscriptionItemType();
            log.debug("No subscription item was retrieved");
        }
        return subscriptionItemType;
    }

    protected SubscriptionRecord loadSubscriptionRecord(org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType)
    {
        SubscriptionRecord record = new SubscriptionRecord();
        record.setType(getSubscriptionType());
        record.setSubscription(loadSubscriptionItem(subscriptionItemType));
        return record;
    }

    protected SubscriptionItem loadSubscriptionItem(org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType)
    {
        SubscriptionItem subscriptionItem = null;
        if (subscriptionItemType != null)
        {
            subscriptionItem = new SubscriptionItem();
            subscriptionItem.setSubscriber(loadSubscriber(subscriptionItemType.getSubscriber()));
            subscriptionItem.setSubscribee(loadSubscribee(subscriptionItemType.getSubscribee()));
            subscriptionItem.setSubscriptionCriteria(loadSubscriptionCriteria(subscriptionItemType.getSubscriptionCriteria()));
            subscriptionItem.setSubscriptionReference(loadSubscriptionReference(subscriptionItemType.getSubscriptionReference()));
            subscriptionItem.setParentSubscriptionReference(loadSubscriptionReference(subscriptionItemType.getParentSubscriptionReference()));
        }
        return subscriptionItem;
    }

    protected SubscriptionParticipant loadSubscriber(org.alembic.aurion.common.subscription.SubscriberType subscriberType)
    {
        SubscriptionParticipant subscribee = null;
        if (subscriberType != null)
        {
            subscribee = new SubscriptionParticipant();
            subscribee.setNotificationEndpointAddress(subscriberType.getNotificationConsumerEndpointAddress());
            subscribee.setCommunity(loadCommunity(subscriberType.getCommunity()));
            subscribee.setUserAddress(subscriberType.getUserAddress());
        }
        return subscribee;
    }

    protected SubscriptionParticipant loadSubscribee(org.alembic.aurion.common.subscription.SubscribeeType subscribeeType)
    {
        SubscriptionParticipant subscribee = null;
        if (subscribeeType != null)
        {
            subscribee = new SubscriptionParticipant();
            subscribee.setNotificationEndpointAddress(subscribeeType.getNotificationProducerEndpointAddress());
            subscribee.setCommunity(loadCommunity(subscribeeType.getCommunity()));
            subscribee.setUserAddress(subscribeeType.getUserAddress());
        }
        return subscribee;
    }

    protected SubscriptionCriteria loadSubscriptionCriteria(org.alembic.aurion.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType)
    {
        SubscriptionCriteria subscriptionCriteria = null;
        if (subscriptionCriteriaType != null)
        {
            subscriptionCriteria = new SubscriptionCriteria();
            subscriptionCriteria.setSubscriberPatient(loadPatient(subscriptionCriteriaType.getSubscriberPatient()));
            subscriptionCriteria.setSubscribeePatient(loadPatient(subscriptionCriteriaType.getSubscribeePatient()));
            subscriptionCriteria.setCriteria(loadCriteria(subscriptionCriteriaType.getCriteria()));
            subscriptionCriteria.setTopicExpression(loadTopicExpression(subscriptionCriteriaType.getTopicExpression()));
            subscriptionCriteria.setSubscriptionPolicy(loadSubscriptionPolicy(subscriptionCriteriaType.getSubscriptionPolicy()));
        }
        return subscriptionCriteria;
    }
    
    protected TopicExpression loadTopicExpression(TopicExpressionType topicExpressionType)
    {
        TopicExpression topicExpression = null;
        if(topicExpressionType != null)
        {
            topicExpression = new TopicExpression();
            topicExpression.setDialect(topicExpressionType.getDialect());
            topicExpression.setTopicExpressionValue(topicExpressionType.getValue());
        }
        return topicExpression;
    }
    
    protected SubscriptionPolicy loadSubscriptionPolicy(SubscriptionPolicyType subscriptionPolicyType)
    {
        SubscriptionPolicy subscriptionPolicy = null;
        if((subscriptionPolicyType != null) && (subscriptionPolicyType.getGenericPolicyItems() != null) && (subscriptionPolicyType.getGenericPolicyItems().getKeyValuePair() != null))
        {
            subscriptionPolicy = new SubscriptionPolicy();
            for(KeyValuePairType keyValPairType : subscriptionPolicyType.getGenericPolicyItems().getKeyValuePair())
            {
                SubscriptionPolicyItem policyItem = new SubscriptionPolicyItem();
                policyItem.setKey(keyValPairType.getKey());
                policyItem.setValue(keyValPairType.getValue());
                subscriptionPolicy.getPolicyItems().add(policyItem);
            }
            
        }
        return subscriptionPolicy;
    }

    protected Patient loadPatient(org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType qualSubjectId)
    {
        Patient patient = null;
        if (qualSubjectId != null)
        {
            patient = new Patient();
            patient.setPatientId(qualSubjectId.getSubjectIdentifier());
            if(qualSubjectId.getAssigningAuthorityIdentifier() != null)
            {
                patient.setAssigningAuthority(loadAssigningAuthority(qualSubjectId.getAssigningAuthorityIdentifier()));
            }
        }
        return patient;
    }

    protected Community loadAssigningAuthority(String assigningAuthorityId)
    {
        Community assigningAuthority = null;
        if (assigningAuthorityId != null)
        {
            assigningAuthority = new Community();
            assigningAuthority.setCommunityId(assigningAuthorityId);
        }
        return assigningAuthority;
    }

    protected List<Criterion> loadCriteria(org.alembic.aurion.common.subscription.CriteriaType criteriaType)
    {
        List<Criterion> criteria = null;
        if (criteriaType != null)
        {
            criteria = new ArrayList<Criterion>();
            List<org.alembic.aurion.common.subscription.CriterionType> criterionTypeList = criteriaType.getCriterion();
            if (criterionTypeList != null)
            {
                for (org.alembic.aurion.common.subscription.CriterionType criterionType : criterionTypeList)
                {
                    Criterion criterion = new Criterion();
                    criterion.setKey(criterionType.getKey());
                    criterion.setValue(criterionType.getValue());
                    criteria.add(criterion);
                }
            }
        }
        return criteria;
    }

    protected SubscriptionReference loadSubscriptionReference(org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        SubscriptionReference subscriptionReference = null;
        if (subscriptionReferenceType != null)
        {
            subscriptionReference = new SubscriptionReference();
            subscriptionReference.setSubscriptionManagerEndpointAddress(subscriptionReferenceType.getSubscriptionManagerEndpointAddress());
            if ((subscriptionReferenceType.getReferenceParameters() != null) && (subscriptionReferenceType.getReferenceParameters().getReferenceParameter() != null))
            {
                List<ReferenceParameter> referenceParameters = new ArrayList<ReferenceParameter>();
                for (org.alembic.aurion.common.subscription.ReferenceParameterType refParamType : subscriptionReferenceType.getReferenceParameters().getReferenceParameter())
                {
                    ReferenceParameter refParam = new ReferenceParameter();
                    refParam.setNamespace(refParamType.getNamespace());
                    refParam.setNamespacePrefix(refParamType.getPrefix());
                    refParam.setElementName(refParamType.getElementName());
                    refParam.setValue(refParamType.getValue());
                    referenceParameters.add(refParam);
                }
                subscriptionReference.setReferenceParameters(referenceParameters);
            }
        }
        return subscriptionReference;
    }

    protected Community loadCommunity(org.alembic.aurion.common.subscription.CommunityType communityType)
    {
        Community community = null;
        if (communityType != null)
        {
            community = new Community();
            community.setCommunityId(communityType.getId());
            community.setCommunityName(communityType.getName());
        }
        return community;
    }

    protected org.alembic.aurion.common.subscription.SubscriptionReferenceType loadSubscriptionReferenceType(SubscriptionReference subscriptionReference)
    {
        org.alembic.aurion.common.subscription.SubscriptionReferenceType subscriptionReferenceType = null;
        if (subscriptionReference != null)
        {
            subscriptionReferenceType = subscriptionRepositoryObjFact.createSubscriptionReferenceType();
            subscriptionReferenceType.setSubscriptionManagerEndpointAddress(subscriptionReference.getSubscriptionManagerEndpointAddress());
            if (subscriptionReference.getReferenceParameters() != null)
            {
                ReferenceParametersType refParametersType = subscriptionRepositoryObjFact.createReferenceParametersType();
                subscriptionReferenceType.setReferenceParameters(refParametersType);
                List<ReferenceParameterType> refParameterTypeList = refParametersType.getReferenceParameter();
                for (ReferenceParameter refParam : subscriptionReference.getReferenceParameters())
                {
                    ReferenceParameterType refParamType = subscriptionRepositoryObjFact.createReferenceParameterType();
                    refParamType.setNamespace(refParam.getNamespace());
                    refParamType.setPrefix(refParam.getNamespacePrefix());
                    refParamType.setElementName(refParam.getElementName());
                    refParamType.setValue(refParam.getValue());
                    refParameterTypeList.add(refParamType);
                }
            }
        }
        return subscriptionReferenceType;
    }

    protected org.alembic.aurion.common.subscription.SubscriptionItemType loadSubscriptionItemType(SubscriptionItem subscriptionItem)
    {
        org.alembic.aurion.common.subscription.SubscriptionItemType subscriptionItemType = null;
        if (subscriptionItem != null)
        {
            subscriptionItemType = new org.alembic.aurion.common.subscription.SubscriptionItemType();
            subscriptionItemType.setSubscriber(loadSubscriberType(subscriptionItem.getSubscriber()));
            subscriptionItemType.setSubscribee(loadSubscribeeType(subscriptionItem.getSubscribee()));
            subscriptionItemType.setSubscriptionCriteria(loadSubscriptionCriteriaType(subscriptionItem.getSubscriptionCriteria()));
            subscriptionItemType.setSubscriptionReference(loadSubscriptionReferenceType(subscriptionItem.getSubscriptionReference()));
            subscriptionItemType.setParentSubscriptionReference(loadSubscriptionReferenceType(subscriptionItem.getParentSubscriptionReference()));
        }
        return subscriptionItemType;
    }

    protected org.alembic.aurion.common.subscription.SubscriberType loadSubscriberType(SubscriptionParticipant subscriber)
    {
        org.alembic.aurion.common.subscription.SubscriberType subscriberType = null;
        if (subscriber != null)
        {
            subscriberType = new org.alembic.aurion.common.subscription.SubscriberType();
            subscriberType.setNotificationConsumerEndpointAddress(subscriber.getNotificationEndpointAddress());
            subscriberType.setUserAddress(subscriber.getUserAddress());
            subscriberType.setCommunity(loadCommunityType(subscriber.getCommunity()));
        }
        return subscriberType;
    }

    protected org.alembic.aurion.common.subscription.SubscribeeType loadSubscribeeType(SubscriptionParticipant subscriber)
    {
        org.alembic.aurion.common.subscription.SubscribeeType subscribeeType = null;
        if (subscriber != null)
        {
            subscribeeType = new org.alembic.aurion.common.subscription.SubscribeeType();
            subscribeeType.setNotificationProducerEndpointAddress(subscriber.getNotificationEndpointAddress());
            subscribeeType.setUserAddress(subscriber.getUserAddress());
            subscribeeType.setCommunity(loadCommunityType(subscriber.getCommunity()));
        }
        return subscribeeType;
    }

    protected org.alembic.aurion.common.subscription.SubscriptionCriteriaType loadSubscriptionCriteriaType(SubscriptionCriteria subscriptionCriteria)
    {
        org.alembic.aurion.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType = null;
        if (subscriptionCriteria != null)
        {
            subscriptionCriteriaType = new org.alembic.aurion.common.subscription.SubscriptionCriteriaType();
            subscriptionCriteriaType.setSubscriberPatient(loadQualifiedSubjectId(subscriptionCriteria.getSubscriberPatient()));
            subscriptionCriteriaType.setSubscribeePatient(loadQualifiedSubjectId(subscriptionCriteria.getSubscribeePatient()));
            subscriptionCriteriaType.setCriteria(loadCriteriaType(subscriptionCriteria.getCriteria()));
            subscriptionCriteriaType.setTopicExpression(loadTopicExpressionType(subscriptionCriteria.getTopicExpression()));
            subscriptionCriteriaType.setSubscriptionPolicy(loadSubscriptionPolicyType(subscriptionCriteria.getSubscriptionPolicy()));
        }
        return subscriptionCriteriaType;
    }

    protected org.alembic.aurion.common.subscription.CriteriaType loadCriteriaType(List<Criterion> criteria)
    {
        org.alembic.aurion.common.subscription.CriteriaType criteriaType = null;
        if (criteria != null)
        {
            criteriaType = new org.alembic.aurion.common.subscription.CriteriaType();
            List<org.alembic.aurion.common.subscription.CriterionType> criterionTypeList = criteriaType.getCriterion();
            for (Criterion criterion : criteria)
            {
                org.alembic.aurion.common.subscription.CriterionType criterionType = new org.alembic.aurion.common.subscription.CriterionType();
                criterionType.setKey(criterion.getKey());
                criterionType.setValue(criterion.getValue());
                criterionTypeList.add(criterionType);
            }
        }
        return criteriaType;
    }
    
    protected TopicExpressionType loadTopicExpressionType(TopicExpression topicExpression)
    {
        TopicExpressionType topicExpressionType = null;
        if(topicExpression != null)
        {
            topicExpressionType = new TopicExpressionType();
            topicExpressionType.setDialect(topicExpression.getDialect());
            topicExpressionType.setValue(topicExpression.getTopicExpressionValue());
        }
        return topicExpressionType;
    }
    
    protected SubscriptionPolicyType loadSubscriptionPolicyType(SubscriptionPolicy subscriptionPolicy)
    {
        SubscriptionPolicyType subscriptionPolicyType = null;
        if((subscriptionPolicy != null) && (subscriptionPolicy.getPolicyItems() != null) && (!subscriptionPolicy.getPolicyItems().isEmpty()))
        {
            subscriptionPolicyType = new SubscriptionPolicyType();
            KeyValuePairListType pairsType = new KeyValuePairListType();
            subscriptionPolicyType.setGenericPolicyItems(pairsType);
            
            for(SubscriptionPolicyItem policyItem : subscriptionPolicy.getPolicyItems())
            {
                KeyValuePairType keyValPairType = new KeyValuePairType();
                keyValPairType.setKey(policyItem.getKey());
                keyValPairType.setValue(policyItem.getValue());
                pairsType.getKeyValuePair().add(keyValPairType);
            }
        }
        return subscriptionPolicyType;
    }

    protected org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType loadQualifiedSubjectId(Patient patient)
    {
        org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType qualSubjId = null;
        if (patient != null)
        {
            qualSubjId = new org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType();
            qualSubjId.setSubjectIdentifier(patient.getPatientId());
            if(patient.getAssigningAuthority() != null)
            {
                qualSubjId.setAssigningAuthorityIdentifier(patient.getAssigningAuthority().getCommunityId());
            }
        }
        return qualSubjId;
    }

    protected org.alembic.aurion.common.nhinccommon.HomeCommunityType loadHomeCommunity(Community community)
    {
        org.alembic.aurion.common.nhinccommon.HomeCommunityType homeCommunity = null;
        if (community != null)
        {
            homeCommunity = new org.alembic.aurion.common.nhinccommon.HomeCommunityType();
            homeCommunity.setHomeCommunityId(community.getCommunityId());
            homeCommunity.setName(community.getCommunityName());
        }
        return homeCommunity;
    }

    protected org.alembic.aurion.common.subscription.CommunityType loadCommunityType(Community community)
    {
        org.alembic.aurion.common.subscription.CommunityType communityType = null;
        if (community != null)
        {
            communityType = new org.alembic.aurion.common.subscription.CommunityType();
            communityType.setId(community.getCommunityId());
            communityType.setName(community.getCommunityName());
        }
        return communityType;
    }

    protected abstract SubscriptionType getSubscriptionType();
}
