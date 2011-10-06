package org.alembic.aurion.saml.extraction;

import com.sun.xml.ws.security.opt.impl.incoming.SAMLAssertion;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.ByteArrayOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Neil Webb
 */
public class RawSamlAssertionUtilTest {
    private static final String SAML_ASSERTION_RAW_NS1 = "</saml2:AttributeStatement></saml2:Assertion>";
    private static final String SAML_ASSERTION_CORRECT_NS1 = "</saml2:AttributeStatement></saml2:Assertion></saml2:Evidence></saml2:AuthzDecisionStatement></saml2:Assertion>";
    private static final String SAML_ASSERTION_RAW_NS2 = "</sml:AttributeStatement></sml:Assertion>";
    private static final String SAML_ASSERTION_CORRECT_NS2 = "</sml:AttributeStatement></sml:Assertion></sml:Evidence></sml:AuthzDecisionStatement></sml:Assertion>";
    private static final String SAML_ASSERTION_FULL_RAW = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:exc14n=\"http://www.w3.org/2001/10/xml-exc-c14n#\" xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsse11=\"http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" ID=\"1998e8a1a72542d48232127a625c2913\" IssueInstant=\"2011-09-21T18:55:24.937Z\" Version=\"2.0\"><saml2:Issuer Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US</saml2:Issuer><saml2:Subject><saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">UID=kskagerb</saml2:NameID><saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:holder-of-key\"><saml2:SubjectConfirmationData><ds:KeyInfo>" +
        "<ds:KeyValue>" +
        "<ds:RSAKeyValue><ds:Modulus>vYxVZKIzVdGMSBkW4bYnV80MV/RgQKV1bf/DoMTX8laMO45P6rlEarxQiOYrgzuYp+snzz2XM0S6o3JGQtXQuzDwcwPkH55bHFwHgtOMzxG4SQ653a5Dzh04nsmJvxvbncNH/XNaWfHaC0JHBEfNCMwRebYocxYM92pq/G5OGyE=</ds:Modulus><ds:Exponent>AQAB</ds:Exponent></ds:RSAKeyValue>" +
        "</ds:KeyValue>" +
        "</ds:KeyInfo></saml2:SubjectConfirmationData></saml2:SubjectConfirmation></saml2:Subject><saml2:AuthnStatement AuthnInstant=\"2009-04-16T13:15:39.000Z\" SessionIndex=\"987\"><saml2:SubjectLocality Address=\"158.147.185.168\" DNSName=\"cs.myharris.net\" /><saml2:AuthnContext><saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:X509</saml2:AuthnContextClassRef></saml2:AuthnContext></saml2:AuthnStatement><saml2:AttributeStatement><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:organization\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">InternalTest2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:organization-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">2.2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:nhin:names:saml:homeCommunityId\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">2.2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xacml:2.0:subject:role\"><saml2:AttributeValue><hl7:Role xmlns:hl7=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" code=\"307969004\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED_CT\" displayName=\"Public Health\" xsi:type=\"hl7:CE\" /></saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\"><saml2:AttributeValue><hl7:PurposeForUse xmlns:hl7=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" code=\"PUBLICHEALTH\" codeSystem=\"2.16.840.1.113883.3.18.7.1\" codeSystemName=\"nhin-purpose\" displayName=\"Use or disclosure of Psychotherapy Notes\" xsi:type=\"hl7:CE\" /></saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xacml:2.0:resource:resource-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">500000000^^^&amp;1.1&amp;ISO</saml2:AttributeValue></saml2:Attribute></saml2:AttributeStatement><saml2:AuthzDecisionStatement Decision=\"Permit\" Resource=\"https://localhost:8181/CONNECTGateway/GatewayService/AuditRepositoryManagerSecuredService\"><saml2:Action Namespace=\"urn:oasis:names:tc:SAML:1.0:action:rwedc\">Execute</saml2:Action><saml2:Evidence><saml2:Assertion ID=\"40df7c0a-ff3e-4b26-baeb-f2910f6d05a9\" IssueInstant=\"2009-04-16T13:10:39.093Z\" Version=\"2.0\"><saml2:Issuer Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">CN=SAML User,OU=Harris,O=HITS,L=Melbourne,ST=FL,C=US</saml2:Issuer><saml2:Conditions NotBefore=\"2009-04-16T13:10:39.093Z\" NotOnOrAfter=\"2009-12-31T12:00:00.000Z\" /><saml2:AttributeStatement><saml2:Attribute Name=\"AccessConsentPolicy\" NameFormat=\"http://www.hhs.gov/healthit/nhin\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Claim-Ref-1234</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"InstanceAccessConsentPolicy\" NameFormat=\"http://www.hhs.gov/healthit/nhin\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Claim-Instance-1</saml2:AttributeValue></saml2:Attribute></saml2:AttributeStatement></saml2:Assertion>";
    private static final String SAML_ASSERTION_FULL_CORRECTED = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:exc14n=\"http://www.w3.org/2001/10/xml-exc-c14n#\" xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsse11=\"http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" ID=\"1998e8a1a72542d48232127a625c2913\" IssueInstant=\"2011-09-21T18:55:24.937Z\" Version=\"2.0\"><saml2:Issuer Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US</saml2:Issuer><saml2:Subject><saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">UID=kskagerb</saml2:NameID><saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:holder-of-key\"><saml2:SubjectConfirmationData><ds:KeyInfo>" +
        "<ds:KeyValue>" +
        "<ds:RSAKeyValue><ds:Modulus>vYxVZKIzVdGMSBkW4bYnV80MV/RgQKV1bf/DoMTX8laMO45P6rlEarxQiOYrgzuYp+snzz2XM0S6o3JGQtXQuzDwcwPkH55bHFwHgtOMzxG4SQ653a5Dzh04nsmJvxvbncNH/XNaWfHaC0JHBEfNCMwRebYocxYM92pq/G5OGyE=</ds:Modulus><ds:Exponent>AQAB</ds:Exponent></ds:RSAKeyValue>" +
        "</ds:KeyValue>" +
        "</ds:KeyInfo></saml2:SubjectConfirmationData></saml2:SubjectConfirmation></saml2:Subject><saml2:AuthnStatement AuthnInstant=\"2009-04-16T13:15:39.000Z\" SessionIndex=\"987\"><saml2:SubjectLocality Address=\"158.147.185.168\" DNSName=\"cs.myharris.net\" /><saml2:AuthnContext><saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:X509</saml2:AuthnContextClassRef></saml2:AuthnContext></saml2:AuthnStatement><saml2:AttributeStatement><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:organization\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">InternalTest2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:organization-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">2.2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:nhin:names:saml:homeCommunityId\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">2.2</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xacml:2.0:subject:role\"><saml2:AttributeValue><hl7:Role xmlns:hl7=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" code=\"307969004\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED_CT\" displayName=\"Public Health\" xsi:type=\"hl7:CE\" /></saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\"><saml2:AttributeValue><hl7:PurposeForUse xmlns:hl7=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" code=\"PUBLICHEALTH\" codeSystem=\"2.16.840.1.113883.3.18.7.1\" codeSystemName=\"nhin-purpose\" displayName=\"Use or disclosure of Psychotherapy Notes\" xsi:type=\"hl7:CE\" /></saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"urn:oasis:names:tc:xacml:2.0:resource:resource-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">500000000^^^&amp;1.1&amp;ISO</saml2:AttributeValue></saml2:Attribute></saml2:AttributeStatement><saml2:AuthzDecisionStatement Decision=\"Permit\" Resource=\"https://localhost:8181/CONNECTGateway/GatewayService/AuditRepositoryManagerSecuredService\"><saml2:Action Namespace=\"urn:oasis:names:tc:SAML:1.0:action:rwedc\">Execute</saml2:Action><saml2:Evidence><saml2:Assertion ID=\"40df7c0a-ff3e-4b26-baeb-f2910f6d05a9\" IssueInstant=\"2009-04-16T13:10:39.093Z\" Version=\"2.0\"><saml2:Issuer Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName\">CN=SAML User,OU=Harris,O=HITS,L=Melbourne,ST=FL,C=US</saml2:Issuer><saml2:Conditions NotBefore=\"2009-04-16T13:10:39.093Z\" NotOnOrAfter=\"2009-12-31T12:00:00.000Z\" /><saml2:AttributeStatement><saml2:Attribute Name=\"AccessConsentPolicy\" NameFormat=\"http://www.hhs.gov/healthit/nhin\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Claim-Ref-1234</saml2:AttributeValue></saml2:Attribute><saml2:Attribute Name=\"InstanceAccessConsentPolicy\" NameFormat=\"http://www.hhs.gov/healthit/nhin\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Claim-Instance-1</saml2:AttributeValue></saml2:Attribute></saml2:AttributeStatement></saml2:Assertion></saml2:Evidence></saml2:AuthzDecisionStatement></saml2:Assertion>";

