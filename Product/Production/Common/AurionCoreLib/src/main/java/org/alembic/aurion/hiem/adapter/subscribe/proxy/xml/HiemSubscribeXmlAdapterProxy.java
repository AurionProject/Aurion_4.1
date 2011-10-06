/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.adapter.subscribe.proxy.xml;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public interface HiemSubscribeXmlAdapterProxy {

    public Node subscribe(Node subscribe, AssertionType assertion, NhinTargetSystemType target) throws Exception;
}
