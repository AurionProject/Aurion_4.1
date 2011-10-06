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

package org.alembic.aurion.adaptersubscription;

import org.alembic.aurion.adaptersubscriptionmanagement.AdapterNotificationProducerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterNotificationProducer", portName = "AdapterNotificationProducerPortSoap11", endpointInterface = "org.alembic.aurion.adaptersubscriptionmanagement.AdapterNotificationProducerPortType", targetNamespace = "urn:org:alembic:aurion:adaptersubscriptionmanagement", wsdlLocation = "META-INF/wsdl/AdapterSubscriptionService/AdapterSubscriptionManagement.wsdl")
@Stateless
public class AdapterSubscriptionService implements AdapterNotificationProducerPortType {

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.alembic.aurion.common.nhinccommonadapter.SubscribeRequestType subscribeRequest) {
        return AdapterSubscriptionHelper.subscribe(subscribeRequest);
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(org.alembic.aurion.common.nhinccommonadapter.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeCdcBioPackage(org.alembic.aurion.common.nhinccommonadapter.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
