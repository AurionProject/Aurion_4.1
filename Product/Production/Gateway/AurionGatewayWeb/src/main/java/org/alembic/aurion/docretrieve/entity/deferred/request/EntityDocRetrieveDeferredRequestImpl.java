/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.request;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRequestImpl {

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, null);
        getSoapLogger().logRawAssertion(assertion);
        RetrieveDocumentSetRequestType message = body.getRetrieveDocumentSetRequest();
        NhinTargetCommunitiesType target = body.getNhinTargetCommunities();
        return new EntityDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     *
     * @param crossGatewayRetrieveRequest
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        RetrieveDocumentSetRequestType message = crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
        NhinTargetCommunitiesType target = crossGatewayRetrieveRequest.getNhinTargetCommunities();
        AssertionType assertion = extractAssertionFromContext(context, crossGatewayRetrieveRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        return new EntityDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     * 
     * @param context
     * @param oAssertionIn
     * @return AssertionType
     */
    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

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
