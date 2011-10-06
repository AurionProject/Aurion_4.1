/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommonproxy.NotifyRequestType;
import org.alembic.aurion.common.nhinccommonproxy.NotifyRequestSecuredType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.nhin.notify.proxy.NhinHiemNotifyProxy;
import org.alembic.aurion.hiem.nhin.notify.proxy.NhinHiemNotifyProxyObjectFactory;
import org.w3c.dom.Element;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.xmlCommon.XmlUtility;

/**
 *
 * @author jhoppesc
 */
public class ProxyHiemNotifyImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);

    public void notify(NotifyRequestType notifyRequest, WebServiceContext context)
    {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, notifyRequest.getAssertion(), notifyRequest.getNhinTargetSystem());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    public void notify(NotifyRequestSecuredType notifyRequest, WebServiceContext context)
    {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("NOTIFY MESSAGE RECEIVED FROM SECURED INTERFACE: " + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
//        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "notifySoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, SamlTokenExtractor.GetAssertion(context), notifyRequest.getNhinTargetSystem());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    private AcknowledgementType audit(NotifyRequestType notifyRequest)
    {
        AcknowledgementType ack = null;

        return ack;
    }

    private AcknowledgementType audit(NotifyRequestSecuredType notifyRequest)
    {
        AcknowledgementType ack = null;

        return ack;
    }
}
