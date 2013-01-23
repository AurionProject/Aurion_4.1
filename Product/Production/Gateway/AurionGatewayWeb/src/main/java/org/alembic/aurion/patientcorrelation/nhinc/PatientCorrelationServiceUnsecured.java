/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientcorrelation.nhinc;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "org.alembic.aurion.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceUnsecured/NhincComponentPatientCorrelation.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "PatientCorrelationServiceSoapHeaderHandler.xml")
public class PatientCorrelationServiceUnsecured {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.RetrievePatientCorrelationsResponseType retrievePatientCorrelations(org.hl7.v3.RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
        return new PatientCorrelationServiceUnsecuredImpl().retrievePatientCorrelations(retrievePatientCorrelationsRequest, context);
    }

    public org.hl7.v3.AddPatientCorrelationResponseType addPatientCorrelation(org.hl7.v3.AddPatientCorrelationRequestType addPatientCorrelationRequest) {
        return new PatientCorrelationServiceUnsecuredImpl().addPatientCorrelation(addPatientCorrelationRequest, context);
    }

}
