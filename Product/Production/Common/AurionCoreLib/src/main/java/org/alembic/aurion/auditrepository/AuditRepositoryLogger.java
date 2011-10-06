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
package org.alembic.aurion.auditrepository;

import org.alembic.aurion.common.auditlog.AdhocQueryMessageType;
import org.alembic.aurion.common.auditlog.AdhocQueryResponseMessageType;
import org.alembic.aurion.common.auditlog.DocRetrieveMessageType;
import org.alembic.aurion.common.auditlog.DocRetrieveResponseMessageType;
import org.alembic.aurion.common.auditlog.FindAuditEventsMessageType;
import org.alembic.aurion.common.auditlog.FindAuditEventsResponseMessageType;
import org.alembic.aurion.common.auditlog.LogAdhocQueryRequestType;
import org.alembic.aurion.common.auditlog.LogAdhocQueryResultRequestType;
import org.alembic.aurion.common.auditlog.LogDocRetrieveRequestType;
import org.alembic.aurion.common.auditlog.LogDocRetrieveResultRequestType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.auditlog.LogFindAuditEventsRequestType;
import org.alembic.aurion.common.auditlog.LogFindAuditEventsResultRequestType;
import org.alembic.aurion.common.auditlog.LogNhinSubjectDiscoveryAckRequestType;
import org.alembic.aurion.common.auditlog.LogSubjectAddedRequestType;
import org.alembic.aurion.common.auditlog.LogSubjectReidentificationRequestType;
import org.alembic.aurion.common.auditlog.LogSubjectReidentificationResponseType;
import org.alembic.aurion.common.auditlog.LogSubjectRevisedRequestType;
import org.alembic.aurion.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import org.alembic.aurion.common.auditlog.SubjectAddedMessageType;
import org.alembic.aurion.common.auditlog.SubjectReidentificationRequestMessageType;
import org.alembic.aurion.common.auditlog.SubjectReidentificationResponseMessageType;
import org.alembic.aurion.common.auditlog.SubjectRevisedMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityCdcNotifyRequestMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityCdcSubscribeRequestMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityDocumentNotifyRequestMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityDocumentSubscribeRequestMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityNotifyResponseMessageType;
import org.alembic.aurion.common.hiemauditlog.EntityUnsubscribeRequestMessageType;
import org.alembic.aurion.common.hiemauditlog.LogEntityCdcNotifyRequestType;
import org.alembic.aurion.common.hiemauditlog.LogEntityCdcSubscribeRequestType;
import org.alembic.aurion.common.hiemauditlog.LogEntityDocumentNotifyRequestType;
import org.alembic.aurion.common.hiemauditlog.LogEntityDocumentSubscribeRequestType;
import org.alembic.aurion.common.hiemauditlog.LogEntityNotifyResponseType;
import org.alembic.aurion.common.hiemauditlog.LogEntityUnsubscribeRequestType;
import org.alembic.aurion.common.hiemauditlog.LogNhinNotifyRequestType;
import org.alembic.aurion.common.hiemauditlog.LogNhinSubscribeRequestType;
import org.alembic.aurion.common.hiemauditlog.LogNhinUnsubscribeRequestType;
import org.alembic.aurion.common.hiemauditlog.LogSubscribeResponseType;
import org.alembic.aurion.common.hiemauditlog.LogUnsubscribeResponseType;
import org.alembic.aurion.common.hiemauditlog.SubscribeResponseMessageType;
import org.alembic.aurion.common.hiemauditlog.UnsubscribeResponseMessageType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import org.alembic.aurion.common.nhinccommoninternalorch.NotifyRequestType;
import org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType;
import org.alembic.aurion.common.nhinccommoninternalorch.UnsubscribeRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.audit.AdminDistTransforms;
import org.alembic.aurion.transform.audit.DocumentQueryTransforms;
import org.alembic.aurion.transform.audit.DocumentRetrieveDeferredTransforms;
import org.alembic.aurion.transform.audit.DocumentRetrieveTransforms;
import org.alembic.aurion.transform.audit.FindAuditEventsTransforms;
import org.alembic.aurion.transform.audit.NotifyTransforms;
import org.alembic.aurion.transform.audit.PatientDiscoveryTransforms;
import org.alembic.aurion.transform.audit.SubjectDiscoveryTransforms;
import org.alembic.aurion.transform.audit.SubscribeTransforms;
import org.alembic.aurion.transform.audit.UnsubscribeTransforms;
import org.alembic.aurion.transform.audit.XDRTransforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;



