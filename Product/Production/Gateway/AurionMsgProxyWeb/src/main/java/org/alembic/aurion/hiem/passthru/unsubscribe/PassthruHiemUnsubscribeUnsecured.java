/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.unsubscribe;

import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxySubscriptionManager", portName = "NhincProxySubscriptionManagerPortSoap", endpointInterface = "org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/PassthruHiemUnsubscribeUnsecured/NhincProxySubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruHiemUnsubscribeHeaderHandler.xml")
public class PassthruHiemUnsubscribeUnsecured {

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        return new PassthruHiemUnsubscribeImpl().unsubscribe(unsubscribeRequest, context);
    }
}
