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
package org.alembic.aurion.patientdiscovery.response;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201301UV02;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.transform.subdisc.HL7PRPA201301Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import org.alembic.aurion.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201305Processor;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

/**
 *
 * @author dunnek
 */
public class TrustMode implements ResponseMode {

    private Log log = null;

    public TrustMode() {
        super();
        log = createLogger();
    }

    public PRPAIN201306UV02 processResponse(ResponseParams params) {
        log.debug("begin processResponse");


        PRPAIN201306UV02 response = params.response;
        AssertionType assertion = params.assertion;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : response.getControlActProcess().getSubject()) {
            PRPAIN201306UV02 tempResp = HL7PRPA201306Transforms.createPRPA201306(subject, getReceiverCommunityId(response), getSenderCommunityId(response), response);
            if (requestHasLivingSubjectId(requestMsg)) {
                try {
                    II localPatId = getPatientId(requestMsg);
                    II remotePatId = getPatientId(tempResp);

                    if (localPatId != null &&
                            remotePatId != null) {
                        sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, tempResp);
                    } else {
                        log.error("One or more of the Patient Id values are null");
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            } else {
                log.debug("Local Patient Id was not provided, no correlation will be attempted");
            }
        }

        return response;
    }

    public PRPAIN201306UV02 processResponse(PRPAIN201306UV02 response, AssertionType assertion, II localPatId) {
        log.debug("begin processResponse");

        for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : response.getControlActProcess().getSubject()) {
            PRPAIN201306UV02 tempResp = HL7PRPA201306Transforms.createPRPA201306(subject, getReceiverCommunityId(response), getSenderCommunityId(response), response);
            II remotePatId = getPatientId(response);

            try {
                if (localPatId != null &&
                        remotePatId != null) {
                    sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, tempResp);
                } else {
                    log.error("One or more of the Patient Id values are null");
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        return response;
    }

    protected void sendToPatientCorrelationComponent(II localPatId, II remotePatId, AssertionType assertion, PRPAIN201306UV02 response) {
        PRPAIN201301UV02 request = new PRPAIN201301UV02();

        if (localPatId != null &&
                NullChecker.isNotNullish(localPatId.getRoot()) &&
                NullChecker.isNotNullish(localPatId.getExtension()) &&
                remotePatId != null &&
                NullChecker.isNotNullish(remotePatId.getRoot()) &&
                NullChecker.isNotNullish(remotePatId.getExtension())) {
            request = HL7PRPA201301Transforms.createPRPA201301(response, localPatId.getRoot());

            if (request != null &&
                    request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                    request.getControlActProcess().getSubject().get(0) != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {

                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localPatId);

                PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
                patCorrelationProxy.addPatientCorrelation(request, assertion);
            }
        }
    }

    protected boolean requestHasLivingSubjectId(PRPAIN201305UV02 request) {
        boolean result = false;

        result = (getPatientId(request) != null);

        return result;
    }

    protected II getPatientId(PRPAIN201305UV02 request) {
        return new PatientDiscovery201305Processor().extractPatientIdFrom201305(request);
    }

    protected II getPatientId(PRPAIN201306UV02 request) {
        II patId = null;

        if (request != null &&
                request.getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                request.getControlActProcess().getSubject().get(0) != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {

            patId = new II();
            patId.setRoot(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
            patId.setExtension(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
        }

        return patId;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 input) {
        PRPAIN201301UV02 result = null;

        result = HL7PRPA201301Transforms.createPRPA201301(input, getLocalHomeCommunityId());

        return result;
    }

    protected String getLocalHomeCommunityId() {
        String result = "";

        try {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    private PRPAIN201301UV02 mergeIds(PRPAIN201301UV02 patient, II localId) {
        PRPAIN201301UV02 result = patient;

        II remoteId;

        log.debug("begin MergeIds");

        if (result != null &&
                result.getControlActProcess() != null &&
                NullChecker.isNotNullish(result.getControlActProcess().getSubject()) &&
                result.getControlActProcess().getSubject().get(0) != null &&
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null) {
            try {
                remoteId = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);

                log.debug("Local Id = " + localId.getExtension() + "; remote id = " + remoteId.getExtension());

                //clear Id's
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();

                //add both the local and remote id.
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localId);
                result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remoteId);

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            return null;
        }

        return result;
    }

    private String getSenderCommunityId(PRPAIN201306UV02 response) {
        String hcid = null;

        if (response != null &&
                response.getSender() != null &&
                response.getSender().getDevice() != null &&
                response.getSender().getDevice().getAsAgent() != null &&
                response.getSender().getDevice().getAsAgent().getValue() != null &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            hcid = response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return hcid;
    }

    private String getReceiverCommunityId(PRPAIN201306UV02 response) {
        String hcid = null;

        if (response != null &&
                NullChecker.isNotNullish(response.getReceiver()) &&
                response.getReceiver().get(0) != null &&
                response.getReceiver().get(0).getDevice() != null &&
                response.getReceiver().get(0).getDevice().getAsAgent() != null &&
                response.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            hcid = response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return hcid;
    }
}


