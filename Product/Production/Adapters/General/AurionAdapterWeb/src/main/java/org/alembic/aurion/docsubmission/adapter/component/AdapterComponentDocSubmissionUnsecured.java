/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.component;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterComponentXDR_Service", portName = "AdapterComponentXDR_Port", endpointInterface = "org.alembic.aurion.adaptercomponentxdr.AdapterComponentXDRPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentxdr", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocSubmissionUnsecured/AdapterComponentXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterComponentDocSubmissionSoapHandler.xml")
@Addressing(enabled=true)
public class AdapterComponentDocSubmissionUnsecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetb(org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterComponentDocSubmissionImpl().provideAndRegisterDocumentSetb(body, context);
    }

}
