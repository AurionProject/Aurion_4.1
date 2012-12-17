/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.PersonNameType;
import org.alembic.aurion.common.auditlog.LogDocRetrieveRequestType;
import org.alembic.aurion.common.auditlog.DocRetrieveMessageType;
import org.alembic.aurion.common.auditlog.LogDocRetrieveResultRequestType;
import org.alembic.aurion.common.auditlog.DocRetrieveResponseMessageType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.alembic.aurion.common.nhinccommon.UserType;

/**
 *
 * @author mflynn02
 */
public class DocumentRetrieveTransformsTest {
    private static Log log = LogFactory.getLog(DocumentRetrieveTransformsTest.class);

    public DocumentRetrieveTransformsTest() {
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
     * Test of transformDocRetrieveReq2AuditMsg method, of class DocumentRetrieveTransforms.
     */
    @Test
    public void testTransformDocRetrieveReq2AuditMsg() {
        log.debug("Begin - testTransformDocRetrieveReq2AuditMsg");
        LogDocRetrieveRequestType logMessage = new LogDocRetrieveRequestType();
        DocRetrieveMessageType docReqMessage = new DocRetrieveMessageType();
        AssertionType assertion = new AssertionType();
        
        RetrieveDocumentSetRequestType message = new RetrieveDocumentSetRequestType();
        DocumentRequest docReq = new DocumentRequest();
        docReq.setDocumentUniqueId("123-45-6789");
        docReq.setHomeCommunityId("2.16.840.1.113883.3.200");
        message.getDocumentRequest().add(docReq);
        
        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Presley");
        person.setGivenName("Elvis");
        userInfo.setPersonName(person);
        assertion.setUserInfo(userInfo);
        
        docReqMessage.setAssertion(assertion);
        docReqMessage.setRetrieveDocumentSetRequest(message);
        logMessage.setMessage(docReqMessage);
        
        AuditData auditData = new AuditData();
        auditData.setReceiverPatientId("999999");
        auditData.setMessageType("RetrieveDocumentSetRequest");
        
        AuditMessageType expResult = new AuditMessageType();
        
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName(person.getGivenName() + " " + person.getFamilyName());
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(sourceId);
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        partObjId.setParticipantObjectID(auditData.getReceiverPatientId() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_READ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        LogEventRequestType result = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logMessage);
        
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
//        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(), result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
        log.debug("Begin - testTransformDocRetrieveReq2AuditMsg");
    }

    /**
     * Test of transformDocRetrieveResp2AuditMsg method, of class DocumentRetrieveTransforms.
     */
    @Test
    public void testTransformDocRetrieveResp2AuditMsg() {
        log.debug("Begin - testTransformDocRetrieveResp2AuditMsg");
        
        LogDocRetrieveResultRequestType logMessage = new LogDocRetrieveResultRequestType();
        DocRetrieveResponseMessageType docRespMessage = new DocRetrieveResponseMessageType();
        AssertionType assertion = new AssertionType();
        
        RetrieveDocumentSetResponseType message = new RetrieveDocumentSetResponseType();
        DocumentRequest docReq = new DocumentRequest();
        docReq.setDocumentUniqueId("987-06543-021");
        docReq.setHomeCommunityId("2.16.840.1.113883.3.166.4");
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("DocRetrieve Response Status");
        message.setRegistryResponse(resp);
        
        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.166.4");
        home.setName("CareSpark");
        userInfo.setOrg(home);
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Jackson");
        person.setGivenName("Andrew");
        userInfo.setPersonName(person);
        assertion.setUserInfo(userInfo);
        
        docRespMessage.setAssertion(assertion);
        docRespMessage.setRetrieveDocumentSetResponse(message);
        logMessage.setMessage(docRespMessage);
        
        AuditData auditData = new AuditData();
        auditData.setReceiverPatientId("78987");
        auditData.setMessageType("RetrieveDocumentSetResponse");
        
        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName(person.getGivenName() + " " + person.getFamilyName());
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(sourceId);
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        partObjId.setParticipantObjectID(auditData.getReceiverPatientId() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_READ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logMessage, Boolean.FALSE);
        
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
//        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(), result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
        log.debug("End - testTransformDocRetrieveResp2AuditMsg");
    }

}