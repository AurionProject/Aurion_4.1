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

package org.alembic.aurion.docregistry.adapter.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 * @author svalluripalli
 */
public class AdapterComponentDocRegistryProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "AdapterDocumentRegistryProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocumentregistry";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocRegistryProxy getAdapterComponentDocRegistryProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocRegistryProxy.class);
    }

}
