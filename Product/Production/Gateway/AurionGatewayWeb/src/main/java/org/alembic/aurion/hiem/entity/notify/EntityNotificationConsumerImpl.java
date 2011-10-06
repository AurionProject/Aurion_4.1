/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.notify;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.NotifyRequestType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 * 
 * 
 * @author Neil Webb
 */
public class EntityNotificationConsumerImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityNotificationConsumerImpl.class);

    public AcknowledgementType notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        log.debug("EntityNotifyServiceImpl.notify");
        AssertionType assertion = getAssertion(context, notifyRequest.getAssertion());

        String rawNotifyXml = getRawXml(context);

        return new EntityHiemNotifyOrchImpl().notify(notifyRequest.getNotify(), assertion, null, rawNotifyXml);
    }

    public AcknowledgementType notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("EntityNotifyServiceImpl.notify");
        AssertionType assertion = getAssertion(context, null);

        String rawNotifyXml = getRawXml(context);

        return new EntityHiemNotifyOrchImpl().notify(notifyRequest, assertion, null, rawNotifyXml);
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

    private String getRawXml(WebServiceContext context) {
        String rawNotifyXml = new SoapUtil().extractSoapMessage(context, "notifySoapMessage");
        return rawNotifyXml;
    }
}
