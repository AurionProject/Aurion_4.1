/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.notify;

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
@WebService(serviceName = "NhincProxyNotificationConsumer", portName = "NhincProxyNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxynotificationconsumer", wsdlLocation = "WEB-INF/wsdl/PassthruHiemNotifyUnsecured/NhincProxyNotificationConsumer.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
@HandlerChain(file = "PassthruHiemNotifyHeaderHandler.xml")
public class PassthruHiemNotifyUnsecured {

    @Resource
    private WebServiceContext context;

    public void notify(org.alembic.aurion.common.nhinccommonproxy.NotifyRequestType notifyRequest) {
        new PassthruHiemNotifyImpl().notify(notifyRequest, context);
    }

}
