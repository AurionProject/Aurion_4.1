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
package org.alembic.aurion.docquery.nhin.deferred.response;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.DocQueryPolicyChecker;
import org.alembic.aurion.docquery.adapter.deferred.response.proxy.AdapterDocQueryDeferredResponseProxy;
import org.alembic.aurion.docquery.adapter.deferred.response.proxy.AdapterDocQueryDeferredResponseProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredResponseOrchImpl {

    private static Log log = LogFactory.getLog(NhinDocQueryDeferredResponseOrchImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        AcknowledgementType ack = null;
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG);
        respAck.setMessage(regResp);

        // Audit the incoming NHIN Message
        if (isNhinAudit) {
            ack = auditResponse(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        // Check if the service is enabled
        if (isServiceEnabled()) {
            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                // Perform the inbound policy check
                if (isPolicyValid(msg, assertion)) {
                    respAck = sendToAgency(msg, assertion);
                } else {
                    log.error("Policy Check Failed for incoming Document Query Deferred Response");
                }
            } else {
                // Send the deferred response to the Adapter Interface
                respAck = sendToAgency(msg, assertion);
            }
        } else {
            // Service is not enabled so we are not doing anything with this response
            log.error("Document Query Deferred Response Service Not Enabled");
        }

        // Audit the outgoing NHIN Message
        if (isNhinAudit) {
            ack = auditAck(respAck, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        return respAck;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;

        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_RESP_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    private DocQueryAcknowledgementType sendToAgency(AdhocQueryResponse request, AssertionType assertion) {
        log.debug("Sending Response to Adapter Interface");
        AcknowledgementType ack = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        // Audit the Adapter Response Message
        if (isAdapterAudit) {
            ack = auditResponse(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }
        AdapterDocQueryDeferredResponseProxyObjectFactory factory = new AdapterDocQueryDeferredResponseProxyObjectFactory();
        AdapterDocQueryDeferredResponseProxy proxy = factory.getAdapterDocQueryDeferredResponseProxy();

        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion);

        // Audit the incoming Adapter Message
        if (isAdapterAudit) {
            ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        return ackResp;
    }

    private AcknowledgementType auditResponse(AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.logDocQueryAck(msg, assertion, direction, _interface);

        return ack;
    }

    private boolean isPolicyValid(AdhocQueryResponse message, AssertionType assertion) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingResponsePolicy(message, assertion);

        return policyIsValid;
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
