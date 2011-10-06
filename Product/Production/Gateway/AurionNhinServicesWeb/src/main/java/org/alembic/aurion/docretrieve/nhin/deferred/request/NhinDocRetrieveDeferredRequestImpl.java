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

package org.alembic.aurion.docretrieve.nhin.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.async.AsyncMessageIdExtractor;

/**
 *
 * @author jhoppesc
 */
public class NhinDocRetrieveDeferredRequestImpl {
    public DocRetrieveAcknowledgementType respondingGatewayDeferredRequestCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new NhinDocRetrieveDeferredReqOrchImpl().sendToRespondingGateway(body, assertion);
    }

}
