/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.proxy;

import org.alembic.aurion.performance.model.AuditPerformance;

/**
 *
 * @author Sai Valluripalli
 */
public interface PerformanceProxy
{
   public boolean logPerformance(AuditPerformance oAuditPerformance);
}
