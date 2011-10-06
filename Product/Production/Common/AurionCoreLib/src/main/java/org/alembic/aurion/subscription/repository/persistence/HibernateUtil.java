/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.persistence;

import org.alembic.aurion.properties.HibernateAccessor;
import org.alembic.aurion.nhinclib.NhincConstants;
import java.io.File;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to create hibernate connections
 * 
 * @author Neil Webb
 */
public class HibernateUtil 
{
    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);
   
    static
    {
        try
        {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();
        } 
        catch (Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    private static File getConfigFile(){
        File result = null;

        try
        {
            result = HibernateAccessor.getHibernateFile(NhincConstants.HIBERNATE_HIEM_REPOSITORY);
        }
        catch (Exception ex)
        {
            log.error("Unable to load " + NhincConstants.HIBERNATE_HIEM_REPOSITORY + " " + ex.getMessage(), ex );
        }


        return result;


    }
}