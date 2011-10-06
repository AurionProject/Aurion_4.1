/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.proxy;

import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class PerformanceProxyJavaImpl implements PerformanceProxy
{
    private static Log log = LogFactory.getLog(PerformanceProxyJavaImpl.class);
    /**
     * 
     * @param oAuditPerformance
     * @return boolean
     */
    public boolean logPerformance(AuditPerformance oAuditPerformance)
    {
        log.debug("PerformanceProxyJavaImpl.logPerformance() -- Begin");
        PerformanceMonitor oPerformanceMonitor = new PerformanceMonitor();
        log.debug("PerformanceProxyJavaImpl.logPerformance() -- End");
        return oPerformanceMonitor.logPerformance(oAuditPerformance);
    }

}
