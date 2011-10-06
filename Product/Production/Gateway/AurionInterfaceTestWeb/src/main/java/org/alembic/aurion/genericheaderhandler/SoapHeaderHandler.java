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

package org.alembic.aurion.genericheaderhandler;

import com.sun.xml.ws.security.opt.impl.util.SOAPUtil;
import org.alembic.aurion.nhinclib.NhincConstants;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author rayj
 */
public class SoapHeaderHandler implements SOAPHandler<SOAPMessageContext> {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapHeaderHandler.class);

    public Set<QName> getHeaders() {
        log.debug("SoapHeaderHandler.getHeaders");
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        log.debug("SoapHeaderHandler.handleMessage");
        new org.alembic.aurion.hiem.dte.SoapUtil().extractReferenceParameters(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        log.warn("SoapHeaderHandler.handleFault");
        return true;
    }

    public void close(MessageContext context) {
        log.debug("SoapHeaderHandler.close");
    }

}
