/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.passthru.notify;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationConsumerSecured", portName = "NhincProxyNotificationConsumerPortSoap", endpointInterface = "org.alembic.aurion.nhincproxynotificationconsumersecured.NhincProxyNotificationConsumerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/PassthruHiemNotifySecured/NhincProxyNotificationConsumerSecured.wsdl")
@HandlerChain(file = "PassthruHiemNotifyHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PassthruHiemNotifySecured {

    @Resource
    private WebServiceContext context;

    public void notify(org.alembic.aurion.common.nhinccommonproxy.NotifyRequestSecuredType notifyRequestSecured)
    {
        new PassthruHiemNotifyImpl().notify(notifyRequestSecured, context);
    }

}
