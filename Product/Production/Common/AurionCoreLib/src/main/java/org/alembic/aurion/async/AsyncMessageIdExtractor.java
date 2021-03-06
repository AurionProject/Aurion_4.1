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
package org.alembic.aurion.async;

import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.JAXWSProperties;
import org.alembic.aurion.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMessageIdExtractor
{

    public static String GetAsyncMessageId(WebServiceContext context)
    {
        String messageId = null;
        String tempMsgId = null;

        if (context != null && context.getMessageContext() != null)
        {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList)
            {
                HeaderList hlist = (HeaderList) oList;
                Header header = hlist.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_MESSAGEID, false);
                if (header != null)
                {
                    tempMsgId = header.getStringContent();
                }
            }
        }

        if (tempMsgId == null)
        {
            tempMsgId = UUID.randomUUID().toString();
        }

        // Check for the presence of uuid
        if (!(tempMsgId.contains("uuid"))) {
            messageId = "urn:uuid:" + tempMsgId;
        }
        else {
            messageId = tempMsgId;
        }

        return messageId;
    }

    public static List<String> GetAsyncRelatesTo(WebServiceContext context)
    {
        List<String> relatesToId = new ArrayList<String>();

        if (context != null && context.getMessageContext() != null)
        {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList)
            {
                HeaderList hlist = (HeaderList) oList;
                Iterator<Header> allRelatesToHeaders = hlist.getHeaders(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_RELATESTO, false);
                while (allRelatesToHeaders.hasNext())
                {
                    Header header = allRelatesToHeaders.next();

                    if (header != null)
                    {
                        relatesToId.add(header.getStringContent());
                    }
                }
            }
        }
        return relatesToId;
    }

    public static String GetToURL(WebServiceContext context)
    {
        String toURL = null;

        if (context != null && context.getMessageContext() != null)
        {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList)
            {
                HeaderList hlist = (HeaderList) oList;
                Header header = hlist.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_TO, false);
                if (header != null)
                {
                    toURL = header.getStringContent();
                }
            }
        }
        return toURL;
    }
}
