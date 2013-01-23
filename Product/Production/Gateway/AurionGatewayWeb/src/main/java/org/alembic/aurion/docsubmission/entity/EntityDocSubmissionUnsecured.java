/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.entity;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@WebService(serviceName = "EntityXDR_Service", portName = "EntityXDR_Port", endpointInterface = "org.alembic.aurion.nhincentityxdr.EntityXDRPortType", targetNamespace = "urn:org:alembic:aurion:nhincentityxdr", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionUnsecured/EntityXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocSubmissionSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocSubmissionUnsecured {

    @Resource
    private WebServiceContext context;

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetRequestType body)
    {
        RegistryResponseType response = null;

        EntityDocSubmissionImpl impl = getEntityDocSubmissionImpl();
        if (impl != null)
        {
            response = impl.provideAndRegisterDocumentSetBUnsecured(body, getWebServiceContext());
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
