/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.request.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestOrchImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class EntityPatientDiscoveryDeferredRequestProxyJavaImpl implements EntityPatientDiscoveryDeferredRequestProxy
{

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        EntityPatientDiscoveryDeferredRequestOrchImpl orchImpl = new EntityPatientDiscoveryDeferredRequestOrchImpl();
        response = orchImpl.processPatientDiscoveryAsyncReq(request, assertion, targets);

        return response;
    }
}
