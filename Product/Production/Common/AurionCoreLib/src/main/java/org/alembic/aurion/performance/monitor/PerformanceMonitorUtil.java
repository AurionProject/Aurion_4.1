/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.monitor;

import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.proxy.PerformanceProxy;
import org.alembic.aurion.performance.proxy.PerformanceProxyObjectFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sai Valluripalli
 */
public class PerformanceMonitorUtil
{

    /**
     *
     * @param reTryAttempts
     * @param interfaceName
     * @param msgId
     * @param oType
     * @return AuditPerformance
     */
    public static AuditPerformance buildAuditPerfromance(int reTryAttempts, String interfaceName, String msgId, String oType)
    {
        AuditPerformance oAuditPerformance = new AuditPerformance();
        oAuditPerformance.setAttempts(reTryAttempts);
        oAuditPerformance.setInterface_(interfaceName);
        oAuditPerformance.setTransactionId(msgId);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        String str = sdf.format(new Date());
        oAuditPerformance.setTxTimeStamp(str);
        oAuditPerformance.setType(oType);
        return oAuditPerformance;
    }

    /**
     * retruns Proxy instance...
     * @return
     */
    public static PerformanceProxy getPerformanceProxy()
    {
        PerformanceProxyObjectFactory factory = new PerformanceProxyObjectFactory();
        PerformanceProxy proxy = factory.getPerformanceProxy();
        return proxy;
    }

}
