/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredResponseUnsecuredImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryDeferredResponseUnsecuredImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryResponseType body, WebServiceContext context) {
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            getSoapLogger().logRawAssertion(body.getAssertion());
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = msgIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               body.getAssertion().getRelatesToList().add(msgIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return new AdapterDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), body.getAssertion());
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
