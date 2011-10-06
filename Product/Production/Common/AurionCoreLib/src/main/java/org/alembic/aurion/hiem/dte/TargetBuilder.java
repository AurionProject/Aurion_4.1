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
package org.alembic.aurion.hiem.dte;

import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.alembic.aurion.xmlCommon.XpathHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class TargetBuilder {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TargetBuilder.class);

    public NhinTargetSystemType buildSubscriptionManagerTarget(String subscriptionReferenceXml) throws XPathExpressionException {
        String xpathQuery = "//*[local-name()='Address']";
        NhinTargetSystemType target = null;
        Element subscriptionReferenceAddressElement;
        subscriptionReferenceAddressElement = (Element) XpathHelper.performXpathQuery(subscriptionReferenceXml, xpathQuery);
        String subscriptionReferenceAddress = XmlUtility.getNodeValue(subscriptionReferenceAddressElement);
        target = new NhinTargetSystemType();
        target.setUrl(subscriptionReferenceAddress);
        return target;
    }
}
