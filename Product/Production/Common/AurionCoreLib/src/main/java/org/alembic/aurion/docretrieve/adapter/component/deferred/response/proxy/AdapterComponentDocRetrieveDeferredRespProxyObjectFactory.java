/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.component.deferred.response.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 3:02:25 PM
 */
public class AdapterComponentDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adaptercomponentdocretrievedeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocRetrieveDeferredRespProxy getAdapterDocRetrieveDeferredResponseProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocRetrieveDeferredRespProxy.class);
    }
}
