/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.AdapterRegistryResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterXDRResponseImpl
{
    private Log log = null;

    public AdapterXDRResponseImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRResponseImpl.provideAndRegisterDocumentSetBResponse(unsecured)");
        XDRAcknowledgementType response = null;

        RegistryResponseType regResponse = null;
        AssertionType assertion = null;
        if (body != null)
        {
            regResponse = body.getRegistryResponse();
            assertion = body.getAssertion();
        }
        assertion = getAssertion(context, assertion);
        getSoapLogger().logRawAssertion(assertion);
        response = provideAndRegisterDocumentSetBResponse(regResponse, assertion);

        log.debug("End AdapterXDRResponseImpl.provideAndRegisterDocumentSetBResponse(unsecured)");
        return response;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRResponseImpl.provideAndRegisterDocumentSetBResponse(secured)");
        XDRAcknowledgementType response = null;

        AssertionType assertion = null;
        assertion = getAssertion(context, assertion);
        getSoapLogger().logRawAssertion(assertion);
        response = provideAndRegisterDocumentSetBResponse(body, assertion);

        log.debug("End AdapterXDRResponseImpl.provideAndRegisterDocumentSetBResponse(secured)");
        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
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
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList))
            {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }

    protected XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse, AssertionType assertion)
    {
        log.debug("Begin AdapterXDRResponseImpl.provideAndRegisterDocumentSetBResponse");
        return new AdapterDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(regResponse, assertion);
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
