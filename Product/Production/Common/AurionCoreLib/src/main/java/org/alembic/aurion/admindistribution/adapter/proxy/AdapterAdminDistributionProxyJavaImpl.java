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

package org.alembic.aurion.admindistribution.adapter.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.admindistribution.adapter.AdapterAdminDistributionOrchImpl;
import org.alembic.aurion.common.nhinccommon.AssertionType;

/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyJavaImpl implements AdapterAdminDistributionProxy{
     private Log log = null;

    public AdapterAdminDistributionProxyJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion)
    {
        log.debug("Begin sendAlertMessage");
        getAdapterImplementation().sendAlertMessage(body, assertion);
    }
    protected AdapterAdminDistributionOrchImpl getAdapterImplementation()
    {
        return new org.alembic.aurion.admindistribution.adapter.AdapterAdminDistributionOrchImpl();
    }
}
