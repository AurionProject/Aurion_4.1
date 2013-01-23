/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.unsubscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.alembic.aurion.common.nhinccommonentity.UnsubscribeRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;

/**
 *
 * @author rayj
 */
public class EntitySubscriptionManagerImpl
{

    private static Log log = LogFactory.getLog(EntitySubscriptionManagerImpl.class);

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault, UnableToDestroySubscriptionFault
    {
        AssertionType assertion = getAssertion(context, unsubscribeRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements referenceParametersElements = getRefParams(context);

        UnsubscribeResponse response = new EntityUnsubscribeOrchImpl().unsubscribe(unsubscribeRequest.getUnsubscribe(), assertion, null, referenceParametersElements);
        
        return response;
    }

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context) throws org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault, UnableToDestroySubscriptionFault
    {

        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements referenceParametersElements = getRefParams(context);

        UnsubscribeResponse response = new EntityUnsubscribeOrchImpl().unsubscribe(unsubscribeRequest, assertion, null, referenceParametersElements);

        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    private ReferenceParametersElements getRefParams(WebServiceContext context) {
        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");
        return referenceParametersElements;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
