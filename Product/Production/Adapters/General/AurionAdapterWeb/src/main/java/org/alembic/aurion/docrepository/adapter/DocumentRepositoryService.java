/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docrepository.adapter;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "DocumentRepository_Service", portName = "DocumentRepository_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRepositoryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/DocumentRepositoryService/AdapterComponentDocRepository.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "DocumentRepositorySoapHandler.xml")
public class DocumentRepositoryService {

    @Resource
    WebServiceContext context;

    public DocumentRepositoryService()
    {

    }

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body)
    {
        return new DocumentRepositoryServiceImpl().documentRepositoryProvideAndRegisterDocumentSet(body, context);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)
    {
        return new DocumentRepositoryServiceImpl().documentRepositoryRetrieveDocumentSet(body, context);
    }

}
