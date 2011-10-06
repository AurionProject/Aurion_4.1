/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.unsubscribe;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterSubscriptionManagerSecured", portName = "AdapterSubscriptionManagerPortSoap", endpointInterface = "org.alembic.aurion.adaptersubscriptionmanagementsecured.AdapterSubscriptionManagerPortSecuredType", targetNamespace = "urn:org:alembic:aurion:adaptersubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/AdapterSubscriptionManagerSecured/AdapterSubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class AdapterSubscriptionManagerSecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequest) {
        return new AdapterSubscriptionManagerImpl().unsubscribe(unsubscribeRequest, context);
    }

}
