/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution.entity;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdministrativeDistributionSecured_Service", portName = "AdministrativeDistributionSecured_PortType", endpointInterface = "org.alembic.aurion.entityadmindistribution.AdministrativeDistributionSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistributionSecured/EntityAdminDistSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityAdministrativeDistributionSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityAdministrativeDistributionSecured {
    @Resource
    private WebServiceContext context;
    
    public void sendAlertMessage(org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType body) {
        //TODO implement this method

        AssertionType assertion = extractAssertion(context);
        getSoapLogger().logRawAssertion(assertion);

        //TODO implement this method
        getEntityImpl().sendAlertMessage(body, assertion, body.getNhinTargetCommunities());
    }

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }

    protected EntityAdminDistributionOrchImpl getEntityImpl()
    {
        return new EntityAdminDistributionOrchImpl();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
