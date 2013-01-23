/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.proxy;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyAuditLogQuerySecured", portName = "NhincProxyAuditLogQuerySecuredPortSoap", endpointInterface = "org.alembic.aurion.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxyauditlogquerysecured", wsdlLocation = "WEB-INF/wsdl/NhincAuditQueryProxy/NhincProxyAuditLogQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "NhincAuditQueryProxySoapHeaderHandler.xml")
public class NhincAuditQueryProxy {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(org.alembic.aurion.common.nhinccommonproxy.FindAuditEventsSecuredRequestType findAuditEventsRequest)
    {
        return new ProxyAuditLogQueryImpl().findAuditEvents(findAuditEventsRequest, context);
    }

}
