/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubscriptionManagerSecured", portName = "NhincProxySubscriptionManagerSecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyHiemUnsubscribeSecured
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        try {
            return new ProxyHiemUnsubscribeImpl().unsubscribe(unsubscribeRequestSecured, context);
        }
        catch (Exception ex) {
            return null;
        }
    }

}
