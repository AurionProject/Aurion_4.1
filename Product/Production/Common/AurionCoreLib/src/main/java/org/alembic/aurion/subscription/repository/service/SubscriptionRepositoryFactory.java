/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.service;

import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.subscription.repository.SubscriptionRepositoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for getting an instance of the subscription repository service
 * 
 * @author Neil Webb
 */
public class SubscriptionRepositoryFactory
{
    private static Log log = LogFactory.getLog(SubscriptionRepositoryFactory.class);
    private static final String PROPERTIES_FILE_NAME = "gateway";
    private static final String IMPL_CLASS_NAME_KEY = "subscription.repository.implementation.class";

    /**
     * Create a subscription repository service object. The implementation class
     * for the subscription repository service is defined in the 
     * gateway.properties file using the key: 
     * "subscription.repository.implementation.class"
     * 
     * @return Subscription repository service instance.
     * @throws org.alembic.aurion.subscription.repository.SubscriptionRepositoryException
     */
    public SubscriptionRepositoryService getSubscriptionRepositoryService() throws SubscriptionRepositoryException
    {
        SubscriptionRepositoryService repositoryService = null;
        String errorMessage = "Unknown error creating subscription repository service";

        String implClassName = null;
        try
        {
            log.debug("Retrieving the subscription repository class name");
            implClassName = PropertyAccessor.getProperty(PROPERTIES_FILE_NAME, IMPL_CLASS_NAME_KEY);
            log.debug("Retrieved the subscription repository class name: " + implClassName);
        }
        catch (Throwable t)
        {
            errorMessage = "An error occured locating the implementation class " +
                "for the subscription repository service. Please ensure " +
                "that the implementation class is defined in the " +
                "gateway.properties file using the key: " +
                "\"subscription.repository.implementation.class\". The " +
                "error message was: " + t.getMessage();
            log.error("Error retrieving the subscription implementaion class name: " + t.getMessage(), t);
        }
        if ((implClassName == null) || ("".equals(implClassName.trim())))
        {
            if (errorMessage == null)
            {
                errorMessage = "Unable to locate the implementation class " +
                    "for the subscription repository service. Please ensure " +
                    "that the implementation class is defined in the " +
                    "gateway.properties file using the key: " +
                    "\"subscription.repository.implementation.class\".";
            }
        }
        else
        {
            try
            {
                log.debug("Instantiating the subscription repository service using the class name: " + implClassName);
                repositoryService = (SubscriptionRepositoryService) Class.forName(implClassName).newInstance();
            }
            catch (Throwable t)
            {
                errorMessage = "Unable to instantiate the implementation class " +
                    "for the subscription repository service. The class " +
                    "name provided was: " + implClassName +
                    ". Please ensure that this class exists and is " +
                    "accessable. The exception was: " + t.getMessage();

                log.error("Error instantiating the subscription implementaion class: " + t.getMessage(), t);
            }
        }

        if (repositoryService == null)
        {
            log.error(errorMessage);
            throw new SubscriptionRepositoryException(errorMessage);
        }

        return repositoryService;
    }
}
