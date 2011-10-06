/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
public class PassthruDocRetrieveDeferredResponseImpl {

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionInfo(context, null);
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
        NhinTargetSystemType nhinTargetSystem = body.getNhinTargetSystem();
        return new NhincProxyDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetSystem);
    }

    /**
     * 
     * @param crossGatewayRetrieveResponse
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse, WebServiceContext context) {
        AssertionType assertion = extractAssertionInfo(context, crossGatewayRetrieveResponse.getAssertion());
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse();
        NhinTargetSystemType nhinTargetSystem = crossGatewayRetrieveResponse.getNhinTargetSystem();
        return new NhincProxyDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse, assertion, nhinTargetSystem);
    }

    /**
     * 
     * @param context
     * @param oAssertionIn
     * @return AssertionType
     */
    private AssertionType extractAssertionInfo(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (null == oAssertionIn) {
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
}
