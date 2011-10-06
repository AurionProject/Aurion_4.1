/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.subscribe;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageRequestType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageResponseType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentRequestSecuredType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentResponseType;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidFilterFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import org.alembic.aurion.hiem.processor.entity.EntitySubscribeProcessor;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityHiemSubscribeOrchImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityHiemSubscribeOrchImpl.class);

    public SubscribeDocumentResponseType subscribeDocument(SubscribeDocumentRequestSecuredType arg0) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, Element subscribeElement) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        SubscribeResponse response = null;

        EntitySubscribeProcessor processor = new EntitySubscribeProcessor();
        try {
            response = processor.processSubscribe(subscribeRequest, subscribeElement, assertion, targets);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex.getCause());
        }

        return response;
    }
}
