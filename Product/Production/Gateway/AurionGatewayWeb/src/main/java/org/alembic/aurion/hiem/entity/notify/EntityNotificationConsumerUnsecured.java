/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.entity.notify;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityNotificationConsumer", portName = "EntityNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.entitynotificationconsumer.EntityNotificationConsumerPortType", targetNamespace = "urn:org:alembic:aurion:entitynotificationconsumer", wsdlLocation = "WEB-INF/wsdl/EntityNotificationConsumerUnsecured/EntityNotificationConsumer.wsdl")
@HandlerChain(file = "EntityNotifySoapHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityNotificationConsumerUnsecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(org.alembic.aurion.common.nhinccommonentity.NotifySubscribersOfDocumentRequestType notifySubscribersOfDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfCdcBioPackage(org.alembic.aurion.common.nhinccommonentity.NotifySubscribersOfCdcBioPackageRequestType notifySubscribersOfCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notify(org.alembic.aurion.common.nhinccommonentity.NotifyRequestType notifyRequest) {
        return new EntityNotificationConsumerImpl().notify(notifyRequest, context);
    }

}
