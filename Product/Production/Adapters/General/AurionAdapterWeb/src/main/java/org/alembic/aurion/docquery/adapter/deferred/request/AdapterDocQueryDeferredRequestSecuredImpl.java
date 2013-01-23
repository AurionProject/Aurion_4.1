/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestSecuredImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecureRequestType body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        getSoapLogger().logRawAssertion(assertion);

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), assertion);
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
