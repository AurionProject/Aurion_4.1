/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.unsubscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.w3c.dom.Element;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author rayj
 */
public interface AdapterHiemUnsubscribeProxy {

    public UnsubscribeResponse unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion);
}
