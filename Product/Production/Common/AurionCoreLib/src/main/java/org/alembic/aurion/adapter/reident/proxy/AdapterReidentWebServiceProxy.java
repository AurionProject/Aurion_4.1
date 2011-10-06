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
package org.alembic.aurion.adapter.reident.proxy;

import org.alembic.aurion.adapterreidentification.AdapterReidentification;
import org.alembic.aurion.adapterreidentification.AdapterReidentificationPortType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class AdapterReidentWebServiceProxy implements AdapterReidentProxy {

    private static Log log = LogFactory.getLog(AdapterReidentWebServiceProxy.class);
    static AdapterReidentification service = new AdapterReidentification();

    public PRPAIN201310UV02 getRealIdentifier(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType target) {
        String url = null;
        PRPAIN201310UV02 result = new PRPAIN201310UV02();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.SUBJECT_DISCOVERY_REIDENT_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.SUBJECT_DISCOVERY_REIDENT_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AdapterReidentificationPortType port = getPort(url);

            PIXConsumerPRPAIN201309UVRequestType reidentRequest = new PIXConsumerPRPAIN201309UVRequestType();
            reidentRequest.setAssertion(assertion);
            reidentRequest.setNhinTargetCommunities(target);
            reidentRequest.setPRPAIN201309UV02(request);

            PIXConsumerPRPAIN201310UVRequestType reidentResp = port.getRealIdentifier(reidentRequest);

            if (reidentResp != null &&
                    reidentResp.getPRPAIN201310UV02() != null) {
                result = reidentResp.getPRPAIN201310UV02();
            }
        }

        return result;
    }

    private AdapterReidentificationPortType getPort(String url) {
        AdapterReidentificationPortType port = service.getAdapterReidentificationBindingSoap();

        log.info("Setting endpoint address to Adapter Reidentification Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }
}
