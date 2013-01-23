/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution.entity;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdministrativeDistribution_Service", portName = "AdministrativeDistribution_PortType", endpointInterface = "org.alembic.aurion.entityadmindistribution.AdministrativeDistributionPortType", targetNamespace = "urn:org:alembic:aurion:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistribution/EntityAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityAdministrativeDistributionSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityAdministrativeDistribution {

    public void sendAlertMessage(org.alembic.aurion.common.nhinccommonentity.RespondingGatewaySendAlertMessageType body) {
        //TODO implement this method
        getEntityImpl().sendAlertMessage(body, body.getAssertion(), body.getNhinTargetCommunities());
    }

    protected EntityAdminDistributionOrchImpl getEntityImpl()
    {
        return new EntityAdminDistributionOrchImpl();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
