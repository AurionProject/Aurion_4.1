/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.proxy;

import org.alembic.aurion.performance.model.AuditPerformance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class PerformanceProxyNoOpImpl implements PerformanceProxy
{
    private static Log log = LogFactory.getLog(PerformanceProxyNoOpImpl.class);
    
    /**
     * 
     * @param oAuditPerformance
     * @return boolean
     */
    public boolean logPerformance(AuditPerformance oAuditPerformance) {
        log.debug("PerformanceProxyNoOpImpl.logPerformance() -- NoOp implementation just returns success");
        return true;
    }
}
