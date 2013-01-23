/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution.adapter;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "Adapter_AdministrativeDistributionSecured", portName = "Adapter_AdministrativeDistributionSecured_PortType", endpointInterface = "org.alembic.aurion.adapteradmindistribution.AdapterAdministrativeDistributionSecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapteradmindistribution", wsdlLocation = "WEB-INF/wsdl/AdapterAdministrativeDistributionSecured/AdapterAdminDistSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterAdministrativeDistributionSoapHeaderHandler.xml")
public class AdapterAdministrativeDistributionSecured {
        @Resource
    private WebServiceContext context;

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }

    public void sendAlertMessage(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewaySendAlertMessageSecuredType body) {

        AssertionType assertion = extractAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        getImpl().sendAlertMessage(body.getEDXLDistribution(),assertion);

    }
    protected AdapterAdminDistributionOrchImpl getImpl()
    {
        return new AdapterAdminDistributionOrchImpl();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
