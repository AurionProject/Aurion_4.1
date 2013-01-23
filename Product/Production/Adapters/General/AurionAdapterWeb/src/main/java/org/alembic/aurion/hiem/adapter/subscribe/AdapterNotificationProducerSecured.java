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
@WebService(serviceName = "AdapterNotificationProducerSecured", portName = "AdapterNotificationProducerPortSecuredSoap", endpointInterface = "org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType", targetNamespace = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/AdapterNotificationProducerSecured/AdapterSubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterNotificationProducerSoapHandler.xml")
@Addressing(enabled = true)
public class AdapterNotificationProducerSecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest) {
       return new AdapterNotificationProducerImpl().subscribe(subscribeRequest, context);
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(org.alembic.aurion.common.nhinccommonadapter.SubscribeDocumentRequestSecuredType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
