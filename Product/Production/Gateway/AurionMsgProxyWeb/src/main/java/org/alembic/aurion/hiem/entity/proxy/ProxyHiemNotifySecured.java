/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationConsumerSecured", portName = "NhincProxyNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.nhincproxynotificationconsumersecured.NhincProxyNotificationConsumerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/ProxyHiemNotifySecured/NhincProxyNotificationConsumerSecured.wsdl")
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyHiemNotifySecured
{
    @Resource
    private WebServiceContext context;

    public void notify(org.alembic.aurion.common.nhinccommonproxy.NotifyRequestSecuredType notifyRequestSecured)
    {
        new ProxyHiemNotifyImpl().notify(notifyRequestSecured, context);
    }

}
