/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterHiemSubscribeProxyNoOpImpl implements AdapterHiemSubscribeProxy {
    public SubscribeResponse subscribe(Element subscribe, AssertionType assertion) throws Exception {
        return new SubscribeResponse();
    }
}
