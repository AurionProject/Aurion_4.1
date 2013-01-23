/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru;

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
@WebService(serviceName = "NhincProxyDocRetrieveSecured", portName = "NhincProxyDocRetrieveSecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveSecured/NhincProxyDocRetrieveSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyDocRetrieveSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class NhincProxyDocRetrieveSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body)
    {
        return new NhincProxyDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
