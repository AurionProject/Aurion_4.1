/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredResponseSecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryDeferredResponseSecuredImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecureResponseType body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            getSoapLogger().logRawAssertion(assertion);
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = msgIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               assertion.getRelatesToList().add(msgIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return new AdapterDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), assertion);
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
