/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.admindistribution.passthru;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincAdminDistService", portName = "NhincAdminDist_PortType", endpointInterface = "org.alembic.aurion.nhincadmindistribution.NhincAdminDistPortType", targetNamespace = "urn:org:alembic:aurion:nhincadmindistribution", wsdlLocation = "WEB-INF/wsdl/NhincProxyAdminDist/NhincAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincAdminDistSoapHeaderHandler.xml")
public class NhincAdminDist {

    public void sendAlertMessage(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewaySendAlertMessageType body) {
        getSoapLogger().logRawAssertion(body.getAssertion());
        getNhincImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion(), body.getNhinTargetSystem());
    }

    public PassthruAdminDistributionOrchImpl getNhincImpl()
    {
        return new PassthruAdminDistributionOrchImpl();
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
