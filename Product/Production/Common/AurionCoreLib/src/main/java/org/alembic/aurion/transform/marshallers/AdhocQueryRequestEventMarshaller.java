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

package org.alembic.aurion.transform.marshallers;

import org.alembic.aurion.common.eventcommon.AdhocQueryRequestEventType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AdhocQueryRequestEventMarshaller {
    private static final String AdhocQueryRequestEventContextPath = "org.alembic.aurion.common.eventcommon";

    public Element marshalAdhocQueryRequestEvent(AdhocQueryRequestEventType object) {
        return new Marshaller().marshal(object, AdhocQueryRequestEventContextPath);
    }

    public AdhocQueryRequestEventType unmarshalAdhocQueryRequestEvent(Element element) {
        return (AdhocQueryRequestEventType) new Marshaller().unmarshal(element, AdhocQueryRequestEventContextPath);
    }
}
