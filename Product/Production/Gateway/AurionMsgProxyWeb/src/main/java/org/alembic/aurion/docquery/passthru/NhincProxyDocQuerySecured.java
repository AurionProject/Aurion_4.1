/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocQuerySecured", portName = "NhincProxyDocQuerySecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocquerysecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocQuerySecured/NhincProxyDocQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyDocQuerySoapHeaderHandler.xml")
@Addressing(enabled=true)
public class NhincProxyDocQuerySecured {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new NhincProxyDocQueryImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
