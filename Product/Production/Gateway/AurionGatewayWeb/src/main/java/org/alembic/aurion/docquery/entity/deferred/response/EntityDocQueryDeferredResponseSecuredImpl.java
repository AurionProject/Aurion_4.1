/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseSecuredType;
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
public class EntityDocQueryDeferredResponseSecuredImpl {
    public DocQueryAcknowledgementType crossGatewayQueryResponse(RespondingGatewayCrossGatewayQueryResponseSecuredType body, WebServiceContext context) {
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

        return new EntityDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), assertion, body.getNhinTargetCommunities());
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
