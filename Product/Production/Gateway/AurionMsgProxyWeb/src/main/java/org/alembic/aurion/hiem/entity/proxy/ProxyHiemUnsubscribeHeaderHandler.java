/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.alembic.aurion.hiem.dte.SoapUtil;

/**
 *
 * @author rayj
 */
public class ProxyHiemUnsubscribeHeaderHandler implements SOAPHandler<SOAPMessageContext>
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ProxyHiemUnsubscribeHeaderHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders()
    {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context)
    {
        new SoapUtil().extractReferenceParameters(context, "unsubscribeSoapMessage");
        return true;
    }

    public boolean handleFault(SOAPMessageContext context)
    {
        return true;
    }

    public void close(MessageContext context)
    {
    }
}
