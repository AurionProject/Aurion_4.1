/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.util.soap.SoapLogger;



/**
 * The implementation for passthru service for secured Deferred Doc Query Request.
 *
 * @author Les Westberg
 */
public class PassthruDocQueryDeferredRequestSecuredImpl
{
    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestSecuredImpl.class);

    public DocQueryAcknowledgementType crossGatewayQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType crossGatewayQueryRequest, WebServiceContext context)
    {
        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        AssertionType assertion = extractAssertion(context);
        getSoapLogger().logRawAssertion(assertion);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(getAsyncMessageIdExtractor().GetAsyncMessageId(context));
        }

        // Fwd request to orchestrator
        DocQueryAcknowledgementType ackResponse = getPassthruDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(crossGatewayQueryRequest.getAdhocQueryRequest(),
                assertion, crossGatewayQueryRequest.getNhinTargetSystem());

        getLogger().debug("End of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        return ackResponse;
    }

    /**
     * Returns the static logger for this class
     * @return
     */
    protected Log getLogger()
    {
        return logger;
    }

    /**
     * Implementation class for
     *
     * @return
     */
    protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl()
    {
        return new PassthruDocQueryDeferredRequestOrchImpl();
    }

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }

    protected AsyncMessageIdExtractor getAsyncMessageIdExtractor()
    {
        return new AsyncMessageIdExtractor();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
