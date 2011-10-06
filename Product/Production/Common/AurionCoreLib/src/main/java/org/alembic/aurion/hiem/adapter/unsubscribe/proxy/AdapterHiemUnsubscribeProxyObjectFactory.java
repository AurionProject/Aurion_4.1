/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.unsubscribe.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author rayj
 */
public class AdapterHiemUnsubscribeProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "HiemProxyConfig.xml";
    private static final String BEAN_NAME = "hiemunsubscribeadapter";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     *
     * @return AdapterHiemNotifyProxy
     */
    public AdapterHiemUnsubscribeProxy getAdapterHiemUnsubscribeProxy()
    {
        return getBean(BEAN_NAME, AdapterHiemUnsubscribeProxy.class);
    }

}

