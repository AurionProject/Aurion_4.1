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
package org.alembic.aurion.adapter.cppgui.servicefacade;

import org.alembic.aurion.adapter.cppgui.AssertionCreator;
import org.alembic.aurion.adapter.cppgui.PatientSearchCriteria;
import org.alembic.aurion.adapter.cppgui.valueobject.PatientVO;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.PersonNameType;
import org.alembic.aurion.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import org.alembic.aurion.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import org.alembic.aurion.mpilib.Identifier;
import org.alembic.aurion.mpilib.Patient;
import org.alembic.aurion.mpilib.Patients;
import org.alembic.aurion.mpilib.PersonName;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.subdisc.HL7Extractors;
import org.alembic.aurion.transform.subdisc.HL7PRPA201305Transforms;
import org.alembic.aurion.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author patlollav
 */
public class PatientSearchFacade {


    public List<PatientVO> searchPatient(PatientSearchCriteria patientSearchCriteria) throws Exception
    {

        List<PatientVO> patientVOs = null;

        PRPAIN201305UV02 searchRequest = createPRPAMT201301UVPatient(patientSearchCriteria);

        AdapterComponentMpiProxy mpiProxy = getAdapterComponentMpiProxy();

        AssertionCreator oAssertCreator = new AssertionCreator();
        AssertionType oAssertion = oAssertCreator.createAssertion();
        PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchRequest, oAssertion);

        Patients mpiPatients = convertPRPAIN201306UVToPatients(patients);

        patientVOs = convertMPIPatientToPatientVO(mpiPatients);

        return patientVOs;
    }

    /**
     * 
     * @param patientSearchCriteria
     * @return
     */
    private PRPAIN201305UV02 createPRPAMT201301UVPatient(PatientSearchCriteria patientSearchCriteria) {

        II patId = new II();
        patId.setExtension(patientSearchCriteria.getPatientID());
        patId.setRoot(patientSearchCriteria.getAssigningAuthorityID());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(patientSearchCriteria.getFirstName(), patientSearchCriteria.getLastName(), null, null, null), patId);
        PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getAssigningAuthorityID());
        return searchPat;
    }

    /**
     * 
     * @return
     */
    private AdapterComponentMpiProxy getAdapterComponentMpiProxy() {
        AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
        AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
        return mpiProxy;
    }

    /**
     * 
     * @param patients
     * @return
     */
    private Patients convertPRPAIN201306UVToPatients(PRPAIN201306UV02 patients) {
        Patients mpiPatients = new Patients();
        Patient searchPatient = new Patient();

        if (patients != null &&
                patients.getControlActProcess() != null &&
                NullChecker.isNotNullish(patients.getControlActProcess().getSubject()) &&
                patients.getControlActProcess().getSubject().get(0) != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
            PRPAMT201310UV02Patient mpiPatResult = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();

            if (NullChecker.isNotNullish(mpiPatResult.getId()) &&
                    mpiPatResult.getId().get(0) != null &&
                    NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(), mpiPatResult.getId().get(0).getRoot());
            }

            if (mpiPatResult.getPatientPerson() != null &&
                    mpiPatResult.getPatientPerson().getValue() != null &&
                    mpiPatResult.getPatientPerson().getValue().getName() != null) {
                PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue().getName());
                PersonName personName = new PersonName();
                personName.setFirstName(name.getGivenName());
                personName.setLastName(name.getFamilyName());
                searchPatient.setName(personName);
            }

            mpiPatients.add(searchPatient);
        }

        return mpiPatients;
    }

    /**
     *
     * @return
     */
    private List<PatientVO> convertMPIPatientToPatientVO(Patients mpiPatients) throws Exception
    {
        List<PatientVO> patientVOs = new ArrayList<PatientVO>();

        if (mpiPatients == null || mpiPatients.size() < 1) {
            return patientVOs;
        }

        String assigningAuthId = PropertyAccessor.getProperty("adapter", "assigningAuthorityId");


        for (Patient patient : mpiPatients)
        {
            PatientVO patientVO = new PatientVO();

            PersonName name = patient.getName();

            if (name != null) {
                patientVO.setFirstName(name.getFirstName());
                patientVO.setLastName(name.getLastName());
            }

            for (Identifier id : patient.getIdentifiers())
            {
                if (id.getOrganizationId().equals(assigningAuthId)) 
                {
                    patientVO.setAssigningAuthorityID(assigningAuthId);
                    patientVO.setPatientID(id.getId());
                    patientVO.setOrganizationID(id.getOrganizationId());
                }
            }

            patientVOs.add(patientVO);
        }

        return patientVOs;
    }
}