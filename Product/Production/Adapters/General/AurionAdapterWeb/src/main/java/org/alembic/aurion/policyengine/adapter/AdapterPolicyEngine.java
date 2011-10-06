/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngine", portName = "AdapterPolicyEnginePortSoap", endpointInterface = "org.alembic.aurion.adapterpolicyengine.AdapterPolicyEnginePortType", targetNamespace = "urn:org:alembic:aurion:adapterpolicyengine", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngine/AdapterPolicyEngine.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPolicyEngine {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest)
    {
        return new AdapterPolicyEngineImpl().checkPolicy(checkPolicyRequest, context);
    }

}
