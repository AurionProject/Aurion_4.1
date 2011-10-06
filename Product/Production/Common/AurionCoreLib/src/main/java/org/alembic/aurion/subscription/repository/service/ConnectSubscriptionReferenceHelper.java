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
package org.alembic.aurion.subscription.repository.service;

import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.hiem.dte.EndpointReferenceHelper;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.w3._2005._08.addressing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class ConnectSubscriptionReferenceHelper {

    public static final String REFERENCE_PARAMETER_SUBSCRIPTION_ID_NAMESPACE = "http://www.hhs.gov/healthit/nhin";
    public static final String REFERENCE_PARAMETER_SUBSCRIPTION_ID_ELEMENT_NAME = "SubscriptionId";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(HiemSubscriptionRepositoryService.class);

    public EndpointReferenceType createSubscriptionReference(String subscriptionId) {
        String url = getSubscriptionManagerEndpointAddress();
        EndpointReferenceHelper helper = new EndpointReferenceHelper();
        EndpointReferenceType subscriptionReference = helper.createEndpointReferenceAddressOnly(url);
        helper.attachSimpleReferenceParameter(subscriptionReference, REFERENCE_PARAMETER_SUBSCRIPTION_ID_NAMESPACE, REFERENCE_PARAMETER_SUBSCRIPTION_ID_ELEMENT_NAME, subscriptionId);
        return subscriptionReference;
    }

    //todo: move to common location
    private String getSubscriptionManagerEndpointAddress() {
        String subMgrUrl = null;
        String homeCommunityId = null;
        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            homeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + homeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(homeCommunityId)) {
            try {
                subMgrUrl = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunityId, NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME + " for community id: " + homeCommunityId);
                log.error(ex.getMessage());
            }
        }
        return subMgrUrl;
    }
}
