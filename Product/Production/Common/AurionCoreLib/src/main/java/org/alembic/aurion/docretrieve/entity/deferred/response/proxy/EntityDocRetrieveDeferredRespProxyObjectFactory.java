/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.response.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE = "entitydocretrievedeferredresp";

    /**
     * Retrieve an Entity Document Retrieve Deferred Response implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "entitydocretrievedeferredresp".
     * @return EntityDocRetrieveDeferredRespProxy
     */
    public EntityDocRetrieveDeferredRespProxy getEntityDocRetrieveDeferredRespProxy()
    {
            return getBean(BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE, EntityDocRetrieveDeferredRespProxy.class);
    }

    /**
     * 
     * @return String
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }
    
}
