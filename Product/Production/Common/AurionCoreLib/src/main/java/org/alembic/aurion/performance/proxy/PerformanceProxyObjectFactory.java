/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class PerformanceProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "PerformanceProxyConfig.xml";
    private static final String BEAN_NAME = "performance";
    
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve Performance implementation using Spring IOC framework
     * This method retrieves the object from the framework that has an
     * identifier of "performance".
     * @return PerformanceProxy
     */
    public PerformanceProxy getPerformanceProxy()
    {
        return getBean(BEAN_NAME, PerformanceProxy.class);
    }
    
}
