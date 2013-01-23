/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import org.alembic.aurion.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredResponseImpl
{

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, WebServiceContext context)
    {
        AssertionType assertion = extractAssertionFromContext(context, null);
        getSoapLogger().logRawAssertion(assertion);

        return new PassthruDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponseOrch(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterResponseRequest, WebServiceContext context)
    {
        AssertionType assertion = extractAssertionFromContext(context, provideAndRegisterResponseRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);

        return new PassthruDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());
    }

    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList))
            {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
