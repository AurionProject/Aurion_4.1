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
package universalclientgui;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunityType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.entitypatientdiscovery.EntityPatientDiscovery;
import org.alembic.aurion.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import org.alembic.aurion.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import org.alembic.aurion.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.subdisc.HL7PRPA201305Transforms;
import org.alembic.aurion.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author patlollav
 */
public class PatientDiscoveryClient {

    private static final String PROPERTY_FILE_NAME_ADAPTER = "adapter";
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(PatientDiscoveryClient.class);
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    /**
     * Entity Patient Discovery service
     */
    private static EntityPatientDiscovery service = new EntityPatientDiscovery();

    /**
     *
     * @param url
     * @return
     */
    private EntityPatientDiscoveryPortType getPort(String url) {

        if (service == null) {
            service = new EntityPatientDiscovery();
        }

        EntityPatientDiscoveryPortType port = service.getEntityPatientDiscoveryPortSoap();

        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    /**
     *
     * @return
     */
    private String getEntityPatientDiscoveryEndPointAddress() {

        String endpointAddress = null;

        try {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
            endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME);
            log.debug("Entity Patient Discovery endpoint address: " + endpointAddress);
        } catch (PropertyAccessException pae) {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        } catch (Exception cme) {
            log.error("Exception encountered retrieving the entity doc query connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }

    /**
     * Retrieve the local home community id
     *
     * @return Local home community id
     * @throws org.alembic.aurion.properties.PropertyAccessException
     */
    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_LOCAL_HOME_COMMUNITY);
    }

    /**
     *
     * @param assertion
     * @param patientSearchData
     */
    public void broadcastPatientDiscovery(AssertionType assertion, PatientSearchData patientSearchData) {

        try {

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setAssertion(assertion);

            String localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_LOCAL_DEVICE);
            String orgId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);


            PRPAIN201305UV02 request201305 = this.create201305(patientSearchData, orgId);

            request.setPRPAIN201305UV02(request201305);
            
            EntityPatientDiscoveryPortType port = getPort(getEntityPatientDiscoveryEndPointAddress());

            RespondingGatewayPRPAIN201306UV02ResponseType response = port.respondingGatewayPRPAIN201305UV02(request);
        
        } catch (PropertyAccessException ex) {
            log.error("Exception in patient discovery", ex);
        }
    }

    /**
     * 
     * @param first
     * @param last
     * @param gender
     * @param birthdate
     * @param ssn
     * @param senderOID
     * @param receiverOID
     * @return
     */
    public PRPAIN201305UV02 create201305(PatientSearchData patientSearchData, String receiverOID) {
        PRPAIN201305UV02 resp = new PRPAIN201305UV02();

        String localDeviceId = null;

        try {
            localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_LOCAL_DEVICE);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(PatientDiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientSearchData.getFirstName(), patientSearchData.getLastName(), patientSearchData.getGender(), patientSearchData.getDob(), patientSearchData.getSsn());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientSearchData.getPatientId(), localDeviceId);

        // We should have this - but lets be sure.
        //-----------------------------------------
        if ((patient != null) &&
            (patient.getPatientPerson() != null) &&
            (patient.getPatientPerson().getValue() != null))
        {
            retrieveAndFillInAddrAndPhone(patientSearchData, patient.getPatientPerson().getValue());
        }

        resp = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchData.getAssigningAuthorityID(), receiverOID, localDeviceId);
        return resp;
    }

    /**
     * This method is a quick fix and is used to retrieve the patient data again from the system so that we have all of the
     * patient data to be inlcuded in the patient discovery request.  Otherwise it only sends the last name. first name, DOB,
     * and gender.
     *
     * @param patientSearchData The patient search data that we know.
     * @return The rest of the patient search data.
     */
    private void retrieveAndFillInAddrAndPhone(PatientSearchData patientSearchData, PRPAMT201301UV02Person person)
    {
        log.debug("begin retrieveAndFillInAddrAndPhone.");
        if (patientSearchData == null)
        {
            return;
        }

        String firstName = null;
        if ((patientSearchData.getFirstName() != null) &&
            (patientSearchData.getFirstName().length() > 0))
        {
            firstName = patientSearchData.getFirstName();
        }

        String lastName = null;
        if ((patientSearchData.getLastName() != null) &&
            (patientSearchData.getLastName().length() > 0))
        {
            lastName = patientSearchData.getLastName();
        }

        String gender = "";
        if ((patientSearchData.getGender() != null) &&
            (patientSearchData.getGender().length() > 0))
        {
            gender = patientSearchData.getGender();
        }

        String birthTime = "";
        if ((patientSearchData.getDob() != null) &&
            (patientSearchData.getDob().length() > 0))
        {
            birthTime = patientSearchData.getDob();
        }

        try
        {
            String assigningAuthId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_ADAPTER, PROPERTY_FILE_KEY_ASSIGN_AUTH);
            String orgId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);

            II patId = new II();
            patId.setRoot(assigningAuthId);
            PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(firstName, lastName, gender, birthTime, null), patId);
            PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

            AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
            AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
            AssertionCreator assertionCreator = new AssertionCreator();
            AssertionType oAssertion = assertionCreator.createAssertion();
            PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchPat, oAssertion);

            List<PRPAMT201310UV02Patient> mpiPatResultList = new ArrayList<PRPAMT201310UV02Patient>();
            if ((patients != null) &&
                (patients.getControlActProcess() != null) &&
                (patients.getControlActProcess().getSubject() != null))
            {
                List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjectList = patients.getControlActProcess().getSubject();
                log.debug("Search MPI found " + subjectList.size() + " candidates");
                for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject1 : subjectList) {
                    if ((subject1 != null) &&
                        (subject1.getRegistrationEvent() != null) &&
                        (subject1.getRegistrationEvent().getSubject1() != null) &&
                        (subject1.getRegistrationEvent().getSubject1().getPatient() != null))
                    {
                        PRPAMT201310UV02Patient mpiPat = subject1.getRegistrationEvent().getSubject1().getPatient();
                        mpiPatResultList.add(mpiPat);
                    }
                }
                // Because of the way we are searching there should be exactly one match.  Lets just make sure that
                // is the case.
                //-------------------------------------------------------------------------------------------------
                if (mpiPatResultList.size() == 1)
                {
                    PRPAMT201310UV02Patient patient201310 = mpiPatResultList.get(0);
                    if ((patient201310 != null) &&
                        (patient201310.getPatientPerson() != null) &&
                        (patient201310.getPatientPerson().getValue() != null))
                    {
                        log.debug("Found a '201310' PatientPerson object.");
                        PRPAMT201310UV02Person person201310 = patient201310.getPatientPerson().getValue();
                        if (person201310.getAddr().size() > 0)
                        {
                            log.debug("Setting search address.");
                            person.getAddr().clear();
                            person.getAddr().addAll(person201310.getAddr());
                        }
                        else
                        {
                            log.debug("There was no search address");
                        }

                        if (person201310.getTelecom().size() > 0)
                        {
                            log.debug("Setting search telephone.");
                            person.getTelecom().clear();
                            person.getTelecom().addAll(person201310.getTelecom());
                        }
                        else
                        {
                            log.debug("There was no search telephone.");
                        }
                    }
                    else
                    {
                        log.debug("There was no '201310' PatientPerson object.");
                    }
                }
                else
                {
                    log.error("When re-retrieving the patient data to fill in the 201305 - we found more than one" +
                              " result when we should have only found 1.");
                }
            }

        }
        catch (Exception e)
        {
            log.error("When re-retrieving the patient data to fill in the 201305 - we received an unexpected exception: " +
                      e.getMessage(), e);
        }
        return;
    }

    /**
     *
     * @param first
     * @param last
     * @param gender
     * @param birthdate
     * @param ssn
     * @return
     */
    private PRPAMT201301UV02Patient createPatient(String first, String last, String gender, String birthdate, String ssn) {
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(first, last, gender, birthdate, ssn);
        II id = new II();

        patient = HL7PatientTransforms.create201301Patient(person, id);

        return patient;
    }


//    /**
//     * This will be removed once the broadcast PD is implented
//     */
//    private NhinTargetCommunitiesType createNhinTargetCommunities(){
//        NhinTargetCommunitiesType nhinTargetCommunities = new NhinTargetCommunitiesType();
//        NhinTargetCommunityType nhinTargetCommunity = new NhinTargetCommunityType();
//        nhinTargetCommunity.setList("List");
//        nhinTargetCommunity.setRegion("Region");
//
//        HomeCommunityType homeCommunity = new HomeCommunityType();
//        homeCommunity.setDescription("Description");
//        homeCommunity.setHomeCommunityId("2.2");
//
//        nhinTargetCommunity.setHomeCommunity(homeCommunity);
//        nhinTargetCommunities.getNhinTargetCommunity().add(nhinTargetCommunity);
//
//        return nhinTargetCommunities;
//    }

}
