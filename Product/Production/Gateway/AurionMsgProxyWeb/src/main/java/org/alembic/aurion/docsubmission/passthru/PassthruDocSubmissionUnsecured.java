/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru;

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
@WebService(serviceName = "ProxyXDR_Service", portName = "ProxyXDR_Port", endpointInterface = "org.alembic.aurion.nhincproxyxdr.ProxyXDRPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxyxdr", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionUnsecured/NhincProxyXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PassthruDocSubmissionSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class PassthruDocSubmissionUnsecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType body) {
        return new PassthruDocSubmissionImpl().provideAndRegisterDocumentSetB(body, context);
    }

}
