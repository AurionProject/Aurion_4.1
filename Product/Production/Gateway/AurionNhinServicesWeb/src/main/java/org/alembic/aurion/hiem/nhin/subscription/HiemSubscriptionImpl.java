/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.subscription;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.dte.WebServiceContextHelper;
import org.alembic.aurion.hiem.nhin.subscribe.NhinHiemSubscriptionOrchImpl;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 *
 * @author jhoppesc
 */
public class HiemSubscriptionImpl {

    private static Log log = LogFactory.getLog(HiemSubscriptionImpl.class);

    public SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault  {
        log.debug("Entering HiemSubscriptionImpl.subscribe");

        WebServiceContextHelper contextHelper = new WebServiceContextHelper();
        Element soapMessage = contextHelper.extractSoapMessage(context);

        NhinHiemSubscriptionOrchImpl subscribeOrchImpl = new NhinHiemSubscriptionOrchImpl();
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        
        SubscribeResponse response = subscribeOrchImpl.subscribe(subscribeRequest, soapMessage, assertion);

        log.debug("Exiting HiemSubscriptionImpl.subscribe");
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

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
