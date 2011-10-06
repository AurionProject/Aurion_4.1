/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.request.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocSubmissionDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME = "adapterxdrrequest";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocSubmissionDeferredRequestProxy getAdapterDocSubmissionDeferredRequestProxy()
    {
        return getBean(BEAN_NAME, AdapterDocSubmissionDeferredRequestProxy.class);
    }
}


