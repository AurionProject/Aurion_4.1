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

package org.alembic.aurion.patientcorrelation.nhinc.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.patientcorrelation.nhinc.PatientCorrelationOrchImpl;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationProxyJavaImpl implements PatientCorrelationProxy {

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request, AssertionType assertion) {
        return new PatientCorrelationOrchImpl().retrievePatientCorrelations(request, assertion);
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion) {
        return new PatientCorrelationOrchImpl().addPatientCorrelation(request, assertion);
    }

}
