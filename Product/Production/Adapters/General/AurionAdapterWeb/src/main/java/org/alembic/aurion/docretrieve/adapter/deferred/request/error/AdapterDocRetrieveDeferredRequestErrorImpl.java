/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.request.error;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocRetrieveDeferredRequestErrorImpl {
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        AdapterDocumentRetrieveDeferredRequestErrorType   request = new AdapterDocumentRetrieveDeferredRequestErrorType();

        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());
        request.setErrorMsg(body.getErrorMsg());

        return new AdapterDocRetrieveDeferredReqErrorOrchImpl().respondingGatewayCrossGatewayRetrieve(request, assertion, body.getErrorMsg());
    }

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(AdapterDocumentRetrieveDeferredRequestErrorType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        AdapterDocumentRetrieveDeferredRequestErrorType   request = new AdapterDocumentRetrieveDeferredRequestErrorType();

        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());
        request.setErrorMsg(body.getErrorMsg());

        return new AdapterDocRetrieveDeferredReqErrorOrchImpl().respondingGatewayCrossGatewayRetrieve(request, assertion, body.getErrorMsg());
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
