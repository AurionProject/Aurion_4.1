/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.fta;

import org.alembic.aurion.adapternotificationconsumer.AdapterNotificationConsumerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterNotificationConsumer", portName = "AdapterNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.adapternotificationconsumer.AdapterNotificationConsumerPortType", targetNamespace = "urn:org:alembic:aurion:adapternotificationconsumer", wsdlLocation = "META-INF/wsdl/AdapterFTAService/AdapterNotificationConsumer.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterFTAService implements AdapterNotificationConsumerPortType {

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notify(org.alembic.aurion.common.nhinccommonadapter.NotifyRequestType notifyRequest) {
        return NotificationImpl.processNotify(notifyRequest);
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(org.alembic.aurion.common.nhinccommonadapter.NotifySubscribersOfDocumentRequestType notifySubscribersOfDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfCdcBioPackage(org.alembic.aurion.common.nhinccommonadapter.NotifySubscribersOfCdcBioPackageRequestType notifySubscribersOfCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
