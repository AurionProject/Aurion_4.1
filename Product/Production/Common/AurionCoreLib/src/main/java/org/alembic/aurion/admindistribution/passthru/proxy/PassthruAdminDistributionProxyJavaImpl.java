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

package org.alembic.aurion.admindistribution.passthru.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.admindistribution.passthru.PassthruAdminDistributionOrchImpl;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyJavaImpl implements PassthruAdminDistributionProxy{
   private Log log = null;

    public PassthruAdminDistributionProxyJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target)
    {
        log.info("begin sendAlert");
        this.getNhincAdminDistImpl().sendAlertMessage(body, assertion, target);

    }
    protected PassthruAdminDistributionOrchImpl getNhincAdminDistImpl()
    {
        return new PassthruAdminDistributionOrchImpl();
    }
}
