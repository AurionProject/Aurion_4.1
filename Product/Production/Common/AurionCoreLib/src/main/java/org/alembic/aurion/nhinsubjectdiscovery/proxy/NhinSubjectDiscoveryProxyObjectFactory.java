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

package org.alembic.aurion.nhinsubjectdiscovery.proxy;

import org.alembic.aurion.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
 * <bean id="nhinsubjectdiscovery" class="org.alembic.aurion.nhinsubjectdiscovery.proxy.NhinSubjectDiscoveryNoOpImpl"/>
 * the bean id is "nhinsubjectdiscovery" and an object of this type can be retrieved from
 * the application context by calling the getBean method like:
 * context.getBean("nhinsubjectdiscovery");. This returns an object that can be casted to
 * the appropriate interface and then used in the application code. See the
 * getNhinSubjectDisocoveryProxy() method in this class.
 *
 * @author Jon Hoppesch
 */
public class NhinSubjectDiscoveryProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "NhinSubjectDiscoveryProxyConfig.xml";
    private static final String BEAN_NAME_SUBJECT_DISCOVERY = "nhinsubjectdiscovery";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

        /**
     * Retrieve a nhin subject discovery implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhinsubjectdiscovery."
     *
     * @return NhinSubjectDiscoveryProxy instance
     */
    public NhinSubjectDiscoveryProxy getNhinSubjectDiscoveryProxy() {
        NhinSubjectDiscoveryProxy nhinSubjectDiscovery = null;
        if (context != null) {
            nhinSubjectDiscovery = (NhinSubjectDiscoveryProxy) context.getBean(BEAN_NAME_SUBJECT_DISCOVERY);
        }
        return nhinSubjectDiscovery;
    }

}
