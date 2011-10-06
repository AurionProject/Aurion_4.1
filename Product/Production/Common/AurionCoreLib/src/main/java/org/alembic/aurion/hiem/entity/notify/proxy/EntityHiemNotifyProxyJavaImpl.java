/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.subscriptionb2overrideforcdc.NotifyCdcBioPackageType;
import org.alembic.aurion.common.subscriptionb2overridefordocuments.NotifyDocumentType;
import org.alembic.aurion.hiem.entity.notify.EntityHiemNotifyOrchImpl;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityHiemNotifyProxyJavaImpl implements EntityHiemNotifyProxy
{

    public AcknowledgementType notifySubscribersOfDocument(NotifyDocumentType notifySubscribersOfDocumentRequest, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        return null;
    }

    public AcknowledgementType notifySubscribersOfCdcBioPackage(NotifyCdcBioPackageType notifySubscribersOfCdcBioPackageRequest, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        return null;
    }

    public AcknowledgementType notify(Notify notifyRequest, AssertionType assertion, NhinTargetCommunitiesType targets, String rawNotifyXml)
    {
        EntityHiemNotifyOrchImpl orchImpl = new EntityHiemNotifyOrchImpl();
        return orchImpl.notify(notifyRequest, assertion, targets, rawNotifyXml);
    }
}
