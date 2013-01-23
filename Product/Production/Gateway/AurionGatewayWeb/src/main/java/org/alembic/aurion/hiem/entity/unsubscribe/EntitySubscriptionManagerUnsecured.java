package org.alembic.aurion.hiem.entity.unsubscribe;

import org.alembic.aurion.entitysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
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
@WebService(serviceName = "EntitySubscriptionManager", portName = "EntitySubscriptionManagerPortSoap", endpointInterface = "org.alembic.aurion.entitysubscriptionmanagement.EntitySubscriptionManagerPortType", targetNamespace = "urn:org:alembic:aurion:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntitySubscriptionManagerUnsecured/EntitySubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityUnsubscribeSoapHeaderHandler.xml")
@Addressing(enabled = true)
public class EntitySubscriptionManagerUnsecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonentity.UnsubscribeRequestType unsubscribeRequest) throws UnableToDestroySubscriptionFault, ResourceUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault, org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault {
        return new EntitySubscriptionManagerImpl().unsubscribe(unsubscribeRequest, context);
    }

}
