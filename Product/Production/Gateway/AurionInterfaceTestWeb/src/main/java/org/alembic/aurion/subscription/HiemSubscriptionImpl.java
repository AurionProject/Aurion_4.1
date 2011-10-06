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
package org.alembic.aurion.subscription;

import org.alembic.aurion.common.nhinccommoninternalorch.SubscribeRequestType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.nhincsubscription.NhincNotificationProducerService;
import org.alembic.aurion.nhincsubscription.NotificationProducer;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author jhoppesc
 */
public class HiemSubscriptionImpl {

    private static Log log = LogFactory.getLog(HiemSubscriptionImpl.class);
    private static final String SERVICE_NAME = "mocknotificationproducer";

    public static SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context) {
        log.debug("Entering HiemSubscriptionImpl.subscribe");

        SubscribeResponse resp = new SubscribeResponse();
        SubscribeRequestType request = new SubscribeRequestType();

        request.setSubscribe(subscribeRequest);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));


        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincNotificationProducerService service = new NhincNotificationProducerService();
            NotificationProducer port = service.getNotificationProducerPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

            try {
                resp = port.subscribe(request);
            } catch (Exception e) {
                log.error("Received Fault: " + e.getMessage());
                resp = null;
            }
        } else {
            resp = null;
        }

        log.debug("Exiting HiemSubscriptionImpl.subscribe");
        return resp;
    }
}
