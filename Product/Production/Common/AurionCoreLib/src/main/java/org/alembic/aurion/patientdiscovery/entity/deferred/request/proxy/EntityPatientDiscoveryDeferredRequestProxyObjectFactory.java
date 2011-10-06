/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.request.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;


public class EntityPatientDiscoveryDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "EntityPatientDiscoveryAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "entitypatientdiscoveryasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityPatientDiscoveryDeferredRequestProxy getEntityPatientDiscoveryDeferredRequestProxy() {
        return getBean(BEAN_NAME, EntityPatientDiscoveryDeferredRequestProxy.class);
    }

}
