/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
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
 * @author Harris
 */
@WebService(serviceName = "NhincProxySubscriptionManagerSecured", portName = "NhincProxySubscriptionManagerSecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/PassthruHiemUnsubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "PassthruHiemUnsubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PassthruHiemUnsubscribeSecured {

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new PassthruHiemUnsubscribeImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
