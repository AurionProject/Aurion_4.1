/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.docretrieve.adapter.component.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentDocRetrieveReqService", portName = "AdapterComponentDocRetrieveReqPortSoap", endpointInterface = "org.alembic.aurion.adaptercomponentdocretrievedeferredreq.AdapterComponentDocRetrieveDeferredReqPortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentdocretrievedeferredreq", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocRetrieveDeferredRequest/AdapterComponentDocRetrieveDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocRetrieveDeferredRequest {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType retrieveDocumentSetRequest(org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType body) {
        return new AdapterComponentDocRetrieveDeferredRequestImpl().crossGatewayRetrieveRequest(body, context);
    }

}
