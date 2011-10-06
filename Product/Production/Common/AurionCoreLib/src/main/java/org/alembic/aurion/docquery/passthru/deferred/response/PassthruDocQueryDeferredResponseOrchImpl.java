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
package org.alembic.aurion.docquery.passthru.deferred.response;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxy;
import org.alembic.aurion.docquery.nhin.deferred.response.proxy.NhinDocQueryDeferredResponseProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseOrchImpl {
    
    private static final Log log = LogFactory.getLog(PassthruDocQueryDeferredResponseOrchImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse body, AssertionType assertion, NhinTargetSystemType target) {
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        AcknowledgementType ack = null;
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG);
        respAck.setMessage(regResp);

        // Audit the outgoing NHIN Message
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        if (isNhinAudit) {
            ack = auditResponse(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        // Call the NHIN Interface
        NhinDocQueryDeferredResponseProxyObjectFactory factory = new NhinDocQueryDeferredResponseProxyObjectFactory();
        NhinDocQueryDeferredResponseProxy proxy = factory.getNhinDocQueryDeferredResponseProxy();
        respAck = proxy.respondingGatewayCrossGatewayQuery(body, assertion, target);

        // Audit the incoming NHIN Message
        if (isNhinAudit) {
            ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        return respAck;
    }

    private AcknowledgementType auditResponse (AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface);

        return ack;
    }

    private AcknowledgementType auditAck (DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface);

        return ack;
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
