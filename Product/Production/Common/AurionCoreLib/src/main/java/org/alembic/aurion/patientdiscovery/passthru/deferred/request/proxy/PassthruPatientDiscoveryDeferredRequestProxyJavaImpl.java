/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.passthru.deferred.request.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.patientdiscovery.passthru.deferred.request.PassthruPatientDiscoveryDeferredRequestOrchImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class PassthruPatientDiscoveryDeferredRequestProxyJavaImpl implements PassthruPatientDiscoveryDeferredRequestProxy
{

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetSystemType target)
    {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        PassthruPatientDiscoveryDeferredRequestOrchImpl orchImpl = new PassthruPatientDiscoveryDeferredRequestOrchImpl();
        response = orchImpl.processPatientDiscoveryAsyncReq(message, assertion, target);

        return response;
    }
}
