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
package org.alembic.aurion.patientdiscovery.adapter.deferred.request.queue.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 * An object factory that uses the Spring Framework to create service
 * implementation objects. The configuration file is referenced in the
 * common properties file location to assist in installation and configuration
 * simplicity.
 *
 * The Spring configuration file is defined by the constant "SPRING_CONFIG_FILE".
 * This file is loaded into an application context in the static initializer
 * and then the objects defined in the config file are available to the framework
 * for creation. This configuration file can be located anywhere in the
 * classpath.
 *
 * To retrieve an object that is created by the framework, the
 * "getBean(String beanId)" method is called on the application context passing
 * in the beanId that is specified in the config file. Considering the default
 * correlation definition in the config file for this component:
 * <bean id="adapterpatientdiscoveryasyncreqqueue" class="org.alembic.aurion.adapter.patientdiscovery.async.req.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueNoOpImpl"/>
 * the bean id is "adapterpatientdiscoveryasyncreqqueue" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("adapterpatientdiscoveryasyncreqqueue");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getAdapterPatientDisocoveryAsyncReqQueueProxy() method in this class.
 *
 * @author Jon Hoppesch
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterPatientDiscoveryAsyncReqQueueProxyConfig.xml";
    private static final String BEAN_NAME = "adapterpatientdiscoveryasyncreqqueue";

    /**
     * Returns the name of the config file.
     *
     * @return The name of the config file.
     */
    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Return an instance of the AdapterPatientDiscoveryAsyncReqQueueProxy class.
     *
     * @return An instance of the AdapterPatientDiscoveryAsyncReqQueueProxy class.
     */
    public AdapterPatientDiscoveryAsyncReqQueueProxy getAdapterPatientDiscoveryAsyncReqQueueProxy()
    {
        return getBean(BEAN_NAME, AdapterPatientDiscoveryAsyncReqQueueProxy.class);
    }
}