/**
 *
 * @author Jon Hoppesch
 */
public class AuditRepositoryLogger {

    private static Log log = LogFactory.getLog(AuditRepositoryLogger.class);

    /**
     * This method will create the generic Audit Log Message from an subject discovery announce request
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectAdded(SubjectAddedMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logSubjectAdded(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            LogSubjectAddedRequestType logReqMsg = new LogSubjectAddedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectAdded(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscAck(MCCIIN000002UV01 message, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformAck2AuditMsg(message, assertion, direction, _interface);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a NHIN Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinPatientDiscResp(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscReq(PRPAIN201305UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformAdapterPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Adapter Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdapterPatientDiscResp(PRPAIN201306UV02 message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logAdapterPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformNhinPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdapterPatientDiscResp(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Request
     *
     * @param message The Patient Discovery Request message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscReq(RespondingGatewayPRPAIN201305UV02RequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformEntityPRPAIN201305RequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscResp(RespondingGatewayPRPAIN201306UV02ResponseType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformEntityPRPAIN201306ResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Async Response
     *
     * @param message The Patient Discovery Async Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityPatientDiscAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscAsyncResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();
            auditMsg = auditTransformer.transformEntityPRPAIN201306AsyncResponseToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscAsyncResp(...)");

        return auditMsg;
    }

    public LogEventRequestType logEntityXDRReq(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message.getProvideAndRegisterDocumentSetRequest(), assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logEntityXDRResponse(RegistryResponseType response, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformResponseToAuditMsg(response, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
        return auditMsg;
    }

    public LogEventRequestType logEntityXDRResponseRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType response, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityXDRResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(response, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logEntityXDRResponse(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query request
     *
     * @param message The Document Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQuery(AdhocQueryMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQuery(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryRequestType logReqMsg = new LogAdhocQueryRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = new DocumentQueryTransforms().transformDocQueryReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdhocQuery(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery revised request
     *
     * @param message The Subject Discovery Revised Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectRevised(SubjectRevisedMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logSubjectRevised(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectRevisedRequestType logReqMsg = new LogSubjectRevisedRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectRevised(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery acknowledgement
     *
     * @param message The Subject Discovery Ack message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinSubjectDiscoveryAck(NhinSubjectDiscoveryAckMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinSubjectDiscoveryAck(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogNhinSubjectDiscoveryAckRequestType logReqMsg = new LogNhinSubjectDiscoveryAckRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = SubjectDiscoveryTransforms.transformAck2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinSubjectDiscoveryAck(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document query response
     *
     * @param message The Document Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logAdhocQueryResult(AdhocQueryResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogAdhocQueryResultRequestType logReqMsg = new LogAdhocQueryResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = new DocumentQueryTransforms().transformDocQueryResp2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdhocQueryResult(...)");

        return auditMsg;
    }

    public LogEventRequestType logAdhocQueryDeferredAck(DocQueryAcknowledgementType acknowledgement, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logAdhocQueryResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            auditMsg = new DocumentQueryTransforms().transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, _interface);
        }
        log.debug("Exiting AuditRepositoryLogger.logAdhocQueryResult(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve request
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logDocRetrieve(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveRequestType logReqMsg = new LogDocRetrieveRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieve(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a document retrieve response
     *
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logDocRetrieveResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogDocRetrieveResultRequestType logReqMsg = new LogDocRetrieveResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logDocRetrieveResult(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an audit query request
     *
     * @param message The Audit Query Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEvents(FindAuditEventsMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logFindAuditEvents(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            LogFindAuditEventsRequestType logReqMsg = new LogFindAuditEventsRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEvents(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an audit query response
     *
     * @param message The Audit Query Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logFindAuditEventsResult(FindAuditEventsResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logFindAuditEventsResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogFindAuditEventsResultRequestType logReqMsg = new LogFindAuditEventsResultRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logFindAuditEventsResult method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logFindAuditEventsResult(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery reidentification request
     *
     * @param message The Subject Discovery Reidentification Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectReident(SubjectReidentificationRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logSubjectReident(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationRequestType logReqMsg = new LogSubjectReidentificationRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectReident(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subject discovery reidentification response
     *
     * @param message The Subject Discovery Reidentification Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubjectReidentResult(SubjectReidentificationResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logSubjectReidentResult(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubjectReidentificationResponseType logReqMsg = new LogSubjectReidentificationResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            auditMsg = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubjectReidentResult(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin Subscribe request
     *
     * @param message The Nhin Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinSubscribeRequest(SubscribeRequestType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            SubscribeTransforms transformLib = new SubscribeTransforms();
            LogNhinSubscribeRequestType logReqMsg = new LogNhinSubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinSubscribeRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin notify request
     *
     * @param message The Nhin Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNotifyRequest(NotifyRequestType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            NotifyTransforms transformLib = new NotifyTransforms();
            LogNhinNotifyRequestType logReqMsg = new LogNhinNotifyRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNotifyRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a Nhin unsubscribe request
     *
     * @param message The Nhin Unsubscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logNhinUnsubscribeRequest(UnsubscribeRequestType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            LogNhinUnsubscribeRequestType logReqMsg = new LogNhinUnsubscribeRequestType();
            logReqMsg.setMessage(message);
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);

            auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logNhinUnsubscribeRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an unsubscribe response
     *
     * @param message The Unsubscribe Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logUnsubscribeResponse(UnsubscribeResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logUnsubscribeResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogUnsubscribeResponseType logReqMsg = new LogUnsubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();

            auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logUnsubscribeResponse(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from a subscribe response
     *
     * @param message The Subscribe Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logSubscribeResponse(SubscribeResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logSubscribeResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogSubscribeResponseType logReqMsg = new LogSubscribeResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            SubscribeTransforms transformLib = new SubscribeTransforms();

            auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logSubscribeResponse(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document subscribe request
     *
     * @param message The Entity Document Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocSubscribeRequest(EntityDocumentSubscribeRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentSubscribeRequestType logReqMsg = new LogEntityDocumentSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocSubscribeRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityDocSubscribeRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC subscribe request
     *
     * @param message The Entity CDC Subscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcSubscribeRequest(EntityCdcSubscribeRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcSubscribeRequestType logReqMsg = new LogEntityCdcSubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcSubscribeRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcSubscribeRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity document notify request
     *
     * @param message The Entity Document Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityDocNotifyRequest(EntityDocumentNotifyRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityDocNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityDocumentNotifyRequestType logReqMsg = new LogEntityDocumentNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityDocNotifyRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityDocNotifyRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity CDC notify request
     *
     * @param message The Entity CDC Notify Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityCdcNotifyRequest(EntityCdcNotifyRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityCdcNotifyRequestType logReqMsg = new LogEntityCdcNotifyRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityCdcNotifyRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityCdcNotifyRequest(...)");

        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity notify response
     *
     * @param message The Entity Notify Response message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityNotifyResponse(EntityNotifyResponseMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityNotifyResponse(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            LogEntityNotifyResponseType logReqMsg = new LogEntityNotifyResponseType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);

            log.warn("logEntityNotifyRespRequest method is not implemented");
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityNotifyResponse(...)");

        return auditMsg;
    }

    public LogEventRequestType logXDRReq(ProvideAndRegisterDocumentSetRequestType message, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;

        if (message == null) {
            log.error("Message is null");
            return null;
        }

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, _interface);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logXDRReq(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    public LogEventRequestType logXDRResponse(RegistryResponseType message, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logXDRResponse(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformResponseToAuditMsg(message, assertion, direction, _interface);
        }

        log.debug("Exiting AuditRepositoryLogger.logXDRResponse(...)");
        return auditMsg;
    }

    public LogEventRequestType logNhinXDRResponseRequest(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformRequestToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }

    /**
     * This method will create the generic Audit Log Message from an entity unsubscribe request
     *
     * @param message The Entity Unsubscribe Request message to be audit logged.
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityUnsubscribeRequest(EntityUnsubscribeRequestMessageType message, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            UnsubscribeTransforms auditTransforms = new UnsubscribeTransforms();
            LogEntityUnsubscribeRequestType logReqMsg = new LogEntityUnsubscribeRequestType();
            logReqMsg.setDirection(direction);
            logReqMsg.setInterface(_interface);
            logReqMsg.setMessage(message);
            auditMsg = auditTransforms.transformEntityUnsubscribeRequestToAuditMessage(logReqMsg);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityUnsubscribeRequest(...)");

        return auditMsg;
    }

    protected boolean isServiceEnabled() {
        log.debug("Entering AuditRepositoryLogger.isServiceEnabled(...)");
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage(), ex);
        }
        log.debug("Exiting AuditRepositoryLogger.isServiceEnabled(...) with value of: " + serviceEnabled);
        return serviceEnabled;
    }

    /**
     *
     * @param acknowledgement
     * @param assertion
     * @param direction
     * @param action
     * @return
     */
    public LogEventRequestType logAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action, String _interface) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, _interface, action);
        }

        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }
    
    public LogEventRequestType logEntityAcknowledgement(XDRAcknowledgementType acknowledgement, AssertionType assertion, String direction, String action) {
        getLogger().debug("Entering AuditRepositoryLogger.logAcknowledgement(...)");

        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            XDRTransforms auditTransformer = new XDRTransforms();
            auditMsg = auditTransformer.transformAcknowledgementToAuditMsg(acknowledgement, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, action);
        }

        getLogger().debug("Exiting AuditRepositoryLogger.logAcknowledgement(...)");
        return auditMsg;
    }
    
    /**
     * This method will create the generic Audit Log Message from an Entity Patient Discovery Response
     *
     * @param message The Patient Discovery Response message to be audit logged.
     * @param assertion The Assertion Class containing SAML information
     * @param direction  The direction this message is going (Inbound or Outbound)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logEntityAdminDist(org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageType message, AssertionType assertion, String direction) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            AdminDistTransforms auditTransformer = new AdminDistTransforms();
            auditMsg = auditTransformer.transformEntitySendAlertToAuditMsg(message, assertion, direction, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");

        return auditMsg;
    }

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @return LogEventRequestType
     */
    public LogEventRequestType logNhincAdminDist(EDXLDistribution message, AssertionType assertion, NhinTargetSystemType target, String direction) {

        return logAdminDist(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }

    /**
     * 
     * @param message
     * @param assertion
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType logAdminDist(EDXLDistribution message, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logEntityPatientDiscResp(...)");
        LogEventRequestType auditMsg = null;

        if (isServiceEnabled()) {
            AdminDistTransforms auditTransformer = new AdminDistTransforms();
            auditMsg = auditTransformer.transformEDXLDistributionRequestToAuditMsg(message, assertion, direction, _interface);
        }
        log.debug("Exiting AuditRepositoryLogger.logEntityPatientDiscResp(...)");

        return auditMsg;
    }

    /**
     *
     * @return Log
     */
    protected Log getLogger(){
        return log;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param direction
     * @return LogEventRequestType
     */
    public LogEventRequestType logDocRetrieveAckResponse(RegistryResponseType message, AssertionType assertion, String direction, String _interface) {
        log.debug("Entering AuditRepositoryLogger.logNhinXDRReq(...)");
        LogEventRequestType auditMsg = null;


        if (isServiceEnabled()) {
            DocumentRetrieveDeferredTransforms auditTransformer = new DocumentRetrieveDeferredTransforms();
            auditMsg = auditTransformer.transformAckResponseToAuditMsg(message, assertion, direction, _interface);
        }

        log.debug("Exiting AuditRepositoryLogger.logNhinXDRReq(...)");
        return auditMsg;
    }
}
