/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.data;

import java.io.Serializable;

/**
 * Data class for a patient
 * 
 * @author Neil Webb
 */
public class Patient implements Serializable
{
    private static final long serialVersionUID = -4817108979630447814L;
    private String patientId;
    private Community assigningAuthority;

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public Community getAssigningAuthority()
    {
        return assigningAuthority;
    }

    public void setAssigningAuthority(Community assigningAuthority)
    {
        this.assigningAuthority = assigningAuthority;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Patient other = (Patient) obj;
        if (this.patientId != other.patientId && (this.patientId == null || !this.patientId.equals(other.patientId)))
        {
            return false;
        }
        if (this.assigningAuthority != other.assigningAuthority && (this.assigningAuthority == null || !this.assigningAuthority.equals(other.assigningAuthority)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + (this.patientId != null ? this.patientId.hashCode() : 0);
        hash = 89 * hash + (this.assigningAuthority != null ? this.assigningAuthority.hashCode() : 0);
        return hash;
    }
}
