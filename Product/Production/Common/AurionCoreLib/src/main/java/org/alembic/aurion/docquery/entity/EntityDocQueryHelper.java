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
package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import org.alembic.aurion.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import org.alembic.aurion.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import org.alembic.aurion.util.format.PatientIdFormatUtil;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.alembic.aurion.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import org.alembic.aurion.connectmgr.ConnectionManagerCommunityMapping;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class EntityDocQueryHelper {

    private Log log = null;

    public EntityDocQueryHelper() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public List<QualifiedSubjectIdentifierType> retreiveCorrelations(List<SlotType1> slotList, CMUrlInfos urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunity) {
        log.debug("begin: retrieveCorrelations(..)");
        log.debug("retrieveCorrelations:localHomeCommunity = " + localHomeCommunity);

        RetrievePatientCorrelationsResponseType results = null;
        RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        List<QualifiedSubjectIdentifierType> subIdList = new ArrayList<QualifiedSubjectIdentifierType>();
        boolean querySelf = false;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {

            // Find the Patient Id slot
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    qualSubId.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(slot.getValueList().getValue().get(0)));
                    String localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0));
                    qualSubId.setAssigningAuthorityIdentifier(localAssigningAuthorityId);

                    log.info("Extracting subject id: " + qualSubId.getSubjectIdentifier());
                    log.info("Extracting assigning authority id: " + qualSubId.getAssigningAuthorityIdentifier());
                    patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);
                }

                // Save off the target home community ids to use in the patient correlation query
                if (urlInfoList != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {
                    for (CMUrlInfo target : urlInfoList.getUrlInfo()) {
                        if (NullChecker.isNotNullish(target.getHcid())) {
                            patientCorrelationReq.getTargetHomeCommunity().add(target.getHcid());
                            log.debug("patientCorrelationReq.getTargetHomeCommunity().add(target.getHcid() - Adding: " + target.getHcid());

                            if (target.getHcid().equals(localHomeCommunity) &&
                                    isTargeted == true) {
                                querySelf = true;
                            }
                        }
                    }
                }

                break;
            }
        }

        if (!querySelf) {
            querySelf = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DOC_QUERY_SELF_PROPERTY_NAME);
        }

        // Retreive Patient Correlations this patient
        PatientCorrelationProxyObjectFactory factory = new PatientCorrelationProxyObjectFactory();
        PatientCorrelationProxy proxy = factory.getPatientCorrelationProxy();

        log.debug("Outputting patientCorrelationReq.getTargetAssigningAuthority(): ");
        if ((patientCorrelationReq != null) &&
            (patientCorrelationReq.getTargetAssigningAuthority().size() > 0))
        {
            int i = 0;
            for (String sAssignAuthority : patientCorrelationReq.getTargetAssigningAuthority())
            {
                log.debug("AssnAuthority[" + i + "]: " + sAssignAuthority);
            }
        }

        log.debug("Outputting patientCorrelationReq.getTargetHomeCommunities(): ");
        if ((patientCorrelationReq != null) &&
            (patientCorrelationReq.getTargetHomeCommunity().size() > 0))
        {
            int i = 0;
            for (String sHomeCommunity : patientCorrelationReq.getTargetHomeCommunity())
            {
                log.debug("HomeCommunity[" + i + "]: " + sHomeCommunity);
            }
        }


        patientCorrelationReq.setAssertion(assertion);
        PRPAIN201309UV02 patCorrelationRequest = PixRetrieveBuilder.createPixRetrieve(patientCorrelationReq);

        if ((patCorrelationRequest != null) &&
            (patCorrelationRequest.getControlActProcess() != null) &&
            (patCorrelationRequest.getControlActProcess().getQueryByParameter() != null) &&
            (patCorrelationRequest.getControlActProcess().getQueryByParameter().getValue() != null) &&
            (patCorrelationRequest.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null) &&
            (patCorrelationRequest.getControlActProcess().getQueryByParameter().getValue().getParameterList().getDataSource().size() > 0))
        {
            List<PRPAMT201307UV02DataSource> lTemp = patCorrelationRequest.getControlActProcess().getQueryByParameter().getValue().getParameterList().getDataSource();
            log.debug("Output the contents of the data sources of rhte RetreivePatientCorrelationsRequestSecured. Right before retrieve call.");
            for (PRPAMT201307UV02DataSource dataSource : lTemp)
            {
                if ((dataSource != null) &&
                    (dataSource.getValue().size() > 0) &&
                    (dataSource.getValue().get(0) != null) &&
                    (dataSource.getValue().get(0).getRoot() != null))
                {
                    log.debug("Root: " + dataSource.getValue().get(0).getRoot());
                }
            }
        }

        results = proxy.retrievePatientCorrelations(patCorrelationRequest, assertion);

        // Make sure the response is valid
        if (results != null &&
                results.getPRPAIN201310UV02() != null &&
                results.getPRPAIN201310UV02().getControlActProcess() != null &&
                NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject()) &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
            for (II id : results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) {
                QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                subId.setAssigningAuthorityIdentifier(id.getRoot());
                subId.setSubjectIdentifier(id.getExtension());
                subIdList.add(subId);
            }
        }

        // If we are querying ourselves as well then add this community to the list of correlations
        if (querySelf == true) {
            subIdList.add(patientCorrelationReq.getQualifiedPatientIdentifier());
        }

        return subIdList;
    }

    public HomeCommunityType lookupHomeCommunityId(String sAssigningAuthorityId, String sLocalAssigningAuthorityId, String sLocalHomeCommunity) {
        HomeCommunityType targetCommunity = null;
        if ((sAssigningAuthorityId != null) && (sAssigningAuthorityId.equals(sLocalAssigningAuthorityId))) {
            /*
             * If the target is the local home community, the local
             * assigning authority may not be mapped to the local
             * home community in the community mapping. Set manually.
             */
            targetCommunity = new HomeCommunityType();
            targetCommunity.setHomeCommunityId(sLocalHomeCommunity);
            log.debug("Assigning authority was for the local home community. Set target to manual local home community id");
        } else {
            targetCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(sAssigningAuthorityId);
        }
        return targetCommunity;
    }

    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }

    public String getLocalAssigningAuthority(List<SlotType1> slotList) {
        String localAssigningAuthorityId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0));
                }
                break;
            }
        }

        return localAssigningAuthorityId;
    }
}
