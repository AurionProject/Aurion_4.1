/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.passthru.deferred.response;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryAuditLogger;
import org.alembic.aurion.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxy;
import org.alembic.aurion.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxyObjectFactory;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespOrchImpl
{
    private static Log log = LogFactory.getLog(PassthruPatientDiscoveryDeferredRespOrchImpl.class);

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        MCCIIN000002UV01 response = null;
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        if (isNhinAudit) {
            auditLog.auditNhin201306(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        }

        NhinPatientDiscoveryDeferredRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryDeferredRespProxyObjectFactory();
        NhinPatientDiscoveryDeferredRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

        response = proxy.respondingGatewayPRPAIN201306UV02(request, assertion, targetSystem);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        if (isNhinAudit) {
            auditLog.auditAck(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        return response;
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
