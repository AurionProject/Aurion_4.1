/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.response.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

public class AdapterDocSubmissionDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterXDRResponseProxyConfig.xml";
    private static final String BEAN_NAME = "adapterxdrresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocSubmissionDeferredResponseProxy getAdapterDocSubmissionDeferredResponseProxy()
    {
        return getBean(BEAN_NAME, AdapterDocSubmissionDeferredResponseProxy.class);
    }
}
