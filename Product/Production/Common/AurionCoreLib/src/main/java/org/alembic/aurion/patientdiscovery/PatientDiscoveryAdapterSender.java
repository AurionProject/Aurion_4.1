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

package org.alembic.aurion.patientdiscovery;

import org.alembic.aurion.patientdiscovery.adapter.deferred.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxy;
import org.alembic.aurion.patientdiscovery.adapter.deferred.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory;
import org.alembic.aurion.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import org.alembic.aurion.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxy;
import org.alembic.aurion.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory;
import org.alembic.aurion.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import org.alembic.aurion.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import org.alembic.aurion.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import org.alembic.aurion.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import org.alembic.aurion.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAdapterSender {

    public PRPAIN201306UV02 send201305ToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryProxyObjectFactory factory = new AdapterPatientDiscoveryProxyObjectFactory();
        AdapterPatientDiscoveryProxy proxy = factory.getAdapterPatientDiscoveryProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        PRPAIN201306UV02 adapterResp = proxy.respondingGatewayPRPAIN201305UV02(adapterReq.getPRPAIN201305UV02(), adapterReq.getAssertion());

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredReqToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        AdapterPatientDiscoveryDeferredReqProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredReqProxyObjectFactory();
        AdapterPatientDiscoveryDeferredReqProxy proxy = factory.getAdapterPatientDiscoveryDeferredReqProxy();

        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReq(request, assertion);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendAsyncReqToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {

        AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory factory = new AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory();
        AdapterPatientDiscoveryAsyncReqQueueProxy proxy = factory.getAdapterPatientDiscoveryAsyncReqQueueProxy();

        MCCIIN000002UV01 adapterResp = proxy.addPatientDiscoveryAsyncReq(request, assertion);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredReqErrorToAgency(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        AsyncAdapterPatientDiscoveryErrorRequestType adapterReq = new AsyncAdapterPatientDiscoveryErrorRequestType();

        AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory();
        AdapterPatientDiscoveryDeferredReqErrorProxy proxy = factory.getAdapterPatientDiscoveryDeferredReqErrorProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        adapterReq.setErrorMsg(errMsg);
        PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request);

        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReqError(request, response, assertion, errMsg);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredRespToAgency(PRPAIN201306UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201306UV02RequestType adapterReq = new RespondingGatewayPRPAIN201306UV02RequestType();

        AdapterPatientDiscoveryDeferredRespProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredRespProxyObjectFactory();
        AdapterPatientDiscoveryDeferredRespProxy proxy = factory.getAdapterPatientDiscoveryDeferredRespProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201306UV02(request);
        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncResp(request, assertion);

        return adapterResp;
    }

}
