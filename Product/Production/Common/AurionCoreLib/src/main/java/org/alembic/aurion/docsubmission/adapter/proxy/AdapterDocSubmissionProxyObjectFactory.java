/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 * @author svalluripalli
 */
public class AdapterDocSubmissionProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "DocumentSubmissionProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocsubmission";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocSubmissionProxy getAdapterDocSubmissionProxy() {
        return getBean(BEAN_NAME, AdapterDocSubmissionProxy.class);
    }

}
