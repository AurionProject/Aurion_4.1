/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "Adapter_AdministrativeDistribution", portName = "Adapter_AdministrativeDistribution_PortType", endpointInterface = "org.alembic.aurion.adapteradmindistribution.AdapterAdministrativeDistributionPortType", targetNamespace = "urn:org:alembic:aurion:adapteradmindistribution", wsdlLocation = "WEB-INF/wsdl/AdapterAdministrativeDistribution/AdapterAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterAdministrativeDistributionSoapHeaderHandler.xml")
public class AdapterAdministrativeDistribution {
     @Resource
    private WebServiceContext context;

    public void sendAlertMessage(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType body) {
        getSoapLogger().logRawAssertion(body.getAssertion());
        getImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion());
    }

    protected AdapterAdminDistributionOrchImpl getImpl()
    {
        return new AdapterAdminDistributionOrchImpl();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
