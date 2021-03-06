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
package org.alembic.aurion.docquery.nhin.deferred.request;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.DocQueryPolicyChecker;
import org.alembic.aurion.docquery.adapter.deferred.request.error.proxy.AdapterDocQueryDeferredRequestErrorProxy;
import org.alembic.aurion.docquery.adapter.deferred.request.error.proxy.AdapterDocQueryDeferredRequestErrorProxyObjectFactory;
import org.alembic.aurion.docquery.adapter.deferred.request.proxy.AdapterDocQueryDeferredRequestProxy;
import org.alembic.aurion.docquery.adapter.deferred.request.proxy.AdapterDocQueryDeferredRequestProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredRequestOrchImpl {

    private static Log log = LogFactory.getLog(NhinDocQueryDeferredRequestOrchImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        String ackMsg = null;
        AcknowledgementType ack = null;
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        // Audit the incoming NHIN Message
        if (isNhinAudit) {
            ack = auditRequest(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        // Check if the service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {

                // Perform the inbound policy check
                if (isPolicyValid(msg, assertion)) {
                    respAck = sendToAgency(msg, assertion);
                } else {
                    ackMsg = "Policy Check Failed for incoming Document Query Deferred Request";
                    log.error(ackMsg);
                    respAck = sendToAgencyError(msg, assertion, ackMsg);
                }
            } else {
                // Send the deferred request to the Adapter Interface
                respAck = sendToAgency(msg, assertion);
            }
        } else {
            // Send the error to the Adapter Error Interface
            ackMsg = "Document Query Deferred Request Service Not Enabled";
            log.error(ackMsg);
            respAck = sendToAgencyError(msg, assertion, ackMsg);
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
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    private boolean isPolicyValid(AdhocQueryRequest message, AssertionType assertion) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingPolicy(message, assertion);

        return policyIsValid;
    }

    private DocQueryAcknowledgementType sendToAgencyError(AdhocQueryRequest request, AssertionType assertion, String errMsg) {
        log.debug("Sending Request to Adapter Error Interface");
        
        AcknowledgementType ack = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        // Audit the Adapter Request
        if (isAdapterAudit) {
            ack = auditRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        AdapterDocQueryDeferredRequestErrorProxyObjectFactory factory = new AdapterDocQueryDeferredRequestErrorProxyObjectFactory();
        AdapterDocQueryDeferredRequestErrorProxy proxy = factory.getAdapterDocQueryDeferredRequestErrorProxy();
        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion, errMsg);

        // Audit the incoming Adapter Message
        if (isAdapterAudit) {
            ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        return ackResp;
    }

    private DocQueryAcknowledgementType sendToAgency(AdhocQueryRequest request, AssertionType assertion) {
        log.debug("Sending Request to Adapter Interface");

        AcknowledgementType ack = null;
        boolean isAdapterAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ADAPTER_AUDIT_PROPERTY);
        // Audit the Adapter Request
        if (isAdapterAudit) {
            ack = auditRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        AdapterDocQueryDeferredRequestProxyObjectFactory factory = new AdapterDocQueryDeferredRequestProxyObjectFactory();
        AdapterDocQueryDeferredRequestProxy proxy = factory.getAdapterDocQueryDeferredRequestProxy();

        DocQueryAcknowledgementType ackResp = proxy.respondingGatewayCrossGatewayQuery(request, assertion);

        // Audit the incoming Adapter Message
        if (isAdapterAudit) {
            ack = auditAck(ackResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        return ackResp;
    }

    private AcknowledgementType auditRequest(AdhocQueryRequest msg, AssertionType assertion, String direction, String _interface) {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg, assertion, direction, _interface);

        return ack;
    }

    private AcknowledgementType auditAck(DocQueryAcknowledgementType msg, AssertionType assertion, String direction, String _interface) {
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
