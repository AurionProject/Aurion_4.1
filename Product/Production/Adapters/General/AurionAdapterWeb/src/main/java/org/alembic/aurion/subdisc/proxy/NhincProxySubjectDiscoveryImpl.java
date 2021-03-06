/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subdisc.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecured;
import org.alembic.aurion.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecuredPortType;
import org.alembic.aurion.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.alembic.aurion.util.soap.SoapLogger;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxySecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxySecuredRequestType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class NhincProxySubjectDiscoveryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxySubjectDiscoveryImpl.class);
    private static NhincProxySubjectDiscoverySecured service = new NhincProxySubjectDiscoverySecured();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            getSoapLogger().logRawAssertion(assertIn);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201301UVProxySecuredRequestType body = new PIXConsumerPRPAIN201301UVProxySecuredRequestType();
            body.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            ack = port.pixConsumerPRPAIN201301UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVProxyRequestType request) {
        PRPAIN201310UV02 response = new PRPAIN201310UV02();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            getSoapLogger().logRawAssertion(assertIn);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201309UVProxySecuredRequestType body = new PIXConsumerPRPAIN201309UVProxySecuredRequestType();
            body.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            response = port.pixConsumerPRPAIN201309UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private NhincProxySubjectDiscoverySecuredPortType getPort(String url) {
        NhincProxySubjectDiscoverySecuredPortType port = service.getNhincProxySubjectDiscoverySecuredPortSoap();

        log.info("Setting endpoint address to Entity Subject Discovery Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
