/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterXDRRequestImpl
{
    private Log log = null;

    public AdapterXDRRequestImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRRequestImpl.provideAndRegisterDocumentSetBRequest(unsecure)");
        XDRAcknowledgementType response = null;

        ProvideAndRegisterDocumentSetRequestType request = null;
        String liftURL = null;
        AssertionType assertion = null;
        if (body != null)
        {
            request = body.getProvideAndRegisterDocumentSetRequest();
            liftURL = body.getUrl();
            assertion = body.getAssertion();
        }
        assertion = getAssertion(context, assertion);

        response = provideAndRegisterDocumentSetBRequest(request, liftURL, assertion);
        log.debug("End AdapterXDRRequestImpl.provideAndRegisterDocumentSetBRequest(unsecure)");
        return response;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRRequestImpl.provideAndRegisterDocumentSetBRequest(secure)");
        XDRAcknowledgementType response = null;

        ProvideAndRegisterDocumentSetRequestType request = null;
        String liftURL = null;
        AssertionType assertion = null;
        if (body != null)
        {
            request = body.getProvideAndRegisterDocumentSetRequest();
            liftURL = body.getUrl();
        }
        assertion = getAssertion(context, assertion);

        response = provideAndRegisterDocumentSetBRequest(request, liftURL, assertion);
        log.debug("End AdapterXDRRequestImpl.provideAndRegisterDocumentSetBRequest(secure)");
        return response;
    }

    protected AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
    {
        AssertionType assertion = null;
        if (oAssertionIn == null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    protected XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, String liftURL, AssertionType assertion)
    {
        return new AdapterDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBRequest(request, liftURL, assertion);
    }
}
