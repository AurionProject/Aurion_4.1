/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.w3c.dom.Element;

/**
 *
 * @author Sai Valluripalli
 */
public interface EntityHiemSubscribeProxy {
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, Element subscribeElement) throws org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidFilterFault, org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault, org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault, org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault, org.alembic.aurion.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault, org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault, org.alembic.aurion.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.TopicNotSupportedFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
}
