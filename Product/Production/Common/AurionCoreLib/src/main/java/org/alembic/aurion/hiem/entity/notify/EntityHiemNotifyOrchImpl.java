/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.notify;

import org.alembic.aurion.hiem.processor.entity.EntityNotifyProcessor;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.oasis_open.docs.wsn.b_2.Notify;
/**
 *
 * @author Sai Valluripalli
 */
public class EntityHiemNotifyOrchImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityHiemNotifyOrchImpl.class);

    public AcknowledgementType notify(Notify notifyRequest, AssertionType assertion, NhinTargetCommunitiesType targets, String rawNotifyXml)
    {
        log.debug("EntityHiemNotifyOrchImpl.notify");
        AcknowledgementType ack = new AcknowledgementType();

        try
        {
            EntityNotifyProcessor processor = new EntityNotifyProcessor();
            processor.processNotify(notifyRequest, assertion, rawNotifyXml);
        }
        catch (Throwable t)
        {
            log.error("Exception encountered processing notify message: " + t.getMessage(), t);
        }

        return ack;
    }
}
