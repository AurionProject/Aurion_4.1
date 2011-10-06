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

package org.alembic.aurion.admindistribution.passthru;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.admindistribution.nhin.proxy.NhinAdminDistributionProxy;
import org.alembic.aurion.admindistribution.nhin.proxy.NhinAdminDistributionProxyObjectFactory;
import org.alembic.aurion.admindistribution.AdminDistributionAuditLogger;
import org.alembic.aurion.admindistribution.nhin.proxy.NhinAdminDistributionProxyWebServiceSecuredImpl;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.nhinclib.NhincConstants;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionOrchImpl {
   private Log log = null;

    public PassthruAdminDistributionOrchImpl()
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
        //TODO: LogRequest        
        AcknowledgementType ack = getLogger().auditNhincAdminDist(body, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        if (ack != null)
        {
            log.debug("ack: " + ack.getMessage());
        }

        getNhinProxy().sendAlertMessage(body, assertion, target);        

    }
    protected AdminDistributionAuditLogger getLogger()
    {
        return new AdminDistributionAuditLogger();
    }
    protected NhinAdminDistributionProxy getNhinProxy()
    {
        return new NhinAdminDistributionProxyObjectFactory().getNhinAdminDistProxy();
    }

}
