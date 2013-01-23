/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.entity;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@WebService(serviceName = "EntityXDRSecured_Service", portName = "EntityXDRSecured_Port", endpointInterface = "org.alembic.aurion.nhincentityxdrsecured.EntityXDRSecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincentityxdrsecured", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionSecured/EntityXDRSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocSubmissionSoapHeaderHandler.xml")
@Addressing(enabled = true)
public class EntityDocSubmissionSecured
{

    @Resource
    private WebServiceContext context;

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body)
    {
        RegistryResponseType response = null;

        EntityDocSubmissionImpl impl = getEntityDocSubmissionImpl();
        if (impl != null)
        {
            response = impl.provideAndRegisterDocumentSetBSecured(body, getWebServiceContext());
        }

        return response;
    }

    protected EntityDocSubmissionImpl getEntityDocSubmissionImpl()
    {
        return new EntityDocSubmissionImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
