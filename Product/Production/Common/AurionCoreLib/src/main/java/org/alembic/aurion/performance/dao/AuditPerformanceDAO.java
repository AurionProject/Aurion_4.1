/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.dao;

import org.alembic.aurion.performance.hibernate.util.AuditPerformanceHibernateUtil;
import org.alembic.aurion.performance.model.AuditPerformance;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Sai Valluripalli
 */
public class AuditPerformanceDAO {

    private static Log log = LogFactory.getLog(AuditPerformanceDAO.class);
    private static AuditPerformanceDAO oAuditPerformanceDAO = null;

    /**
     * default constructor...
     */
    private AuditPerformanceDAO() {
        log.info("AuditPerformanceDAO - Initialized");
    }

    /**
     * Singleton instance creator
     * @returnm AuditPerformanceDAO
     */
    public static AuditPerformanceDAO getAuditPerformanceDAOInstance() {
        if (null == oAuditPerformanceDAO) {
            oAuditPerformanceDAO = new AuditPerformanceDAO();
        }
        return oAuditPerformanceDAO;
    }

    /**
     *
     * @param auditPerformance
     * @return List
     */
    public List<AuditPerformance> findAll() {
        log.debug("AuditPerformanceDAO.getAllPerformanceInfo() -- Begin");
        Session session = null;
        List<AuditPerformance> queryList = null;
        try {
            SessionFactory sessionFactory =
                    AuditPerformanceHibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(AuditPerformance.class);
            queryList = crit.list();
        } catch (Exception exp) {
            log.error("Exception in AuditPerformanceDAO.getAllPerformanceInfo() occured due to :" + exp.getMessage());
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {

                session.flush();
                session.close();
            }
        }
        log.debug("AuditPerformanceDAO.getAllPerformanceInfo() -- End");
        return queryList;
    }

    /**
     *
     * @param auditPerformance
     * @return boolean
     */
    public boolean insertAuditPerformanceRecord(List<AuditPerformance> auditList) {
        log.debug("AuditPerformanceDAO.insertAuditPerformanceRecord() -- Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;
        if (auditList != null && auditList.size() > 0) {
            int size = auditList.size();
            AuditPerformance auditRecord = null;

            try {
                SessionFactory sessionFactory =
                        AuditPerformanceHibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");
                for (int i = 0; i < size; i++) {
                    auditRecord = auditList.get(i);
                    session.persist(auditRecord);
                }
                result = true;
                log.info("AuditPerformance List Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Error during insertion caused by :" +
                        e.getMessage());
            } finally {
                // Actual event_log insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("AuditPerformanceDAO.insertAuditPerformanceRecord() -- End");
        return result;
    }

    /**
     * 
     * @return boolean
     */
    public boolean deleteAllAuditPerformanceRecords(AuditPerformance auditPerformance) {
        log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = false;
        String sql = "delete from auditperformance where interface = '"+auditPerformance.getInterface_()+"'";
        try {
            SessionFactory sessionFactory =
                    AuditPerformanceHibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            if (session != null) {
                tx = session.beginTransaction();
                int rowCount = session.createSQLQuery(sql).executeUpdate();
                if (rowCount > 0) {
                    result = true;
                }
            } else {
                log.error("Failed to obtain a session from the sessionFactory");
            }
        } catch (Exception e) {
            result = false;
            log.error(e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- End");
        return result;
    }

    /**
     * This method clears the database to accept more data...
     * @return boolean
     */
    public boolean deleteAllAuditPerformanceRecords()
    {
        log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = false;
        String sql = "delete from auditperformance";
        try {
            SessionFactory sessionFactory =
                    AuditPerformanceHibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            if (session != null) {
                tx = session.beginTransaction();
                int rowCount = session.createSQLQuery(sql).executeUpdate();
                if (rowCount > 0) {
                    result = true;
                }
            } else {
                log.error("Failed to obtain a session from the sessionFactory");
            }
        } catch (Exception e) {
            result = false;
            log.error(e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- End");
        return result;
    }

    /**
     * 
     * @return boolean
     */
    public boolean deleteFirstFiftyRecords()
    {
       log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = false;
        String sql = "DELETE FROM auditperformance ORDER BY id LIMIT 68";
        try {
            SessionFactory sessionFactory =
                    AuditPerformanceHibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            if (session != null) {
                tx = session.beginTransaction();
                int rowCount = session.createSQLQuery(sql).executeUpdate();
                if (rowCount > 0) {
                    result = true;
                }
            } else {
                log.error("Failed to obtain a session from the sessionFactory");
            }
        } catch (Exception e) {
            result = false;
            log.error(e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        log.debug("AuditPerformanceDAO.deleteAllAuditPerformanceRecords() -- End");
        return result;
    }
}
