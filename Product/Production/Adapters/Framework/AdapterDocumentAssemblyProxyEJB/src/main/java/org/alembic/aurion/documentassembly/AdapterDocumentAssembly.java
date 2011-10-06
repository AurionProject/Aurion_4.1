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

package org.alembic.aurion.documentassembly;

import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author kim
 */
@WebService(serviceName = "DocumentAssembly", portName = "DocumentAssemblyPortSoap", endpointInterface = "org.alembic.aurion.documentassembly.DocumentAssemblyPortType", targetNamespace = "urn:org:alembic:aurion:documentassembly", wsdlLocation = "META-INF/wsdl/AdapterDocumentAssembly/DocumentAssembly.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterDocumentAssembly implements DocumentAssemblyPortType {

   @Override
   public RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicAssemblyQuery(RespondingGatewayCrossGatewayQueryRequestType dynamicAssemblyQueryRequest) {
      return AdapterDocumentAssemblyHelper.dynamicAssemblyQuery(dynamicAssemblyQueryRequest);
   }

}
