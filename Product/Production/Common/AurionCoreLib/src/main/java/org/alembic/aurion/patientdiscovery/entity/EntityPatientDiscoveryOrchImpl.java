/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.patientdiscovery.passthru.proxy.PassthruPatientDiscoveryProxy;
import org.alembic.aurion.patientdiscovery.passthru.proxy.PassthruPatientDiscoveryProxyObjectFactory;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201305Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201306Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryAuditLogger;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryPolicyChecker;
import org.alembic.aurion.patientdiscovery.response.ResponseFactory;
import org.alembic.aurion.patientdiscovery.response.ResponseParams;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.service.ServiceFanout;
import org.alembic.aurion.transform.subdisc.HL7PRPA201306Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryOrchImpl {

    private Log log = null;

    /**
     * default constructor
     */
    public EntityPatientDiscoveryOrchImpl() {
        log = createLogger();
    }

    /**
     * Create Log instance
     * @return Log
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return RespondingGatewayPRPAIN201306UV02ResponseType
     */
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        if (request == null) {
            log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
        } else if (assertion == null) {
            log.warn("AssertionType was null.");
        } else if (request.getPRPAIN201305UV02() == null) {
            log.warn("PRPAIN201305UV02 was null.");
        } else {
            if (isEntityAudit) {
                logEntityPatientDiscoveryRequest(request, assertion);
            }

            response = getResponseFromCommunities(request, assertion);
            if (isEntityAudit) {
                logAggregatedResponseFromNhin(response, assertion);
            }
        }
        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

    /**
     * 
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }

    /**
     * 
     * @return PatientDiscovery201305Processor
     */
    protected PatientDiscovery201305Processor getPatientDiscovery201305Processor() {
        return new PatientDiscovery201305Processor();
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param urlInfo
     * @return RespondingGatewayPRPAIN201305UV02RequestType
     */
    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        PRPAIN201305UV02 new201305 = getPatientDiscovery201305Processor().createNewRequest(request.getPRPAIN201305UV02(), urlInfo.getHcid());

        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(new201305);
        newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        return newRequest;
    }

    /**
     * 
     * @return ResponseFactory
     */
    protected ResponseFactory getResponseFactory() {
        return new ResponseFactory();
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return RespondingGatewayPRPAIN201306UV02ResponseType
     */
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("Entering getResponseFromCommunities");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;
        PRPAIN201306UV02 resultFromNhin = null;
        PassthruPatientDiscoveryProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryProxyObjectFactory();
        PassthruPatientDiscoveryProxy proxy = patientDiscoveryFactory.getNhincPatientDiscoveryProxy();

        CMUrlInfos urlInfoList = getEndpoints(request.getNhinTargetCommunities());

        //loop through the communities and send request if results were not null
        if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
            log.warn("No targets were found for the Patient Discovery Request");
        } else {
            response = new RespondingGatewayPRPAIN201306UV02ResponseType();

            List<ServiceFanout> fanoutList = new ArrayList<ServiceFanout>();
            List<Thread> threadList = new ArrayList<Thread>();
            List<ProxyPRPAIN201305UVProxySecuredRequestType> proxyMsgList = new ArrayList<ProxyPRPAIN201305UVProxySecuredRequestType>();
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

            for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                //create a new request to send out to each target community
                RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                newRequest = createNewRequest(request, assertion, urlInfo);

                //check the policy for the outgoing request to the target community
                boolean bIsPolicyOk = checkPolicy(newRequest, assertion);

                if (bIsPolicyOk) {

                    NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                    oTargetSystemType.setUrl(urlInfo.getUrl());

                    //format request for nhincProxyPatientDiscoveryImpl call
                    ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                            new ProxyPRPAIN201305UVProxySecuredRequestType();
                    oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(newRequest.getPRPAIN201305UV02());
                    oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(oTargetSystemType);
                    proxyMsgList.add(oProxyPRPAIN201305UVProxySecuredRequestType);

                    // Create the fanout thread
                    ServiceFanout fanout = new ServiceFanout(proxy, PassthruPatientDiscoveryProxy.class, "PRPAIN201305UV", oProxyPRPAIN201305UVProxySecuredRequestType.getPRPAIN201305UV02(), assertion, oProxyPRPAIN201305UVProxySecuredRequestType.getNhinTargetSystem());
                    fanoutList.add(fanout);
                    Thread pdThread = new Thread(fanout);
                    threadList.add(pdThread);

                    // Start the fanout thread and wait for it to finish
                    pdThread.start();
                } //if (bIsPolicyOk)
                else {
                    log.error("The policy engine evaluated the request and denied the request.");
                    resultFromNhin = new HL7PRPA201306Transforms().createPRPA201306ForErrors(request.getPRPAIN201305UV02(), NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
                } //else policy enging did not return a permit response
            } //for (NhinTargetCommunityType oTargetCommunity : request.getNhinTargetCommunities().getNhinTargetCommunity())

            // If there were threads started then join those threads to wait until they are finished and gather the results
            if (NullChecker.isNotNullish(threadList) &&
                    NullChecker.isNotNullish(fanoutList) &&
                    NullChecker.isNotNullish(proxyMsgList) &&
                    threadList.size() == fanoutList.size() &&
                    threadList.size() == proxyMsgList.size()) {
                log.debug("Start collecting responses from " + threadList.size() + " threads");
                ResponseParams params = new ResponseParams();
                Iterator<ProxyPRPAIN201305UVProxySecuredRequestType> msgIterator = proxyMsgList.iterator();
                Iterator<ServiceFanout> fanoutIterator = fanoutList.iterator();

                // Wait for all of the threads to either complete execution or timeout
                for (Thread tempThread : threadList) {
                    try {
                        log.debug("Collecting response from thread");
                        tempThread.join(120000);

                        resultFromNhin = new PRPAIN201306UV02();
                        resultFromNhin = (PRPAIN201306UV02) fanoutIterator.next().getResponse();

                        params.assertion = assertion;
                        params.origRequest = msgIterator.next();
                        params.response = resultFromNhin;
                        resultFromNhin = new ResponseFactory().getResponseMode().processResponse(params);

                        // Store AA to HCID Mapping from response
                        getPatientDiscovery201306Processor().storeMapping(resultFromNhin);

                        communityResponse = new CommunityPRPAIN201306UV02ResponseType();
                        communityResponse.setPRPAIN201306UV02(resultFromNhin);

                        log.debug("Adding Community Response to response object");
                        response.getCommunityResponse().add(communityResponse);
                    } catch (InterruptedException ex) {
                        log.error("Thread was interrupted prior to finishing execution");
                        log.error(ex);
                    }
                }

                log.debug("Collected " + response.getCommunityResponse().size() + " responses from threads");
            } else {
                log.warn("There were no threads to process");
            }
        }

        log.debug("Exiting getResponseFromCommunities");
        return response;
    }

    /**
     * 
     * @return PatientDiscovery201306Processor
     */
    protected PatientDiscovery201306Processor getPatientDiscovery201306Processor() {
        return new PatientDiscovery201306Processor();
    }

    /**
     * 
     * @return PatientDiscoveryAuditLogger
     */
    protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    /**
     * 
     * @param request
     * @param assertion
     */
    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditEntity201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    /**
     * 
     * @param response
     * @param assertion
     */
    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    /**
     * 
     * @return PatientDiscoveryPolicyChecker
     */
    protected PatientDiscoveryPolicyChecker getPatientDiscoveryPolicyChecker() {
        return new PatientDiscoveryPolicyChecker();
    }

    /**
     * 
     * @param request
     * @param assertion
     * @return boolean
     */
    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return getPatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
