/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.transform.subdisc;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01RespondTo;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.ST;
import org.hl7.v3.TSExplicit;
import org.junit.Before;
import org.junit.Test;

/**
 * The HL7PRPA201305TransformsTest class provides a set of test cases for the 
 * HL7PRPA201305Transforms class.
 * 
 * @author Michael Wooten
 * 
 * @see HL7PRPA201305Transforms
 */
public class HL7PRPA201305TransformsTest {

	/**
	 * The request that will be used in the clone tests.
	 */
	private PRPAIN201305UV02 theRequest;

	/**
	 * Initializes the HL7 201305 request used in the tests.
	 */
	@Before
	public void setUp() {
		theRequest = new PRPAIN201305UV02();

		theRequest.setAcceptAckCode(mock(CS.class));
		theRequest.getAttachmentText().addAll(
				asList(mock(EDExplicit.class), mock(EDExplicit.class)));
		theRequest.getAttentionLine().addAll(
				asList(mock(MCCIMT000100UV01AttentionLine.class), mock(MCCIMT000100UV01AttentionLine.class)));
		theRequest.setControlActProcess(mock(PRPAIN201305UV02QUQIMT021001UV01ControlActProcess.class));
		theRequest.setCreationTime(mock(TSExplicit.class));
		theRequest.setId(mock(II.class));
		theRequest.setInteractionId(mock(II.class));
		theRequest.setITSVersion("version");
		theRequest.getNullFlavor().addAll(asList("value1", "value2"));
		theRequest.setProcessingCode(mock(CS.class));
		theRequest.setProcessingModeCode(mock(CS.class));
		theRequest.getProfileId().addAll(asList(mock(II.class), mock(II.class)));
		theRequest.getRealmCode().addAll(asList(mock(CS.class), mock(CS.class)));
		theRequest.getReceiver().add(mock(MCCIMT000100UV01Receiver.class));
		theRequest.getRespondTo().add(mock(MCCIMT000100UV01RespondTo.class));
		theRequest.setSecurityText(mock(ST.class));
		theRequest.setSender(mock(MCCIMT000100UV01Sender.class));
		theRequest.setSequenceNumber(mock(INT.class));
		theRequest.getTemplateId().addAll(asList(mock(II.class), mock(II.class)));
		theRequest.setTypeId(mock(II.class));
		theRequest.setVersionCode(mock(CS.class));
	}

	/**
	 * Test method for
	 * {@link org.alembic.aurion.transform.subdisc.HL7PRPA201305Transforms#clonePRPA201305(org.hl7.v3.PRPAIN201305UV02)}
	 * .
	 */
	@Test
	public void testClonePRPA201305() {
		PRPAIN201305UV02 clone = HL7PRPA201305Transforms
				.clonePRPA201305(theRequest);

		// Assert that the clone is not the same object
		assertNotSame(theRequest, clone);

		// Assert that the single value fields are equivalent
		assertEquals(theRequest.getAcceptAckCode(), clone.getAcceptAckCode());
		assertEquals(theRequest.getControlActProcess(), clone.getControlActProcess());
		assertEquals(theRequest.getCreationTime(), clone.getCreationTime());
		assertEquals(theRequest.getId(), clone.getId());
		assertEquals(theRequest.getInteractionId(), clone.getInteractionId());
		assertEquals(theRequest.getITSVersion(), clone.getITSVersion());
		assertEquals(theRequest.getProcessingCode(), clone.getProcessingCode());
		assertEquals(theRequest.getProcessingModeCode(), clone.getProcessingModeCode());
		assertEquals(theRequest.getSecurityText(), clone.getSecurityText());
		assertEquals(theRequest.getSender(), clone.getSender());
		assertEquals(theRequest.getSequenceNumber(), clone.getSequenceNumber());
		assertEquals(theRequest.getTypeId(), clone.getTypeId());
		assertEquals(theRequest.getVersionCode(), clone.getVersionCode());
		
		// Assert that all of the lists are shallow copies
		assertShallowCopy(theRequest.getAttachmentText(), clone.getAttachmentText());
		assertShallowCopy(theRequest.getAttentionLine(), clone.getAttentionLine());
		assertShallowCopy(theRequest.getNullFlavor(), clone.getNullFlavor());
		assertShallowCopy(theRequest.getProfileId(), clone.getProfileId());
		assertShallowCopy(theRequest.getRealmCode(), clone.getRealmCode());		
		assertShallowCopy(theRequest.getReceiver(), clone.getReceiver());
		assertShallowCopy(theRequest.getRespondTo(), clone.getRespondTo());
		assertShallowCopy(theRequest.getTemplateId(), clone.getTemplateId());
	}

	/**
	 * Test method for
	 * {@link org.alembic.aurion.transform.subdisc.HL7PRPA201305Transforms#clonePRPA201305(org.hl7.v3.PRPAIN201305UV02)}
	 * that ensures a {@link NullPointerException} is thrown for a
	 * <code>null</code> request.
	 */
	@Test(expected = NullPointerException.class)
	public void testClonePRPA201305ThrowsNPEForNullRequest() {
		HL7PRPA201305Transforms.clonePRPA201305(null);
	}

	/**
	 * Asserts that two lists are not the same object but contain the same contents.
	 * 
	 * @param originalList the original list that was copied.
	 * @param copiedList the shallow copy of the original list.
	 */
	private void assertShallowCopy(List<?> originalList, List<?> copiedList) {
		assertNotNull(originalList);
		assertNotNull(copiedList);
		assertNotSame(originalList, copiedList);
		assertEquals(originalList, copiedList);
	}
}