    private RawSamlAssertionUtil testSubject = null;
    private WebServiceContext mockContext = null;
    private Log mockLog = null;
    private MessageContext mockMsgContext = null;
    private SAMLAssertion mockSamlAssertion = null;
    private XMLOutputFactory mockXMLOutputFactory = null;
    private XMLStreamWriter mockXMLStreamWriter = null;
    private ByteArrayOutputStream mockByteArrayOutputStream = null;

    @Before
    public void setUp() throws Exception {
        mockContext = mock(WebServiceContext.class);
        mockLog = mock(Log.class);
        mockMsgContext = mock(MessageContext.class);
        mockSamlAssertion = mock(SAMLAssertion.class);
        mockXMLOutputFactory = mock(XMLOutputFactory.class);
        mockXMLStreamWriter = mock(XMLStreamWriter.class);
        mockByteArrayOutputStream = mock(ByteArrayOutputStream.class);

        testSubject = new RawSamlAssertionUtil(mockContext) {
            @Override
            protected Log createLogger() {
                return mockLog;
            }
            @Override
            protected XMLOutputFactory createXMLOutputFactory() {
                return mockXMLOutputFactory;
            }
            @Override
            protected ByteArrayOutputStream createByteArrayOutputStream() {
                return mockByteArrayOutputStream;
            }
        };

        when(mockContext.getMessageContext()).thenReturn(mockMsgContext);
        when(mockMsgContext.get(anyString())).thenReturn(mockSamlAssertion);
        when(mockXMLOutputFactory.createXMLStreamWriter(any(ByteArrayOutputStream.class))).thenReturn(mockXMLStreamWriter);
    }

