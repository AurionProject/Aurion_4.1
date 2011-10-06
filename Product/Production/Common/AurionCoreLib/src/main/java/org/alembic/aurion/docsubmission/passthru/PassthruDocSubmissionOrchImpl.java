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

package org.alembic.aurion.docsubmission.passthru;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import org.alembic.aurion.docsubmission.XDRAuditLogger;
import org.alembic.aurion.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import org.alembic.aurion.docsubmission.nhin.proxy.NhinDocSubmissionProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionOrchImpl {
    private Log log = null;

    public PassthruDocSubmissionOrchImpl()
    {
        log = createLogger();
    }

    /**
     * 
     * @param request
     * @param assertion
     * @param targetSystem
     * @return RegistryResponseType
     */
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        RegistryResponseType response = null;
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        body.setNhinTargetSystem(targetSystem);
        body.setProvideAndRegisterDocumentSetRequest(request);
        AcknowledgementType ack = null;
        XDRAuditLogger auditLog = new XDRAuditLogger();
        if (isNhinAudit) {
            ack = auditLog.auditNhinXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            log.debug("ack: " + ack.getMessage());
        }
        NhinDocSubmissionProxyObjectFactory factory = new NhinDocSubmissionProxyObjectFactory();
        NhinDocSubmissionProxy proxy = factory.getNhinDocSubmissionProxy();
        response = proxy.provideAndRegisterDocumentSetB(request, assertion, targetSystem);
        if (isNhinAudit) {
            ack = auditLog.auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
            log.debug("ack: " + ack.getMessage());
        }
        return response;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
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
