package org.alembic.aurion.hiem.adapter.subscribe;

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
@WebService(serviceName = "AdapterNotificationProducer", portName = "AdapterNotificationProducerPortSoap", endpointInterface = "org.alembic.aurion.adaptersubscriptionmanagement.AdapterNotificationProducerPortType", targetNamespace = "urn:org:alembic:aurion:adaptersubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/AdapterNotificationProducerUnsecured/AdapterSubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterNotificationProducerSoapHandler.xml")
@Addressing(enabled = true)
public class AdapterNotificationProducerUnsecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.alembic.aurion.common.nhinccommonadapter.SubscribeRequestType subscribeRequest) {
        return new AdapterNotificationProducerImpl().subscribe(subscribeRequest, context);
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
