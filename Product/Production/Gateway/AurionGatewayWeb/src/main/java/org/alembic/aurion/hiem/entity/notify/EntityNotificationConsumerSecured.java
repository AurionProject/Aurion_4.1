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
@WebService(serviceName = "EntityNotificationConsumerSecured", portName = "EntityNotificationConsumerSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/EntityNotificationConsumerSecured/EntityNotificationConsumerSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityNotifySoapHeaderHandler.xml")
@Addressing(enabled = true)
public class EntityNotificationConsumerSecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(org.alembic.aurion.common.nhinccommonentity.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notify(org.oasis_open.docs.wsn.b_2.Notify notifyRequestSecured) {
        return new EntityNotificationConsumerImpl().notify(notifyRequestSecured, context);
    }

}
