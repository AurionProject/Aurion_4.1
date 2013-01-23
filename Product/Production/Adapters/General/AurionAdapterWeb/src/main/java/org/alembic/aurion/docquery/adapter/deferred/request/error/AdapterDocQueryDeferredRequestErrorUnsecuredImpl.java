/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.docquery.adapter.deferred.request.error;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorUnsecuredImpl {
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdapterDocumentQueryDeferredRequestErrorType body, WebServiceContext context) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            getSoapLogger().logRawAssertion(body.getAssertion());
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestErrorOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), body.getAssertion(), body.getErrorMsg());
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
