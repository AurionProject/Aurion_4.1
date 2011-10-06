/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.notify;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author jhoppesc
 */
public class HiemNotifyImpl {

    private static Log log = LogFactory.getLog(HiemNotifyImpl.class);

    public void notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("Entering HiemNotifyImpl.notify");

        try {
            NhinHiemNotifyOrchImpl notifyOrchImpl = new NhinHiemNotifyOrchImpl();
            notifyOrchImpl.notify(notifyRequest, context);
        } catch (Throwable t) {
            log.debug("Exception encountered processing a notify message: " + t.getMessage(), t);
            // TODO: Add specific catch statements and throw the appropriate fault
        }
        log.debug("Exiting HiemNotifyImpl.notify");
    }
}
