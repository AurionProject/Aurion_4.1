/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.component.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.xml.ws.WebServiceContext;
import java.util.List;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author JHOPPESC
 */
public class AdapterComponentDocRetrieveDeferredResponseImpl {

    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, crossGatewayRetrieveResponse.getAssertion());
        getSoapLogger().logRawAssertion(assertion);

        return new AdapterComponentDocRetrieveDeferredRespOrchImpl().respondingGatewayCrossGatewayRetrieve(crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse(), assertion);
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = oAssertionIn;

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
