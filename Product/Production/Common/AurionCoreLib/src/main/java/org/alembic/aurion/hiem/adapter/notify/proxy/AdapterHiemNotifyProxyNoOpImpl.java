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

package org.alembic.aurion.hiem.adapter.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterHiemNotifyProxyNoOpImpl implements AdapterHiemNotifyProxy {

    public AcknowledgementType notify(Element notify, ReferenceParametersElements referenceParametersElements,AssertionType assertion) throws Exception {
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("Success");
        return ack;
    }

    public AcknowledgementType notifySubscribersOfDocument(Element docNotify, AssertionType assertion) throws Exception {
        return null;
    }

    public AcknowledgementType notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion) throws Exception {
        return null;
    }

}
