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

package org.alembic.aurion.patientdiscovery.passthru.proxy;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.alembic.aurion.patientdiscovery.passthru.NhincPatientDiscoveryOrchImpl;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author mflynn02
 */
public class PassthruPatientDiscoveryProxyJavaImpl implements PassthruPatientDiscoveryProxy {
   private Log log = null;

    public PassthruPatientDiscoveryProxyJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public PRPAIN201306UV02 PRPAIN201305UV(PRPAIN201305UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Entering NhincPatientDiscoveryProxyJavaImpl.PRPAIN201305UV");

        ProxyPRPAIN201305UVProxySecuredRequestType secureRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
        secureRequest.setPRPAIN201305UV02(body);
        secureRequest.setNhinTargetSystem(target);
        return new NhincPatientDiscoveryOrchImpl().proxyPRPAIN201305UV(secureRequest, assertion);
    }
}