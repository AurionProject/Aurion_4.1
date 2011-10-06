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

package org.alembic.aurion.mpi.adapter.component;

import org.alembic.aurion.mpilib.Patients;

/**
 *
 * @author rayj
 */
public class CommonChecks {

    public static boolean isSingleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() == 1));
    }

    public static boolean isMultipleSearchResult(Patients patients) {
        return ((patients != null) && (patients.size() > 1));
    }

    public static boolean isZeroSearchResult(Patients patients) {
        return ((patients == null) || (patients.size() == 0));
    }

}
