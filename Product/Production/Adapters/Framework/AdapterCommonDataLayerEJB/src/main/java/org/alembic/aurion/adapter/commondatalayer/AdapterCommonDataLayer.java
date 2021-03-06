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

package org.alembic.aurion.adapter.commondatalayer;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author kim
 */
@WebService(serviceName = "CommonDataLayerService", portName = "CommonDataLayerPort", endpointInterface = "org.alembic.aurion.adapter.commondatalayer.CommonDataLayerPortType", targetNamespace = "urn:org:alembic:aurion:adapter:commondatalayer", wsdlLocation = "META-INF/wsdl/AdapterCommonDataLayer/AdapterCommonDataLayer.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterCommonDataLayer implements CommonDataLayerPortType {

   public org.hl7.v3.FindEncountersPRPAMT900350UV02ResponseType getEncounters(org.hl7.v3.FindEncountersPRPAIN900300UV02RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getProcedures(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   @Override
   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getMedications(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return AdapterCommonDataLayerImpl.getInstance().getMedications(param0);
   }

   @Override
   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAllergies(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return AdapterCommonDataLayerImpl.getInstance().getAllergies(param0);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getVitals(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   @Override
   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getProblems(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
     return AdapterCommonDataLayerImpl.getInstance().getProblems(param0);
   }

   @Override
   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getTestResults(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      return AdapterCommonDataLayerImpl.getInstance().getTestResults(param0);
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAdmissions(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAppointments(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getImmunizations(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getFamilyHistory(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getSocialHistory(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getInsurances(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getOrders(org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
      //TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
   }

   @Override
   public org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType getPatienInfo(org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType param0) {
      return AdapterCommonDataLayerImpl.getInstance().getPatienInfo(param0);
   }

   @Override
   public org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType findPatients(org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType param0) {
      return AdapterCommonDataLayerImpl.getInstance().findPatients(param0);
   }

}
