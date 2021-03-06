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

package org.alembic.aurion.adaptercomponentmpi;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "org.alembic.aurion.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/AdapterComponentMpi/AdapterComponentMpi.wsdl")
@Stateless
public class AdapterComponentMpi {

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
        return PatientChecker.FindPatient(findCandidatesRequest);
    }

}
