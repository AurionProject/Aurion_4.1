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
package org.alembic.aurion.hiem.dte;

//import org.alembic.aurion.subscription.repository.data.SubscriptionItem;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.alembic.aurion.xmlCommon.XmlUtility;

/**
 *
 * @author rayj
 */
public class SubscriptionReferenceHelper {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionReferenceHelper.class);

    /**
     * @deprecated
     * @param subscribeResponseXml
     * @return
     */

    public SubscribeResponse createSubscribeResponseFromXml(Element subscribeResponseXml) {
        SubscribeResponse subscribeResponse = new SubscribeResponse();

        Element endpointReferenceXml = null;
        endpointReferenceXml = XmlUtility.getSingleChildElement(subscribeResponseXml, Namespaces.WSNT, "SubscriptionReference");

        EndpointReferenceHelper endpointReferenceHelper = new EndpointReferenceHelper();
        EndpointReferenceType subscriptionReference = endpointReferenceHelper.createEndpointReference(endpointReferenceXml);
        subscribeResponse.setSubscriptionReference(subscriptionReference);

        //todo: handle "CurrentTime"
        //todo: handle "TerminationTime"

        return subscribeResponse;
    }
}
