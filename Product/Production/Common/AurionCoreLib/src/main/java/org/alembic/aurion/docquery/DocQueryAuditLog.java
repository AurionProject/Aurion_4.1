/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery;

import org.alembic.aurion.auditrepository.AuditRepositoryLogger;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import org.alembic.aurion.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import org.alembic.aurion.common.auditlog.AdhocQueryMessageType;
import org.alembic.aurion.common.auditlog.AdhocQueryResponseMessageType;
import org.alembic.aurion.common.auditlog.LogEventRequestType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class DocQueryAuditLog {

  private static final Log LOG = LogFactory.getLog(DocQueryAuditLog.class);

  /**
   * This method will log Audit Secured Query Requests received on the Entity Interface
   *
   * @param auditMsg The Audit Query Request message to be audit logged.
   * @param assertion Assertion received
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType audit(RespondingGatewayCrossGatewayQuerySecuredRequestType auditMsg, AssertionType assertion) {
    return audit(auditMsg.getAdhocQueryRequest(), assertion);
  }

  /**
   * This method will log Audit Query Secured/Unsecured Requests received on the Entity Interface
   * @param message The AdhocQueryRequest message to be audit logged.
   * @param assertion Assertion received
   */
  public AcknowledgementType audit(AdhocQueryRequest message, AssertionType assertion) {
    getLog().debug("Entering DocQueryAuditLog.audit (entity)...");

    AdhocQueryMessageType auditReqMsg = new AdhocQueryMessageType();
    auditReqMsg.setAssertion(assertion);
    auditReqMsg.setAdhocQueryRequest(message);

    AcknowledgementType ack = logDocQueryRequest(auditReqMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

    getLog().debug("Exiting DocQueryAuditLog.audit (entity)...");
    return ack;
  }

  /**
   * This method will log Audit Query Requests received on the NHIN Proxy Interface
   *
   * @param auditMsg The Audit Query Request message to be audit logged.
   * @param assertion Assertion received
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType auditDQResponse(AdhocQueryResponse msg, AssertionType assertion, String direction, String _interface) {
    getLog().debug("Entering DocQueryAuditLog.auditDQResponse()...");

    AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
    auditMsg.setAssertion(assertion);
    auditMsg.setAdhocQueryResponse(msg);

    AcknowledgementType ack = logDocQueryResponse(auditMsg, direction, _interface);

    getLog().debug("Exiting DocQueryAuditLog.auditDQResponse()...");
    return ack;
  }

  /**
   * This method will log Document Query Requests sent on the Nhin Interface
   *
   * @param auditMsg The Document Query Request message to be audit logged.
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType audit(
          RespondingGatewayCrossGatewayQueryRequestType auditMsg) {
    getLog().debug("Entering DocQueryAuditLog.audit (proxy)...");

    AdhocQueryMessageType auditReqMsg = new AdhocQueryMessageType();
    auditReqMsg.setAssertion(auditMsg.getAssertion());
    auditReqMsg.setAdhocQueryRequest(auditMsg.getAdhocQueryRequest());

    AcknowledgementType ack = logDocQueryRequest(auditReqMsg,
                                                 NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                                                 NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

    getLog().debug("Exiting DocQueryAuditLog.audit (proxy)...");
    return ack;
  }

  /**
   * This method will log Audit Query Requests received on the NHIN Proxy Interface
   *
   * @param auditMsg The Audit Query Request message to be audit logged.
   * @param assertion Assertion received
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType auditDQRequest(
          AdhocQueryRequest msg, AssertionType assertion, String direction, String _interface) {
    getLog().debug("Entering DocQueryAuditLog.auditDQRequest()...");

    AdhocQueryMessageType auditMsg = new AdhocQueryMessageType();
    auditMsg.setAssertion(assertion);
    auditMsg.setAdhocQueryRequest(msg);

    AcknowledgementType ack = logDocQueryRequest(auditMsg, direction, _interface);

    getLog().debug("Exiting DocQueryAuditLog.auditDQRequest()...");
    return ack;
  }

  /**
   * This method will log Document Query Requests received/sent on a particular public interface
   *
   * @param message The Document Query Request message to be audit logged.
   * @param direction  The direction this message is going (Inbound or Outbound)
   * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  private AcknowledgementType logDocQueryRequest(AdhocQueryMessageType message, String direction, String _interface) {
    getLog().debug("Entering DocQueryAuditLog.logDocQuery(...)...");
    AcknowledgementType ack = new AcknowledgementType();
    AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
    LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message, direction, _interface);

    if (auditLogMsg != null) {
      AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
      AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
      ack = proxy.auditLog(auditLogMsg, message.getAssertion());
    }
    getLog().debug("Exiting DocQueryAuditLog.logDocQuery(...)...");
    return ack;
  }

  /**
   * This method will log Document Query Responses received/sent on a particular public interface
   *
   * @param message The Document Query Response message to be audit logged.
   * @param direction  The direction this message is going (Inbound or Outbound)
   * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType logDocQueryResponse(
          AdhocQueryResponseMessageType message, String direction, String _interface) {
    getLog().debug("Entering DocQueryAuditLog.auditResponse(...)...");
    AcknowledgementType ack = new AcknowledgementType();
    AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
    LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryResult(message, direction, _interface);

    if (auditLogMsg != null) {
      AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
      AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
      ack = proxy.auditLog(auditLogMsg, message.getAssertion());
    }

    getLog().debug("Exiting DocQueryAuditLog.auditResponse(...)...");
    return ack;
  }

  /**
   * This method will log Document Query Responses received/sent on a particular public interface
   *
   * @param message The Document Query Response message to be audit logged.
   * @param direction  The direction this message is going (Inbound or Outbound)
   * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
   * @return An acknowledgment of whether or not the message was successfully logged.
   */
  public AcknowledgementType logDocQueryAck(
          DocQueryAcknowledgementType message, AssertionType assertion, String direction, String _interface) {
    getLog().debug("Entering DocQueryAuditLog.auditResponse(...)...");
    AcknowledgementType ack = new AcknowledgementType();
    AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
    LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryDeferredAck(message, assertion, direction, _interface);

    if (auditLogMsg != null) {
      AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
      AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
      ack = proxy.auditLog(auditLogMsg, assertion);
    }

    getLog().debug("Exiting DocQueryAuditLog.auditResponse(...)...");
    return ack;
  }

  protected Log getLog() {
    return LOG;
  }
}
