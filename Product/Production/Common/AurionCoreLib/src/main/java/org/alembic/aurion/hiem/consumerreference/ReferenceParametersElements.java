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
package org.alembic.aurion.hiem.consumerreference;

import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.xmlCommon.XmlUtility;
import java.util.ArrayList;
import java.util.List;
import javax.xml.soap.SOAPHeader;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class ReferenceParametersElements   {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapUtil.class);
    private List<Element> elements = null;

    public ReferenceParametersElements() {
        elements = new ArrayList();
    }

    public List<Element> getElements() {
        return elements;
    }
}
