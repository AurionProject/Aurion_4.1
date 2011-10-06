/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocRetrieveProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "PassthruDocRetrieveProxyConfig.xml";
    private static final String BEAN_NAME = "passthrudocretrieve";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocRetrieveProxy getPassthruDocRetrieveProxy()
    {
        return getBean(BEAN_NAME, PassthruDocRetrieveProxy.class);
    }
}
