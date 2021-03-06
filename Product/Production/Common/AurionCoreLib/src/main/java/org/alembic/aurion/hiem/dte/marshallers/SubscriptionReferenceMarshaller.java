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

import org.alembic.aurion.xmlCommon.XmlUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class SubscriptionReferenceMarshaller {

    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(EndpointReferenceType object) {
        org.oasis_open.docs.wsn.b_2.ObjectFactory objectFactory = new org.oasis_open.docs.wsn.b_2.ObjectFactory();
        JAXBElement<EndpointReferenceType> jaxb = objectFactory.createSubscriptionReference(object);
        Marshaller marshaller = new Marshaller();
        Element element = marshaller.marshal(jaxb, ContextPath);
        return element;
    }

    public EndpointReferenceType unmarshal(Element element) {
        EndpointReferenceMarshaller marshaller = new EndpointReferenceMarshaller();
        return marshaller.unmarshal(element, ContextPath);
    }
    public EndpointReferenceType unmarshal(String xml) {
        EndpointReferenceMarshaller marshaller = new EndpointReferenceMarshaller();
        return marshaller.unmarshal(xml, ContextPath);
    }
}
