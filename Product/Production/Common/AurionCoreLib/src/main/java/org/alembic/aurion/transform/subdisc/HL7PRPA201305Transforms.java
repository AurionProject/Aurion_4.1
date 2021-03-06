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

package org.alembic.aurion.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.alembic.aurion.nhinclib.NullChecker;
import org.hl7.v3.*;
        
/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201305Transforms {

    public static PRPAIN201305UV02 createPRPA201305 (PRPAMT201301UV02Patient patient, String senderOID, String receiverOID, String localDeviceId) {
        PRPAIN201305UV02 result = new PRPAIN201305UV02();
        
        // Create the 201305 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        result.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201305UV02"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));
        
        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));
        
        result.setControlActProcess(createQUQIMT021001UV01ControlActProcess(patient, localDeviceId));
        
        return result;
    }
    
    public static PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createQUQIMT021001UV01ControlActProcess (PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess  controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        
        controlActProcess.setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201305UV", HL7Constants.INTERACTION_ID_ROOT));
        
        controlActProcess.setQueryByParameter(HL7QueryParamsTransforms.createQueryParams(patient, localDeviceId));

        QUQIMT021001UV01AuthorOrPerformer authorOrPerformer = new QUQIMT021001UV01AuthorOrPerformer();
        authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);
        
        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        id.setRoot(localDeviceId);
        
        assignedDevice.getId().add(id);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = new JAXBElement<COCTMT090300UV01AssignedDevice>(xmlqname, COCTMT090300UV01AssignedDevice.class, assignedDevice);
        
        authorOrPerformer.setAssignedDevice(assignedDeviceJAXBElement);

        controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);

        return controlActProcess;
    }

    /**
     * Creates a clone (shallow copy) of the provided HL7 201305 request message. 
     * 
     * @param request the HL7 201305 message to be cloned (must not be <code>null</code>).
     * 
     * @return a shallow copy of the provided HL7 201305 message. 
     * 
     * @throws NullPointerException if <code>request</code> is <code>null</code>.
     */
    public static PRPAIN201305UV02 clonePRPA201305 (PRPAIN201305UV02 request) {
        
    	if (request == null) {
    		throw new NullPointerException("request argument cannot be null");
    	}
    	
    	PRPAIN201305UV02 newRequest = new PRPAIN201305UV02();

        // Clone the non-list fields of the 201305 first
        if (request.getAcceptAckCode() != null) {
            newRequest.setAcceptAckCode(request.getAcceptAckCode());
        }

        if (request.getControlActProcess() != null) {
            newRequest.setControlActProcess(request.getControlActProcess());
        }

        if (request.getCreationTime() != null) {
            newRequest.setCreationTime(request.getCreationTime());
        }

        if (request.getITSVersion() != null) {
            newRequest.setITSVersion(request.getITSVersion());
        }

        if (request.getId() != null) {
            newRequest.setId(request.getId());
        }

        if (request.getInteractionId() != null) {
            newRequest.setInteractionId(request.getInteractionId());
        }

        if (request.getProcessingCode() != null) {
            newRequest.setProcessingCode(request.getProcessingCode());
        }

        if (request.getProcessingModeCode() != null) {
            newRequest.setProcessingModeCode(request.getProcessingModeCode());
        }

        if (request.getSecurityText() != null) {
            newRequest.setSecurityText(request.getSecurityText());
        }

        if (request.getSender() != null) {
            newRequest.setSender(request.getSender());
        }

        if (request.getSequenceNumber() != null) {
            newRequest.setSequenceNumber(request.getSequenceNumber());
        }

        if (request.getTypeId() != null) {
            newRequest.setTypeId(request.getTypeId());
        }

        if (request.getVersionCode() != null) {
            newRequest.setVersionCode(request.getVersionCode());
        }

        // Clone the list fields of the 201305 first
        if (NullChecker.isNotNullish(request.getAttachmentText())) {
            newRequest.getAttachmentText().addAll(request.getAttachmentText());
        }

        if (NullChecker.isNotNullish(request.getAttentionLine())) {
            newRequest.getAttentionLine().addAll(request.getAttentionLine());
        }

        if (NullChecker.isNotNullish(request.getNullFlavor())) {
            newRequest.getNullFlavor().addAll(request.getNullFlavor());
        }

        if (NullChecker.isNotNullish(request.getReceiver())) {
            newRequest.getReceiver().addAll(request.getReceiver());
        }

        if (NullChecker.isNotNullish(request.getRespondTo())) {
            newRequest.getRespondTo().addAll(request.getRespondTo());
        }
        
        if (NullChecker.isNotNullish(request.getTemplateId())) {
     	    newRequest.getTemplateId().addAll(request.getTemplateId());
        }
        
        if (NullChecker.isNotNullish(request.getRealmCode())) {
            newRequest.getRealmCode().addAll(request.getRealmCode());
        }
        
        if (NullChecker.isNotNullish(request.getProfileId())) {
            newRequest.getProfileId().addAll(request.getProfileId());
        }

        return newRequest;
    }
}
