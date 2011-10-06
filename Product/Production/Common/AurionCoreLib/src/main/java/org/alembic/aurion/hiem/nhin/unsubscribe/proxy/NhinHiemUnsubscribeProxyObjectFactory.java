/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.nhin.unsubscribe.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author rayj
 */
public class NhinHiemUnsubscribeProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "HiemProxyConfig.xml";
    private static final String BEAN_NAME = "nhinhiemunsubscribe";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     *
     * @return EntitySubscribeProxy
     */
    public NhinHiemUnsubscribeProxy getNhinHiemUnsubscribeProxy()
    {
        return getBean(BEAN_NAME, NhinHiemUnsubscribeProxy.class);
    }
    
}