    @Test
    public void testRepairAssertionMinimalAssertionNS1() {
        when(mockByteArrayOutputStream.toString()).thenReturn(SAML_ASSERTION_RAW_NS1);
        String rawAssertion = testSubject.captureRawSAMLAssertion();
        assertEquals(SAML_ASSERTION_CORRECT_NS1, rawAssertion);
    }

    @Test
    public void testRepairAssertionMinimalAssertionNS2() {
        when(mockByteArrayOutputStream.toString()).thenReturn(SAML_ASSERTION_RAW_NS2);
        String rawAssertion = testSubject.captureRawSAMLAssertion();
        assertEquals(SAML_ASSERTION_CORRECT_NS2, rawAssertion);
    }

    @Test
    public void testRepairAssertionFullAssertion() {
        when(mockByteArrayOutputStream.toString()).thenReturn(SAML_ASSERTION_FULL_RAW);
        String rawAssertion = testSubject.captureRawSAMLAssertion();
        assertEquals(SAML_ASSERTION_FULL_CORRECTED, rawAssertion);
    }

    @Test
    public void testNoRepairAssertionFullAssertion() {
        when(mockByteArrayOutputStream.toString()).thenReturn(SAML_ASSERTION_FULL_CORRECTED);
        String rawAssertion = testSubject.captureRawSAMLAssertion();
        assertEquals(SAML_ASSERTION_FULL_CORRECTED, rawAssertion);
    }

}