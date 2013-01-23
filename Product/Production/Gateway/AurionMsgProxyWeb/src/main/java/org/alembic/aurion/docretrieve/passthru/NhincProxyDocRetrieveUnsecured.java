/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyDocRetrieve", portName = "NhincProxyDocRetrievePortSoap", endpointInterface = "org.alembic.aurion.nhincproxydocretrieve.NhincProxyDocRetrievePortType", targetNamespace = "urn:org:alembic:aurion:nhincproxydocretrieve", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieveUnsecured/NhincProxyDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincProxyDocRetrieveSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class NhincProxyDocRetrieveUnsecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        return new NhincProxyDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest, context);
    }

}
