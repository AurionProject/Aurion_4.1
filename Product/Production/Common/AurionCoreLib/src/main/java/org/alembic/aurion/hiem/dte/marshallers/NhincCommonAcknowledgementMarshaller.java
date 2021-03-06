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
package org.alembic.aurion.hiem.dte.marshallers;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NhincCommonAcknowledgementMarshaller {

    private static final String ContextPath = "org.alembic.aurion.common.nhinccommon";

    public Element marshal(AcknowledgementType object) {
//        return new Marshaller().marshal(object, NhincCommonAcknowledgementContextPath);

        org.alembic.aurion.common.nhinccommon.ObjectFactory objectFactory = new org.alembic.aurion.common.nhinccommon.ObjectFactory();
        JAXBElement<AcknowledgementType> jaxb = objectFactory.createAcknowledgement(object);
        Marshaller marshaller = new Marshaller();
        Element element = marshaller.marshal(jaxb, ContextPath);
        return element;
    }

    public AcknowledgementType unmarshal(Element element) {
        //return (AcknowledgementType) new Marshaller().unmarshal(element, NhincCommonAcknowledgementContextPath);
        return (AcknowledgementType) new Marshaller().unmarshallJaxbElement(element, ContextPath);
    }
}
