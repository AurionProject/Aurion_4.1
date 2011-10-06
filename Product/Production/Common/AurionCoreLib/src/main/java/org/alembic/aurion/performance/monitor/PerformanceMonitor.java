/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.monitor;

import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.persistence.AuditPerformancePersistence;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class PerformanceMonitor
{
    private static Log log = LogFactory.getLog(PerformanceMonitor.class);
    private String PERFORMANCE_PROPERTY_FILE = "performancereporting";
    private String PERFORMANCE_MAX_TRANSACTION_PROPERTY = "MaxPerformanceTransactions";
    private String PERFORMANCE_SERVICE_ENABLED_PROPERTY = "PerformanceMetricsService";

    /**
     * Construtor...
     */
    public PerformanceMonitor() {
    }

    /**
     * This is the main method to log the performance record to database...
     * @param strTxId
     * @param sInterface
     * @param sType
     * @param iAttempts
     * @return boolean
     */
    public boolean logPerformance(AuditPerformance oAuditPerformance) {
        boolean proceed = true;
        proceed = checkServiceStatus();
        if (!proceed) {
            log.info("Performance Monitor Service Not Enabled");
            return false;
        }
        proceed = checkPerformanceRecordsSize();
        if (!proceed) {
            log.error("PerformanceMonitor.logPerformance() number of records in performance store exceeds the limit");
            return false;
        }
        List<AuditPerformance> lAuditPerfromance = new ArrayList();
        lAuditPerfromance.add(oAuditPerformance);
        proceed = new AuditPerformancePersistence().insertAuditPerformanceRecord(lAuditPerfromance);
        lAuditPerfromance.clear();
        return proceed;
    }

    /**
     *
     * @return boolean
     */
    private boolean checkServiceStatus() {
        boolean isServiceEnabled = false;
        try {
            String sServiceEnabled = PropertyAccessor.getProperty(PERFORMANCE_PROPERTY_FILE, PERFORMANCE_SERVICE_ENABLED_PROPERTY);
            if (null != sServiceEnabled && !sServiceEnabled.equals("") && sServiceEnabled.equalsIgnoreCase("true")) {
                isServiceEnabled = true;
            }
        } catch (Exception exp) {
            log.error("Unable to access " + PERFORMANCE_SERVICE_ENABLED_PROPERTY + " from property file " + PERFORMANCE_PROPERTY_FILE + " " + exp.getMessage());
        }
        return isServiceEnabled;
    }

    /**
     * This method checks the size of performance table and compares with the number of records actually needed
     * @return boolean
     */
    private boolean checkPerformanceRecordsSize() {
        boolean processRecord = true;
        try {
            String sMaxSize = PropertyAccessor.getProperty(PERFORMANCE_PROPERTY_FILE, PERFORMANCE_MAX_TRANSACTION_PROPERTY);
            int iMaxSize = 0;
            int recordsFound = 0;
            if (null != sMaxSize &&
                    !sMaxSize.equals("")) {
                iMaxSize = Integer.parseInt(sMaxSize);
            }
            AuditPerformancePersistence oPerformance = new AuditPerformancePersistence();
            List<AuditPerformance> lPerfromanceRecords = oPerformance.findAll();
            if (null != lPerfromanceRecords && lPerfromanceRecords.size() > 0) {
                recordsFound = lPerfromanceRecords.size();
            }
            if (recordsFound >= iMaxSize) {
                processRecord = cleanPerformanceRecords();
            }
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return processRecord;
    }

    /**
     * This method clears the database to have most recent 100 records. Old records will be cleaned.
     * @param lPerfromanceRecords
     * @return boolean
     */
    private boolean cleanPerformanceRecords()
    {
        
        AuditPerformancePersistence oPerformance = new AuditPerformancePersistence();
        boolean isDelete = oPerformance.deleteFirstFiftyRecords();
        return isDelete;
    }
}
