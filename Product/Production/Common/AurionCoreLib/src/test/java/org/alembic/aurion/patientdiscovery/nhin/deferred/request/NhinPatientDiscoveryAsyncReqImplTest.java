package org.alembic.aurion.patientdiscovery.nhin.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.patientdiscovery.testhelper.TestHelper;
import org.alembic.aurion.transform.subdisc.HL7AckTransforms;
import org.alembic.aurion.transform.subdisc.HL7PRPA201305Transforms;
import org.alembic.aurion.transform.subdisc.HL7PRPA201306Transforms;
import org.alembic.aurion.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
 @Ignore
public class NhinPatientDiscoveryAsyncReqImplTest {

    public NhinPatientDiscoveryAsyncReqImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02() {
        System.out.println("testRespondingGatewayPRPAIN201305UV02");

        NhinPatientDiscoveryDeferredReqOrchImpl instance = new NhinPatientDiscoveryDeferredReqOrchImpl() {

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

                return resp;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected boolean isInPassThroughMode() {
                return false;
            }

            @Override
            protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessAgency");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessQueue");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
                return HL7AckTransforms.createAckFrom201305(request, errMsg);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgEquals("SuccessQueue", result);
    }

    /**
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02PolicyFailed() {
        System.out.println("testRespondingGatewayPRPAIN201305UV02PolicyFailed");

        NhinPatientDiscoveryDeferredReqOrchImpl instance = new NhinPatientDiscoveryDeferredReqOrchImpl() {

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

                return resp;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected boolean isInPassThroughMode() {
                return false;
            }

            @Override
            protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessAgency");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessQueue");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
                return HL7AckTransforms.createAckFrom201305(request, errMsg);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return false;
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgEquals("Policy Check Failed", result);
    }

    /**
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02Passthru() {
        System.out.println("testRespondingGatewayPRPAIN201305UV02Passthru");

        NhinPatientDiscoveryDeferredReqOrchImpl instance = new NhinPatientDiscoveryDeferredReqOrchImpl() {

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

                return resp;
            }

            @Override
            protected boolean isServiceEnabled() {
                return true;
            }

            @Override
            protected boolean isInPassThroughMode() {
                return true;
            }

            @Override
            protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessAgency");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessQueue");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
                return HL7AckTransforms.createAckFrom201305(request, errMsg);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgEquals("SuccessAgency", result);
    }

        /**
     * Test of respondingGatewayPRPAIN201305UV02 method, of class NhinPatientDiscoveryAsyncReqImpl.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02ServiceEnabled() {
        System.out.println("testRespondingGatewayPRPAIN201305UV02ServiceEnabled");

        NhinPatientDiscoveryDeferredReqOrchImpl instance = new NhinPatientDiscoveryDeferredReqOrchImpl() {

            @Override
            protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
                JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
                PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
                PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

                return resp;
            }

            @Override
            protected boolean isServiceEnabled() {
                return false;
            }

            @Override
            protected boolean isInPassThroughMode() {
                return false;
            }

            @Override
            protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessAgency");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
                return HL7AckTransforms.createAckFrom201305(request, "SuccessQueue");
            }

            @Override
            protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
                return HL7AckTransforms.createAckFrom201305(request, errMsg);
            }

            @Override
            protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
                return true;
            }
        };

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 request = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        AssertionType assertion = new AssertionType();

        MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);

        assertNotNull(result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgEquals("Patient Discovery Async Request Service Not Enabled", result);
    }
}
