/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.adapter.component.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestUnsecuredImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType body, WebServiceContext context) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            getSoapLogger().logRawAssertion(body.getAssertion());
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterComponentDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), body.getAssertion());
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
