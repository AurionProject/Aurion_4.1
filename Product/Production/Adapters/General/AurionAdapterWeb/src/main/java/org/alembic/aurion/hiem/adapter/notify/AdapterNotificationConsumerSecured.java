package org.alembic.aurion.hiem.adapter.notify;

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
@WebService(serviceName = "AdapterNotificationConsumerSecured", portName = "AdapterNotificationConsumerPortSecureType", endpointInterface = "org.alembic.aurion.adapternotificationconsumersecured.AdapterNotificationConsumerPortSecureType", targetNamespace = "urn:org:alembic:aurion:adapternotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/AdapterNotificationConsumerSecured/AdapterNotificationConsumerSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterNotificationConsumerSoapHandler.xml")
@Addressing(enabled = true)
public class AdapterNotificationConsumerSecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(org.alembic.aurion.common.nhinccommonadapter.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommon.AcknowledgementType notify(org.oasis_open.docs.wsn.b_2.Notify notifyRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
