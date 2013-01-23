package org.alembic.aurion.hiem.entity.unsubscribe;

import org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault;
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
@WebService(serviceName = "EntitySubscriptionManagerSecured", portName = "EntitySubscriptionManagerSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitysubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/EntitySubscriptionManagerSecured/EntitySubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityUnsubscribeSoapHeaderHandler.xml")
@Addressing(enabled = true)
public class EntitySubscriptionManagerSecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault {
        return new EntitySubscriptionManagerImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
