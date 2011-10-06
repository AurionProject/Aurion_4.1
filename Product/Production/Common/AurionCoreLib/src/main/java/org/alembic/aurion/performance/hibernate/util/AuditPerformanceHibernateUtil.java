/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.hibernate.util;

import org.alembic.aurion.properties.HibernateAccessor;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Sai Valluripalli
 */
public class AuditPerformanceHibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final String HIBERNATE_AUDIT_CONFIG = "auditperformance.hibernate.cfg.xml";
    private static Log log = LogFactory.getLog(AuditPerformanceHibernateUtil.class);

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory =
                    new Configuration().configure(getConfigFile()).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getHibernateFile(HIBERNATE_AUDIT_CONFIG);
        } catch (Exception ex) {
            log.error("Unable to load " + HIBERNATE_AUDIT_CONFIG +
                    " " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * Method returns an instance of Hibernate SessionFactory
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
