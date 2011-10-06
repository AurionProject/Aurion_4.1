/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.passthru.notify;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.nhin.notify.proxy.NhinHiemNotifyProxy;
import org.alembic.aurion.hiem.nhin.notify.proxy.NhinHiemNotifyProxyObjectFactory;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author jhoppesc
 */
public class PassthruHiemNotifyOrchImpl
{

    private static Log log = LogFactory.getLog(PassthruHiemNotifyOrchImpl.class);

    public void notify(Notify notifyRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements, Element notifyElement)
    {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");       

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, assertion, target);

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    private AcknowledgementType audit(Notify notifyRequest)
    {
        AcknowledgementType ack = null;

        return ack;
    }

}
