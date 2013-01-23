/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.unsubscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.alembic.aurion.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.util.soap.SoapLogger;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;

/**
 *
 * @author jhoppesc
 */
public class HiemUnsubscribeImpl {


    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        UnsubscribeResponse response;
        try {
            NhinHiemUnsubscribeOrchImpl unsubscribeOrchImpl = new NhinHiemUnsubscribeOrchImpl();
            AssertionType assertion = getAssertion(context, null);
            getSoapLogger().logRawAssertion(assertion);
            ReferenceParametersElements referenceParametersElements = getRefParams(context);
            response = unsubscribeOrchImpl.unsubscribe(unsubscribeRequest, referenceParametersElements, assertion);
        } catch (Exception ex) {
            throw new SubscriptionManagerSoapFaultFactory().getGenericProcessingExceptionFault(ex);
        }
        return response;
    }

    private ReferenceParametersElements getRefParams (WebServiceContext context) {
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG);
        return referenceParametersElements;
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

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
