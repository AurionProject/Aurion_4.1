/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.nhin.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredRequestImpl {
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            getSoapLogger().logRawAssertion(assertion);
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new NhinDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(body, assertion);

    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
