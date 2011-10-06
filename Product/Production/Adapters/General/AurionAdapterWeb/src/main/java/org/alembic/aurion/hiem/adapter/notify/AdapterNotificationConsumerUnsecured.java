/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.notify;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterNotificationConsumer", portName = "AdapterNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.adapternotificationconsumer.AdapterNotificationConsumerPortType", targetNamespace = "urn:org:alembic:aurion:adapternotificationconsumer", wsdlLocation = "WEB-INF/wsdl/AdapterNotificationConsumerUnsecured/AdapterNotificationConsumer.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterNotificationConsumerUnsecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notify(org.alembic.aurion.common.nhinccommonadapter.NotifyRequestType notifyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
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
