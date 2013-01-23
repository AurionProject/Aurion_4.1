package org.alembic.aurion.hiem.entity.subscribe;

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
@WebService(serviceName = "EntityNotificationProducer", portName = "EntityNotificationProducerPortSoap", endpointInterface = "org.alembic.aurion.entitysubscriptionmanagement.EntityNotificationProducerPortType", targetNamespace = "urn:org:alembic:aurion:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntityNotificationProducerUnsecured/EntitySubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
@HandlerChain(file = "EntitySubscribeSoapHeaderHandler.xml")
public class EntityNotificationProducerUnsecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.alembic.aurion.common.nhinccommonentity.SubscribeRequestType subscribeRequest) throws Exception {
        return new EntityNotificationProducerImpl().subscribe(subscribeRequest, context);
    }

}
