/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.fta;

import org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterNotificationProducerSecured", portName = "AdapterNotificationProducerPortSecuredSoap", endpointInterface = "org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType", targetNamespace = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured", wsdlLocation = "META-INF/wsdl/AdapterSubscriptionManagementSecured/AdapterSubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterSubscriptionManagementSecured implements AdapterNotificationProducerPortSecuredType {

    private static Log log = LogFactory.getLog(AdapterSubcriptionManagement.class);

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest) {
        SubscribeResponse response = new SubscribeResponse();

        log.info("Received Subscribe Request: " + subscribeRequest);


        return response;
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(org.alembic.aurion.common.nhinccommonadapter.SubscribeDocumentRequestSecuredType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
