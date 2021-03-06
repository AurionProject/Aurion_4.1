/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.dte;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 *
 * @author Neil Webb
 */
public class SubscribeResponseMarshaller {

    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";
    private static final String NotifyRespContextPath = "org.alembic.aurion.common.nhinccommon";

    public Element marshalSubscribeRequest(SubscribeResponse object) {
        return new MarshallerHelper().marshal(object, ContextPath);
    }

    public SubscribeResponse unmarshalSubscribeRequest(Element element) {
        return (SubscribeResponse) new MarshallerHelper().unmarshal(element, ContextPath);
    }

    public Element marshalNotifyRequest(AcknowledgementType object) {
        Element result = null;
        if(object != null)
        {
            org.alembic.aurion.common.nhinccommon.ObjectFactory nccommonObjFact = new org.alembic.aurion.common.nhinccommon.ObjectFactory();
            Object jaxbObj = nccommonObjFact.createAcknowledgement(object);
            result = new MarshallerHelper().marshal(jaxbObj, NotifyRespContextPath);
        }
        return result;
    }

    public AcknowledgementType unmarshalNotifyRequest(Element element) {
        return (AcknowledgementType) new MarshallerHelper().unmarshal(element, NotifyRespContextPath);
    }
}
