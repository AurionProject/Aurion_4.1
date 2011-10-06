/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
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
import org.alembic.aurion.hiem.entity.subscribe.EntityHiemSubscribeOrchImpl;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityHiemSubscribeProxyJavaImpl implements EntityHiemSubscribeProxy
{
    
    @Override
    public SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, Element subscribeElement) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        EntityHiemSubscribeOrchImpl orchImpl = new EntityHiemSubscribeOrchImpl();
        return orchImpl.subscribe(subscribeRequest, assertion, targets, subscribeElement);
    }
}
