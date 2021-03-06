/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.request;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * This is an Entity Unsecure service for Document Retrieve Deferred Request message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredRequest", portName = "EntityDocRetrieveDeferredRequestPortSoap", endpointInterface = "org.alembic.aurion.entitydocretrieve.EntityDocRetrieveDeferredRequestPortType", targetNamespace = "urn:org:alembic:aurion:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredReq/EntityDocRetrieveDeferredReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocRetrieveDeferredReqSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocRetrieveDeferredReq extends EntityDocRetrieveDeferredRequestImpl
{

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
        return crossGatewayRetrieveRequest(crossGatewayRetrieveRequest, context);
    }
}
