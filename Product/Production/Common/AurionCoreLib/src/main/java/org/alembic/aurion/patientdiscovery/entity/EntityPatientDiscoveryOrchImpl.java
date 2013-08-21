/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201305Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201306Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryAuditLogger;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryPolicyChecker;
import org.alembic.aurion.patientdiscovery.passthru.proxy.PassthruPatientDiscoveryProxy;
import org.alembic.aurion.patientdiscovery.passthru.proxy.PassthruPatientDiscoveryProxyObjectFactory;
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

    /**
     * The default amount of time (in seconds) to wait before timing out all of
     * the requests.
     */
    public static final long DEFAULT_TIMEOUT_SECONDS = 120;

    private Log log = null;

    /**
     * default constructor
     */
    public EntityPatientDiscoveryOrchImpl() {
        log = createLogger();
    }

    /**
     * Create Log instance
     * 
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
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion) {
        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;
        boolean isEntityAudit = isAuditEnabled();
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
    protected CMUrlInfos getEndpoints(
            NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache
                    .getEndpontURLFromNhinTargetCommunities(targetCommunities,
                            NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
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
    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
            RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion, CMUrlInfo urlInfo) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        PRPAIN201305UV02 new201305 = getPatientDiscovery201305Processor()
                .createNewRequest(request.getPRPAIN201305UV02(),
                        urlInfo.getHcid());

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
     * Queries the target communities and aggregates their responses.
     * 
     * @param request
     *            the request containing the query and the communities to
     *            target.
     * @param assertion
     *            the information for the security assertion.
     * @return a {@link RespondingGatewayPRPAIN201306UV02ResponseType}
     *         containing the aggregated responses.
     */
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(
            RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion) {
        log.debug("Entering getResponseFromCommunities");
        final RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        final CMUrlInfos urlInfoList = getEndpoints(request.getNhinTargetCommunities());

        // loop through the communities and send request if results were not
        // null
        if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
            log.warn("No targets were found for the Patient Discovery Request");
        } else {
            final List<ProxyPRPAIN201305UVProxySecuredRequestType> requests = createProxyRequests(
                    request, assertion, urlInfoList);
            final Map<ProxyPRPAIN201305UVProxySecuredRequestType, ServiceFanout> requestToServiceMap = createRequestToServiceMap(
                    requests, assertion);

            log.debug("Start collecting responses from "
                    + requestToServiceMap.size() + " threads");

            executeQuery(requestToServiceMap.values());
            final List<CommunityPRPAIN201306UV02ResponseType> communityResponses = extractCommunityResponses(
                    assertion, requestToServiceMap);
            response.getCommunityResponse().addAll(communityResponses);

            log.debug("Collected " + response.getCommunityResponse().size()
                    + " responses from threads");
        }

        log.debug("Exiting getResponseFromCommunities");
        
        return response;
    }

    /**
     * Creates a list of proxy patient discovery requests that should be sent to
     * the target endpoints defined in the URL list.
     * 
     * @param request
     *            the request that should be sent to the target endpoints.
     * @param assertion
     *            the security assertion that should be sent.
     * @param urlInfoList
     *            the list of endpoints that should receive the request.
     * 
     * @return a list of proxy patient discovery requests.
     */
    private List<ProxyPRPAIN201305UVProxySecuredRequestType> createProxyRequests(
            final RespondingGatewayPRPAIN201305UV02RequestType request,
            final AssertionType assertion, final CMUrlInfos urlInfoList) {
        final List<ProxyPRPAIN201305UVProxySecuredRequestType> proxyRequests = new ArrayList<ProxyPRPAIN201305UVProxySecuredRequestType>();
        for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
            // create a new request to send out to each target community
            RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRequest(
                    request, assertion, urlInfo);

            // check the policy for the outgoing request to the target community
            boolean isPolicyOk = checkPolicy(newRequest, assertion);

            if (isPolicyOk) {
                final NhinTargetSystemType targetSystem = createTargetSystem(urlInfo);

                // format request for nhincProxyPatientDiscoveryImpl call
                final ProxyPRPAIN201305UVProxySecuredRequestType proxySecuredRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
                proxySecuredRequest.setPRPAIN201305UV02(newRequest
                        .getPRPAIN201305UV02());
                proxySecuredRequest.setNhinTargetSystem(targetSystem);
                proxyRequests.add(proxySecuredRequest);
            } 
            else {
                log.error("The policy engine evaluated the request for HCID "
                        + urlInfo.getHcid() + " and denied the request.");
            }
        }
        
        return proxyRequests;
    }

    /**
     * Creates a mapping between proxy patient discovery requests and the
     * service fanout thread that will initiate the query.
     * 
     * @param requests
     *            the requests that should be mapped to service threads.
     * @param assertion
     *            the assertion to include in the request.
     * @return a mapping between the provided proxy patient discovery requests
     *         and the service thread that will perform the query.
     */
    private Map<ProxyPRPAIN201305UVProxySecuredRequestType, ServiceFanout> createRequestToServiceMap(
            final List<ProxyPRPAIN201305UVProxySecuredRequestType> requests,
            final AssertionType assertion) {
        final PassthruPatientDiscoveryProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryProxyObjectFactory();
        final PassthruPatientDiscoveryProxy proxy = patientDiscoveryFactory
                .getNhincPatientDiscoveryProxy();

        final Map<ProxyPRPAIN201305UVProxySecuredRequestType, ServiceFanout> serviceMap = new LinkedHashMap<ProxyPRPAIN201305UVProxySecuredRequestType, ServiceFanout>();
        for (ProxyPRPAIN201305UVProxySecuredRequestType request : requests) {
            final ServiceFanout service = new ServiceFanout(proxy,
                    PassthruPatientDiscoveryProxy.class, "PRPAIN201305UV",
                    request.getPRPAIN201305UV02(), assertion, request
                            .getNhinTargetSystem());
            serviceMap.put(request, service);
        }

        return serviceMap;
    }

    /**
     * Creates an NHIN target system based on the provided connection manager
     * URL information.
     * 
     * @param urlInfo
     *            the connection manager URL information used to populate the
     *            NHIN target system.
     * @return the {@link NhinTargetSystemType} populated from the connection
     *         manager URL information.
     */
    private NhinTargetSystemType createTargetSystem(CMUrlInfo urlInfo) {
        final HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(urlInfo.getHcid());
    
        final NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(urlInfo.getUrl());
        targetSystem.setHomeCommunity(homeCommunity);
        
        return targetSystem;
    }

    /**
     * Initiates a broadcast query to all of the provided services. The method
     * will return when either all of the services have responded or the timeout
     * specified by {@link #getRequestTimeout()} is reached.
     * 
     * @param services the services that should be queried.
     */
    private void executeQuery(final Collection<ServiceFanout> services) {
        final ExecutorService threadPool = Executors.newFixedThreadPool(services.size());
        for (ServiceFanout service : services)
        {
            threadPool.execute(service);
        }
        // Wait for all of the threads to either complete execution or timeout
        try {
            log
                    .debug("Spawned patient discovery threads, awaiting termination");
            threadPool.shutdown();
            threadPool.awaitTermination(getRequestTimeout(),
                    TimeUnit.SECONDS);
            threadPool.shutdownNow();
            log.debug("All patient discovery threads terminated");
        } catch (InterruptedException ex) {
            log
                    .error(
                            "Thread was interrupted prior to finishing execution",
                            ex);
        }
    }

    /**
     * Extracts the community responses from the services that were sent
     * requests.
     * 
     * @param assertion
     *            the security assertion sent with the requests to the services.
     * @param requestToServiceMap
     *            a mapping between the original requests and the services that
     *            processed the requests.
     * 
     * @return a list of responses from the services.
     */
    private List<CommunityPRPAIN201306UV02ResponseType> extractCommunityResponses(
            AssertionType assertion, final Map<ProxyPRPAIN201305UVProxySecuredRequestType, ServiceFanout> requestToServiceMap) {
        final List<CommunityPRPAIN201306UV02ResponseType> communityResponses = new ArrayList<CommunityPRPAIN201306UV02ResponseType>();
        for (ProxyPRPAIN201305UVProxySecuredRequestType originalRequest : requestToServiceMap.keySet()) {
            final ServiceFanout service = requestToServiceMap.get(originalRequest);
            final String hcid = originalRequest.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId(); 
            PRPAIN201306UV02 resultFromNhin = (PRPAIN201306UV02) service.getResponse();

            log.debug("Collecting response from thread for HCID " + hcid);
                
            if (resultFromNhin == null) {
                log.debug("No result for HCID " + hcid);
                resultFromNhin = new HL7PRPA201306Transforms()
                        .createPRPA201306ForErrors(
                                originalRequest.getPRPAIN201305UV02(),
                                NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
            }

            log.debug("Processing response for HCID " + hcid);
            
            ResponseParams params = new ResponseParams();
            params.assertion = assertion;
            params.origRequest = originalRequest;
            params.response = resultFromNhin;
            resultFromNhin = new ResponseFactory().getResponseMode()
                    .processResponse(params);

            log.debug("Storing AA mapping for HCID " + hcid);
            
            // Store AA to HCID Mapping from response
            getPatientDiscovery201306Processor().storeMapping(
                    resultFromNhin);

            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02(resultFromNhin);

            log.debug("Adding Community Response to response object for HCID " + hcid);
            communityResponses.add(communityResponse);
        }
        return communityResponses;
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
    protected void logEntityPatientDiscoveryRequest(
            RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditEntity201305(request, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    /**
     * 
     * @param response
     * @param assertion
     */
    protected void logAggregatedResponseFromNhin(
            RespondingGatewayPRPAIN201306UV02ResponseType response,
            AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditEntity201306(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
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
    protected boolean checkPolicy(
            RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return getPatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected boolean isAuditEnabled() {
        return getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.ENTITY_AUDIT_PROPERTY);
    }

    /**
     * Returns the amount of time (in seconds) to wait before giving up on
     * unfinished requests.
     * 
     * @return the amount of time (in seconds) to wait before giving up on
     *         unfinished requests.
     */
    protected long getRequestTimeout() {

        long requestTimeout = DEFAULT_TIMEOUT_SECONDS;

        try {
            requestTimeout = PropertyAccessor.getPropertyLong(
                    NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.PATIENT_DISCOVERY_ENTITY_TIMEOUT);
            log.debug("Read PD entity timeout: " + requestTimeout);
            requestTimeout = (requestTimeout > 0) ? requestTimeout
                    : DEFAULT_TIMEOUT_SECONDS;
        } catch (PropertyAccessException e) {
            log.warn(
                    "Unable to retrieve the patient discovery timeout value, defaulting to "
                            + DEFAULT_TIMEOUT_SECONDS + "s: " + e.getMessage(),
                    e);
        }
        return requestTimeout;
    }

    /**
     * 
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile,
            String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(
                    sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
