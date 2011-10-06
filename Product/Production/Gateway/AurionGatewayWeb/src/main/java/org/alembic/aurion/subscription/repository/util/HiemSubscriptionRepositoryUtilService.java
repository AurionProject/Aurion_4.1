/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.subscription.repository.util;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "HiemSubscriptionRepositoryUtilService", portName = "HiemSubscriptionRepositoryUtilPortTypeBindingPort", endpointInterface = "org.alembic.aurion.hiemsubscriptionrepositoryutil.HiemSubscriptionRepositoryUtilPortType", targetNamespace = "urn:org:alembic:aurion:hiemsubscriptionrepositoryutil", wsdlLocation = "WEB-INF/wsdl/HiemSubscriptionRepositoryUtilService/HiemSubscriptionRepositoryUtil.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class HiemSubscriptionRepositoryUtilService
{

    public org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType getSubscriptionCount(org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountRequestType getSubscriptionCountRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().getSubscriptionCount(getSubscriptionCountRequest);
    }

    public org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(org.alembic.aurion.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().emptySubscriptionRepository(emptySubscriptionRepositoryRequest);
    }

}
