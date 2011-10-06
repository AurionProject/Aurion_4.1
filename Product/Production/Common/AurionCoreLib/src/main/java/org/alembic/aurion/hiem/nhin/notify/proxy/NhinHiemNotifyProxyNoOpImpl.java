/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemNotifyProxyNoOpImpl implements NhinHiemNotifyProxy {

   public void notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,AssertionType assertion, NhinTargetSystemType target) {
        // Do nothing
        
    }
}
