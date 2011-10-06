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

package org.alembic.aurion.admindistribution;

import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {
    private Log log = null;

    public AdminDistributionHelper()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public NhinTargetSystemType createNhinTargetSystemType(String homeCommunityId)
    {
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(homeCommunityId);

        return createNhinTargetSystemType(hc);
    }
    public NhinTargetSystemType createNhinTargetSystemType(HomeCommunityType hc)
    {
        NhinTargetSystemType result = new NhinTargetSystemType();
        result.setHomeCommunity(hc);

        return result;
    }
    public String getLocalCommunityId()
    {

        String result = "";
        try
        {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }
        catch(Exception ex)
        {
            log.error("Unable to retrieve local home community id from Gateway.properties");
            log.error(ex);
        }
        return result;
    }
    public String getUrl(String targetHCID, String targetSystem) {
        String url = null;

        if (targetHCID != null) {
            try {

                NhinTargetSystemType ts = this.createNhinTargetSystemType(targetHCID);

                url = getWebServiceProxyHelper().getUrlFromTargetSystem(ts, targetSystem);
            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
    public String getUrl(NhinTargetSystemType target, String targetSystem) {
        String url = null;

        if (target != null) {
            try {


                url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, targetSystem);

            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    public boolean isInPassThroughMode()
    {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_PASSTHRU_PROPERTY);
    }
    public boolean isServiceEnabled()
    {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_ENABLED);
    }
    public boolean readBooleanGatewayProperty(String propertyName)
    {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + propertyName + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return result;
    }

}
