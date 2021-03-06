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

import org.w3c.dom.Element;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;

/**
 *
 * @author rayj
 */
public class CheckPolicyRequestMarshaller {
    private static final String CheckPolicyRequestContextPath = "org.alembic.aurion.common.nhinccommonadapter";

    public Element marshalCheckPolicyRequest(CheckPolicyRequestType object) {
        return new Marshaller().marshal(object, CheckPolicyRequestContextPath);
    }

    public CheckPolicyRequestType unmarshalCheckPolicyRequest(Element element) {
        return (CheckPolicyRequestType) new Marshaller().unmarshal(element, CheckPolicyRequestContextPath);
    }
}
