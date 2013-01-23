/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocRetrieveDeferredRequestImpl {
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);

        return new AdapterDocRetrieveDeferredReqOrchImpl().respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion);
    }

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, crossGatewayRetrieveRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);

        return new AdapterDocRetrieveDeferredReqOrchImpl().respondingGatewayCrossGatewayRetrieve(crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest(), assertion);
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
