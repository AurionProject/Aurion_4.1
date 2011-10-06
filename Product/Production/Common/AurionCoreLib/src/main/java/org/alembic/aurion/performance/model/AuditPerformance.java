/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.performance.model;

/**
 *
 * @author Sai Valluripalli
 */
public class AuditPerformance {

    int id = 0;
    String transactionId = null;
    String txTimeStamp = null;
    String interface_ = null;
    int attempts = 0;
    String type = null;

    public String getTxTimeStamp() {
        return txTimeStamp;
    }

    public void setTxTimeStamp(String txTimeStamp) {
        this.txTimeStamp = txTimeStamp;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInterface_() {
        return interface_;
    }

    public void setInterface_(String interface_) {
        this.interface_ = interface_;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
