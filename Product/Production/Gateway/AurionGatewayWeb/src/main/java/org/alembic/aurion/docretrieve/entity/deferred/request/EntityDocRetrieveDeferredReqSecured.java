/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.request;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
/**
 * This is an Entity Secure service for Document Retrieve Deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredRequestSecured", portName = "EntityDocRetrieveDeferredRequestSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitydocretrievesecured.EntityDocRetrieveDeferredRequestSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReqSecured/EntityDocRetrieveDeferredReqSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocRetrieveDeferredReqSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocRetrieveDeferredReqSecured extends EntityDocRetrieveDeferredRequestImpl
{

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
        return crossGatewayRetrieveRequest(body, context);
    }
}
