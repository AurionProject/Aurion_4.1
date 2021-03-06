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

package org.alembic.aurion.document;

import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author cmatser
 */
@WebService(serviceName = "DocumentRegistry_Service", portName = "DocumentRegistry_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRegistryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "META-INF/wsdl/DocumentRegistryService/AdapterComponentDocRegistry.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class DocumentRegistryService implements DocumentRegistryPortType {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRegistryRegisterDocumentSetB(oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentRegistryRegistryStoredQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body)
    {
        return new DocumentRegistryHelper().documentRegistryRegistryStoredQuery(body);
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201301UV02(org.hl7.v3.PRPAIN201301UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201302UV02(org.hl7.v3.PRPAIN201302UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201304UV02(org.hl7.v3.PRPAIN201304UV02 body)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

}
