/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.response;

import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * This is an Entity Unsecure service for Document Retrieve Deferred Response message
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredResponse", portName = "EntityDocRetrieveDeferredResponsePortSoap", endpointInterface = "org.alembic.aurion.entitydocretrieve.EntityDocRetrieveDeferredResponsePortType", targetNamespace = "urn:org:alembic:aurion:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredResp/EntityDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntityDocRetrieveDeferredRespSoapHeaderHandler.xml")
@Addressing(enabled=true)
public class EntityDocRetrieveDeferredResp extends EntityDocRetrieveDeferredResponseImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        return crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
    }
}
