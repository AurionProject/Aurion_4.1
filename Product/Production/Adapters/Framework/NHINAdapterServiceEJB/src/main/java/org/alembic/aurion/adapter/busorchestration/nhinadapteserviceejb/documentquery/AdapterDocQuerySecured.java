/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adapter.busorchestration.nhinadapteserviceejb.documentquery;

import org.alembic.aurion.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "AdapterDocQuerySecured", portName = "AdapterDocQuerySecuredPortSoap", endpointInterface = "org.alembic.aurion.adapterdocquerysecured.AdapterDocQuerySecuredPortType", targetNamespace = "urn:org:alembic:aurion:adapterdocquerysecured", wsdlLocation = "META-INF/wsdl/AdapterDocQuerySecured/AdapterDocQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterDocQuerySecured {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
         return AdapterDocQuerySecuredImpl.getInstance().respondingGatewayCrossGatewayQuery(body, context);
    }

}
