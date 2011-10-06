/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.lift;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.alembic.aurion.gateway.lift.StartLiftTransactionResponseType;
import org.alembic.aurion.gateway.lift.StartLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.CompleteLiftTransactionResponseType;
import org.alembic.aurion.gateway.lift.CompleteLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.FailedLiftTransactionResponseType;
import org.alembic.aurion.gateway.lift.FailedLiftTransactionRequestType;
import org.alembic.aurion.lift.proxy.GatewayLiftManagerProxyJavaImpl;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "NhincComponentLiftManager", portName = "NhincComponentLiftManagerPort", endpointInterface = "org.alembic.aurion.nhinccomponentliftmanager.NhincComponentLiftManagerPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentliftmanager", wsdlLocation = "WEB-INF/wsdl/GatewayLiftManager/NhincComponentLiftManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class GatewayLiftManager {

    public StartLiftTransactionResponseType startLiftTransaction(StartLiftTransactionRequestType startLiftTransactionRequest)
    {
        GatewayLiftManagerProxyJavaImpl impl = new GatewayLiftManagerProxyJavaImpl();
        StartLiftTransactionResponseType oResponse = impl.startLiftTransaction(startLiftTransactionRequest);
        return oResponse;
    }

    public CompleteLiftTransactionResponseType completeLiftTransaction(CompleteLiftTransactionRequestType completeLiftTransactionRequest)
    {
        GatewayLiftManagerProxyJavaImpl impl = new GatewayLiftManagerProxyJavaImpl();
        CompleteLiftTransactionResponseType oResponse = impl.completeLiftTransaction(completeLiftTransactionRequest);
        return oResponse;
    }

    public FailedLiftTransactionResponseType failedLiftTransaction(FailedLiftTransactionRequestType failedLiftTransactionRequest)
    {
        GatewayLiftManagerProxyJavaImpl impl = new GatewayLiftManagerProxyJavaImpl();
        FailedLiftTransactionResponseType oResponse = impl.failedLiftTransaction(failedLiftTransactionRequest);
        oResponse.setStatus("SUCCESS");
        return oResponse;
    }

}
