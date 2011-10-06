/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.persistence;

import org.alembic.aurion.performance.dao.AuditPerformanceDAO;
import org.alembic.aurion.performance.model.AuditPerformance;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class AuditPerformancePersistence {

    private static Log log = LogFactory.getLog(AuditPerformancePersistence.class);
    private AuditPerformanceDAO oAuditPerformanceDAO = null;

    /**
     * Default constructor
     */
    public AuditPerformancePersistence() {
        log.debug("AuditPerformancePersistence constructor -- Begin");
        oAuditPerformanceDAO = AuditPerformanceDAO.getAuditPerformanceDAOInstance();
        log.debug("AuditPerformancePersistence constructor -- End");
    }

    /**
     *
     * @param auditPerformance
     * @return List
     */
    public List<AuditPerformance> findAll() {
        log.debug("AuditPerformancePersistence getAllPerformanceInfo -- Begin");
        return oAuditPerformanceDAO.findAll();
    }

    /**
     *
     * @param auditList
     * @return boolean
     */
    public boolean insertAuditPerformanceRecord(List<AuditPerformance> auditList) {
        log.debug("AuditPerformancePersistence insertAuditPerformanceRecord -- Begin");
        return oAuditPerformanceDAO.insertAuditPerformanceRecord(auditList);
    }

    /**
     * 
     * @return boolean
     */
    public boolean deleteAllAuditPerformanceRecords(AuditPerformance auditPerformance) {
        log.debug("AuditPerformancePersistence deleteAllAuditPerformanceRecords -- Begin");
        return oAuditPerformanceDAO.deleteAllAuditPerformanceRecords(auditPerformance);
    }

    /**
     * 
     * @return boolean
     */
    public boolean deleteAllAuditPerformanceRecords() {
        log.debug("AuditPerformancePersistence deleteAllAuditPerformanceRecords -- Begin");
        return oAuditPerformanceDAO.deleteAllAuditPerformanceRecords();
    }

    /**
     * 
     * @return boolean
     */
    public boolean deleteFirstFiftyRecords() {
        log.debug("AuditPerformancePersistence deleteFirstFiftyRecords -- Begin");
        return oAuditPerformanceDAO.deleteFirstFiftyRecords();
    }
}
