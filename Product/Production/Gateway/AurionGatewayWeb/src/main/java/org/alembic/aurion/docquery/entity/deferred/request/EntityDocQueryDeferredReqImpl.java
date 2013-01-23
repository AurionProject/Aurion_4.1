/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 * This abstract class contains business logic common to Entity Secured and Unsecured services for Document Query Deferred Request message
 * @author Mark Goldman
 */
public abstract class EntityDocQueryDeferredReqImpl {

  private EntityDocQueryDeferredReqOrchImpl orchImpl;

  /**
   *
   * @param context
   * @return
   */
  protected AssertionType extractAssertion(WebServiceContext context) {
    AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
    return assertion;
  }

  /**
   *
   * @param assertion
   * @param context
   */
  protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
    if (assertion != null) {
      assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
    }
  }

  /**
   *
   * @param body
   * @param context
   * @return
   */
  protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
        final RespondingGatewayCrossGatewayQueryRequestType body, final WebServiceContext context) {
        AssertionType assertion = body.getAssertion();
        getSoapLogger().logRawAssertion(assertion);
        setMessageID(assertion, context);
        return getOrchImpl().respondingGatewayCrossGatewayQuery(
            body.getAdhocQueryRequest(), assertion, body.getNhinTargetCommunities());
  }

  /**
   *
   * @param body
   * @param context
   * @return
   */
  protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          final RespondingGatewayCrossGatewayQuerySecuredRequestType body, final WebServiceContext context) {
    AssertionType assertion = extractAssertion(context);
    getSoapLogger().logRawAssertion(assertion);
    setMessageID(assertion, context);
    return getOrchImpl().respondingGatewayCrossGatewayQuery(
            body.getAdhocQueryRequest(), assertion, body.getNhinTargetCommunities());
  }

  /**
   * Create an instance of EntityDocRetrieveDeferredReqImpl Class
   * @return EntityDocRetrieveDeferredReqImpl
   */
  protected EntityDocQueryDeferredReqOrchImpl getOrchImpl() {
    if (orchImpl == null) {
      orchImpl = new EntityDocQueryDeferredReqOrchImpl();
    }
    return orchImpl;
  }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
