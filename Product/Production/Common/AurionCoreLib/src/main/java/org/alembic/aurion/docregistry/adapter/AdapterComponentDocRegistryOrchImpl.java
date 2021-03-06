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

package org.alembic.aurion.docregistry.adapter;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.alembic.aurion.docrepository.adapter.model.Document;
import org.alembic.aurion.docrepository.adapter.model.DocumentQueryParams;
import org.alembic.aurion.docrepository.adapter.model.EventCode;
import org.alembic.aurion.docrepository.adapter.model.EventCodeParam;
import org.alembic.aurion.docrepository.adapter.service.DocumentService;
import org.alembic.aurion.util.StringUtil;
import org.alembic.aurion.util.format.PatientIdFormatUtil;

import org.alembic.aurion.util.format.UTCDateUtil;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.alembic.aurion.docrepository.adapter.model.CodedElement;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocRegistryOrchImpl {
    private Log log = null;
    private UTCDateUtil utcDateUtil = null;

    /*
     * The following constants are the parameters defined by IHE for FindDocuments in
     * section 3.16.4.1.4.1 of the ITI TF-2a specification (revision 6.0 dated August 10, 2009)
     */
    private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private static final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
    private static final String EBXML_DOCENTRY_CLASS_CODE_SCHEME = "$XDSDocumentEntryClassCodeScheme";
    // Begin new
    private static final String EBXML_DOCENTRY_PRACTICE_SETTING_CODE = "$XDSDocumentEntryPracticeSettingCode";
    private static final String EBXML_DOCENTRY_PRACTICE_SETTING_CODE_SCHEME = "$XDSDocumentEntryPracticeSettingCodeScheme";
    // End new
    private static final String EBXML_DOCENTRY_CREATION_TIME_FROM = "$XDSDocumentEntryCreationTimeFrom";
    private static final String EBXML_DOCENTRY_CREATION_TIME_TO = "$XDSDocumentEntryCreationTimeTo";
    private static final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
    private static final String EBXML_DOCENTRY_SERVICE_START_TIME_TO = "$XDSDocumentEntryServiceStartTimeTo";
    private static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM = "$XDSDocumentEntryServiceStopTimeFrom";
    private static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
    // Begin new
    private static final String EBXML_DOCENTRY_HEALTHCARE_FACILITY_CODE = "$XDSDocumentEntryHealthcareFacilityTypeCode";
    private static final String EBXML_DOCENTRY_HEALTHCARE_FACILITY_CODE_SCHEME = "$XDSDocumentEntryHealthcareFacilityTypeCodeScheme";
    // End new
    private static final String EBXML_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";
    private static final String EBXML_EVENT_CODE_LIST_SCHEME = "$XDSDocumentEntryEventCodeListScheme";
    // Begin new
    private static final String EBXML_DOCENTRY_CONFIDENTIALITY_CODE = "$XDSDocumentEntryConfidentialityCode";
    private static final String EBXML_DOCENTRY_FORMAT_CODE = "$XDSDocumentEntryFormatCode";
    // End new
    private static final String EBXML_DOCENTRY_STATUS = "$XDSDocumentEntryStatus";
    // -- End IHE defined FindDocuments parameters --


    // We need to be able to do a search using AdhocQueryRequest parameters, but
    // XDS.b does not have a search parameter slot name defined for repository ID
    // and document ID.  So we had to create our own custom ones.
    //----------------------------------------------------------------------------
    private static final String NHINC_CUSTOM_REPOSITORY_ID = "$XDSRepositoryUniqueId";
    private static final String NHINC_CUSTOM_DOCUMENT_ID = "$XDSDocumentEntryUniqueId";

    private static final String EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME = "repositoryUniqueId";
    private static final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String EBXML_RESPONSE_DOCID_NAME = "XDSDocumentEntry.uniqueId";
    private static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    private static final String EBXML_RESPONSE_PATIENTID_NAME = "XDSDocumentEntry.patientId";
    private static final String EBXML_RESPONSE_AUTHOR_CLASS_SCHEME = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    private static final String EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME = "authorPerson";
    private static final String EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME = "authorInstitution";
    private static final String EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME = "authorRole";
    private static final String EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME = "authorSpecialty";
    private static final String EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    private static final String EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    private static final String EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    private static final String EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    private static final String EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    private static final String EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    private static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    private static final String EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME = "codingScheme";
    private static final String EBXML_RESPONSE_CREATIONTIME_SLOTNAME = "creationTime";
    private static final String EBXML_RESPONSE_HASH_SLOTNAME = "hash";
    private static final String EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME = "intendedRecipient";
    private static final String EBXML_RESPONSE_LANGUAGECODE_SLOTNAME = "languageCode";
    private static final String EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME = "legalAuthenticator";
    private static final String EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME = "serviceStartTime";
    private static final String EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME = "serviceStopTime";
    private static final String EBXML_RESPONSE_SIZE_SLOTNAME = "size";
    private static final String EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME = "sourcePatientId";
    private static final String EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME = "sourcePatientInfo";
    private static final String EBXML_RESPONSE_URI_SLOTNAME = "URI";
    private static final int EBXML_RESPONSE_URI_LINE_LENGTH = 128;

    private static final String XDS_QUERY_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    private static final String XDS_QUERY_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    private static final String XDS_QUERY_RESPONSE_OPTION_RETURN_TYPE_OBJECT_REF = "ObjectRef";
    private static final String XDS_QUERY_RESPONSE_OPTION_RETURN_TYPE_LEAF_CLASS = "LeafClass";
    private static final String XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";

    private static final String REPOSITORY_UNIQUE_ID = "1";

    // Properties file keys
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";

    public AdapterComponentDocRegistryOrchImpl()
    {
        log = createLogger();
        utcDateUtil = createDateUtil();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected UTCDateUtil createDateUtil()
    {
        return ((utcDateUtil != null) ? utcDateUtil : new UTCDateUtil());
    }

    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        //getLogger().debug("Begin DocumentRegistryHelper.documentRegistryRegistryStoredQuery(...)");
        oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory queryObjFact = new oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory();
        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = queryObjFact.createAdhocQueryResponse();

        // Collect input values from query.
        String patientId = null;
        List<CodedElement> classCodeValues = null;
        Date creationTimeFrom = null;
        Date creationTimeTo = null;
        Date serviceStartTimeFrom = null;
        Date serviceStartTimeTo = null;
        Date serviceStopTimeFrom = null;
        Date serviceStopTimeTo = null;
        List<String> statuses = null;
        List<String> documentUniqueIds = null;
        String sRepositoryId = null;
        List<String> eventCodeValues = null;
        List<String> eventCodeSchemeValues = null;

        if(request != null)
        {
            AdhocQueryType adhocQuery = request.getAdhocQuery();
            List<SlotType1> slots = null;
            if(adhocQuery != null)
            {
                slots = adhocQuery.getSlot();
            }

            patientId = extractPatientIdentifier(slots);
            classCodeValues = extractClassCodes(slots);
            creationTimeFrom = extractCreationTimeFrom(slots);
            creationTimeTo = extractCreationTimeTo(slots);
            serviceStartTimeFrom = extractServiceStartTimeFrom(slots);
            serviceStartTimeTo = extractServiceStartTimeTo(slots);
            serviceStopTimeFrom = extractServiceStopTimeFrom(slots);
            serviceStopTimeTo = extractServiceStopTimeTo(slots);
            statuses = extractStatuses(slots);
            documentUniqueIds = extractDocumentId(slots);

            // We really do not do much with the repository ID - Since
            // we are not supporting multiple repositories...
            //---------------------------------------------------------
            sRepositoryId = extractRepositoryId(slots);

            eventCodeValues = extractEventCodeList(slots);
            eventCodeSchemeValues = extractEventCodeListSchemes(slots);
        }

        // Perform Query
        DocumentQueryParams params = new DocumentQueryParams();
        params.setPatientId(patientId);
        params.setClassCodes(classCodeValues);
        params.setCreationTimeFrom(creationTimeFrom);
        params.setCreationTimeTo(creationTimeTo);
        params.setServiceStartTimeFrom(serviceStartTimeFrom);
        params.setServiceStartTimeTo(serviceStartTimeTo);
        params.setServiceStopTimeFrom(serviceStopTimeFrom);
        params.setServiceStopTimeTo(serviceStopTimeTo);
        params.setStatuses(statuses);
        params.setDocumentUniqueId(documentUniqueIds);
        params.setEventCodeParams(createEventCodeParameters(eventCodeValues, eventCodeSchemeValues));
        DocumentService service = getDocumentService();
        List<Document> docs = service.documentQuery(params);

        // Create response
        loadResponseMessage(response, docs);
        //getLogger().debug("End DocumentRegistryHelper.documentRegistryRegistryStoredQuery(...)");
        return response;
    }

    protected DocumentService getDocumentService()
    {
        return new DocumentService();
    }

    private List<EventCodeParam> createEventCodeParameters(List<String> eventCodeValues, List<String> eventCodeSchemeValues)
    {
        List<EventCodeParam> eventCodeParams = null;
        if(NullChecker.isNotNullish(eventCodeValues))
        {
            eventCodeParams = new ArrayList<EventCodeParam>();
            boolean hasMatchingSchemes = (NullChecker.isNotNullish(eventCodeSchemeValues) && (eventCodeValues.size() == eventCodeSchemeValues.size()));
            for(int i = 0; i < eventCodeValues.size(); i++)
            {
                String eventCode = eventCodeValues.get(i);
                EventCodeParam eventCodeParam = new EventCodeParam();
                eventCodeParam.setEventCode(eventCode);
                if(hasMatchingSchemes)
                {
                    String eventCodeScheme = eventCodeSchemeValues.get(i);
                    eventCodeParam.setEventCodeScheme(eventCodeScheme);
                }
                eventCodeParams.add(eventCodeParam);
            }
        }

        return eventCodeParams;
    }

    private String extractPatientIdentifier(List<SlotType1> slots)
    {
        String patientId = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            String formattedPatientId = slotValues.get(0);
            patientId = PatientIdFormatUtil.parsePatientId(formattedPatientId);
        }
        return patientId;
    }

    private List<CodedElement> extractClassCodes(List<SlotType1> slots)
    {
        List<CodedElement> classCodeElements = null;
        List<String> classCodeSlotValues = extractSlotValues(slots, EBXML_DOCENTRY_CLASS_CODE);
        List<String> classCodeSchemeSlotValues = extractSlotValues(slots, EBXML_DOCENTRY_CLASS_CODE_SCHEME);

        if(NullChecker.isNotNullish(classCodeSlotValues)) {
            classCodeElements = new ArrayList<CodedElement>();

            if(NullChecker.isNotNullish(classCodeSchemeSlotValues)) {
                parseCodeSystemFromSeparateSlots(classCodeElements, classCodeSlotValues, classCodeSchemeSlotValues);
            } else {
                parseCodeSystemFromClassCodeSlot(classCodeElements, classCodeSlotValues);
            }
        }
        List<String> classCodes = null;
        if((classCodeSlotValues != null) && (!classCodeSlotValues.isEmpty()))
        {
            classCodes = new ArrayList<String>();
            for(String slotValue : classCodeSlotValues)
            {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodeElements;
    }

    private void parseCodeSystemFromSeparateSlots(List<CodedElement> classCodeElements, List<String> classCodeSlotValues, List<String> classCodeSchemeSlotValues) {
        if(classCodeSlotValues.size() == classCodeSchemeSlotValues.size()) {
            for(int i = 0; i < classCodeSlotValues.size(); i++) {
                CodedElement code = new CodedElement();
                code.setCode(classCodeSlotValues.get(i));
                code.setCodeSystem(classCodeSchemeSlotValues.get(i));
                classCodeElements.add(code);
            }
        } else {
            throw new IllegalArgumentException("Class code and class code scheme slots must have the same number of values.");
        }
    }

    public void parseCodeSystemFromClassCodeSlot(List<CodedElement> classCodeElements, List<String> classCodeSlotValues) {
        for(String classCode : classCodeSlotValues) {
            List<String> codeEntries = new ArrayList<String>();
            parseParamFormattedString(classCode, codeEntries);
            for(String codeEntry : codeEntries) {
                CodedElement codeElement = parseClassCodeElement(codeEntry);
                if(codeElement != null) {
                    classCodeElements.add(codeElement);
                }
            }
        }
    }

    private CodedElement parseClassCodeElement(String classCodeSlotValue) {
        CodedElement classCodeElement = null;
        if(NullChecker.isNotNullish(classCodeSlotValue)) {
            String[] components = classCodeSlotValue.split("\\^");
            if((components != null) && (components.length > 2)) {
                classCodeElement = new CodedElement();
                classCodeElement.setCode(components[0]);
                classCodeElement.setCodeSystem(components[2]);
            }
        }
        return classCodeElement;
    }

    private Date extractCreationTimeFrom(List<SlotType1> slots)
    {
        Date creationTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CREATION_TIME_FROM);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            creationTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return creationTimeFrom;
    }

    private Date extractCreationTimeTo(List<SlotType1> slots)
    {
        Date creationTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CREATION_TIME_TO);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            creationTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return creationTimeTo;
    }

    private Date extractServiceStartTimeFrom(List<SlotType1> slots)
    {
        Date serviceStartTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_START_TIME_FROM);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            serviceStartTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStartTimeFrom;
    }

    private Date extractServiceStartTimeTo(List<SlotType1> slots)
    {
        Date serviceStartTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_START_TIME_TO);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            serviceStartTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStartTimeTo;
    }

    private Date extractServiceStopTimeFrom(List<SlotType1> slots)
    {
        Date serviceStopTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            serviceStopTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStopTimeFrom;
    }

    private Date extractServiceStopTimeTo(List<SlotType1> slots)
    {
        Date serviceStopTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_STOP_TIME_TO);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            serviceStopTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStopTimeTo;
    }

    private List<String> extractStatuses(List<SlotType1> slots)
    {
        List<String> statuses = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_STATUS);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            statuses = new ArrayList<String>();
            for(String slotValue : slotValues)
            {
                parseParamFormattedString(slotValue, statuses);
            }
        }
        return statuses;
    }

    /**
     * Extract the document ID from the slots, if it exists and put it into
     * the array.
     *
     * @param slots The slots to be searched.
     * @return The document ID in a list if it is found.
     */
    private List<String> extractDocumentId(List<SlotType1> slots)
    {
        List<String> documentIds = null;
        String docId = "";
        List<String> slotValues = extractSlotValues(slots, NHINC_CUSTOM_DOCUMENT_ID);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            // We should only have one - so use the first one.
            //-------------------------------------------------
            documentIds = new ArrayList<String>();
            docId = StringUtil.extractStringFromTokens(slotValues.get(0).trim(), "'()");
            documentIds.add(docId);
        }
        return documentIds;
    }

    /**
     * Extract the repository ID from the slots
     *
     * @param slots The slots to be searched.
     * @return The repository Id.
     */
    private String extractRepositoryId(List<SlotType1> slots)
    {
        String repositoryId = null;
        List<String> slotValues = extractSlotValues(slots, NHINC_CUSTOM_REPOSITORY_ID);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            // We should only have one - so use the first one.
            //-------------------------------------------------
            repositoryId = slotValues.get(0).trim();
            repositoryId = StringUtil.extractStringFromTokens(repositoryId, "'()");
        }
        return repositoryId;
    }

    private List<String> extractEventCodeList(List<SlotType1> slots)
    {
        List<String> classCodes = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_EVENT_CODE_LIST);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            classCodes = new ArrayList<String>();
            for(String slotValue : slotValues)
            {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodes;
    }

    private List<String> extractEventCodeListSchemes(List<SlotType1> slots)
    {
        List<String> classCodes = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_EVENT_CODE_LIST_SCHEME);
        if((slotValues != null) && (!slotValues.isEmpty()))
        {
            classCodes = new ArrayList<String>();
            for(String slotValue : slotValues)
            {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodes;
    }

    private void loadResponseMessage(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response, List<Document> docs)
    {
        RegistryObjectListType regObjList = new RegistryObjectListType();
        response.setRegistryObjectList(regObjList);

        if(NullChecker.isNullish(docs))
        {
            response.setStatus(XDS_QUERY_RESPONSE_STATUS_FAILURE);
        }
        else
        {
            response.setStatus(XDS_QUERY_RESPONSE_STATUS_SUCCESS);

            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            // Collect the home community id
            String homeCommunityId = retrieveHomeCommunityId();

            List<JAXBElement<? extends IdentifiableType>> olRegObjs = regObjList.getIdentifiable();

            // Save these so that theyu can be added in later after all of the other items..
            //------------------------------------------------------------------------------
            ArrayList<JAXBElement<? extends IdentifiableType>> olObjRef = new ArrayList<JAXBElement<? extends IdentifiableType>>();
            ArrayList<JAXBElement<? extends IdentifiableType>> olAssoc = new ArrayList<JAXBElement<? extends IdentifiableType>>();

            for(Document doc : docs)
            {
                ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
                JAXBElement<? extends IdentifiableType> oJAXBExtObj = oRimObjectFactory.createExtrinsicObject(oExtObj);
                List<SlotType1> olSlot = oExtObj.getSlot();
                List<ClassificationType> olClassifications = oExtObj.getClassification();
                boolean bHaveData = false;

                oExtObj.setIsOpaque(Boolean.FALSE);
                oExtObj.setObjectType(XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE);

                // Generate a UUID for the document
                UUID oDocumentUUID = UUID.randomUUID();
                String sDocumentUUID = "urn:uuid:" + oDocumentUUID.toString();
                oExtObj.setId(sDocumentUUID);

                //Document Unique ID
                //------------------
                String sDocumentId = "";        // need to keep a handle to this to be used later...
                if(NullChecker.isNotNullish(doc.getDocumentUniqueId()))
                {
                    sDocumentId = doc.getDocumentUniqueId();
                    ExternalIdentifierType oExtId = new ExternalIdentifierType();
                    oExtId.setId("");
                    oExtObj.getExternalIdentifier().add(oExtId);
                    oExtId.setRegistryObject(sDocumentUUID);
                    oExtId.setValue(sDocumentId);
                    oExtId.setIdentificationScheme(EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME);
                    InternationalStringType oName = CreateSingleValueInternationalStringType(EBXML_RESPONSE_DOCID_NAME);
                    oExtId.setName(oName);
                    bHaveData = true;
                }

                // Author data
                boolean bHasAuthorData = false;
                ClassificationType oClassification = new ClassificationType();
                oClassification.setId("");
                oClassification.setClassificationScheme(EBXML_RESPONSE_AUTHOR_CLASS_SCHEME);
                oClassification.setClassifiedObject(sDocumentUUID);
                oClassification.setNodeRepresentation("");
                List<SlotType1> olClassificationSlot = oClassification.getSlot();

                // AuthorPerson
                //-------------
                if(NullChecker.isNotNullish(doc.getAuthorPerson()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME,
                        doc.getAuthorPerson());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }
                // AuthorInstitution
                //------------------
                if(NullChecker.isNotNullish(doc.getAuthorInstitution()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME,
                        doc.getAuthorInstitution());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                // AuthorRole
                //------------
                if(NullChecker.isNotNullish(doc.getAuthorRole()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME,
                        doc.getAuthorRole());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                // AuthorSpecialty
                //----------------
                if(NullChecker.isNotNullish(doc.getAuthorSpecialty()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME,
                        doc.getAuthorSpecialty());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                if (bHasAuthorData)
                {
                    olClassifications.add(oClassification);
                    bHaveData = true;
                }

                // Availability Status
                //---------------------
                if(NullChecker.isNotNullish(doc.getAvailablityStatus()))
                {
                    oExtObj.setStatus(doc.getAvailablityStatus());
                    bHaveData = true;
                }

                // Class Code
                //------------
                ClassificationType classCodeClassification = createClassificationFromCodedData(doc.getClassCode(), doc.getClassCodeScheme(), doc.getClassCodeDisplayName(),
                    EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (classCodeClassification != null)
                {
                    olClassifications.add(classCodeClassification);
                    bHaveData = true;
                }

                // Comments
                //---------
                if(NullChecker.isNotNullish(doc.getComments()))
                {
                    InternationalStringType oComments = CreateSingleValueInternationalStringType(doc.getComments());
                    oExtObj.setDescription(oComments);
                    bHaveData = true;
                }

                // Confidentiality Code
                //---------------------
                ClassificationType confidentialityCodeClassification = createClassificationFromCodedData(doc.getConfidentialityCode(), doc.getConfidentialityCodeScheme(), doc.getConfidentialityCodeDisplayName(),
                    EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (confidentialityCodeClassification != null)
                {
                    olClassifications.add(confidentialityCodeClassification);
                    bHaveData = true;
                }

                // Creation Time
                //--------------
                if(doc.getCreationTime() != null)
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_CREATIONTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getCreationTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Event Code List
                //----------------
                if((doc.getEventCodes() != null) && (!doc.getEventCodes().isEmpty()))
                {
                    for(EventCode eventCode : doc.getEventCodes())
                    {
                        ClassificationType eventCodeClassification = createClassificationFromCodedData(eventCode.getEventCode(), eventCode.getEventCodeScheme(), eventCode.getEventCodeDisplayName(),
                            EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME,
                            sDocumentUUID);
                        if (eventCodeClassification != null)
                        {
                            olClassifications.add(eventCodeClassification);
                            bHaveData = true;
                        }
                    }
                }

                // Format Code
                //-------------
                ClassificationType formatCodeClassification = createClassificationFromCodedData(doc.getFormatCode(), doc.getFormatCodeScheme(), doc.getFormatCodeDisplayName(),
                    EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (formatCodeClassification != null)
                {
                    olClassifications.add(formatCodeClassification);
                    bHaveData = true;
                }

                // Hash Code
                //----------
                if(NullChecker.isNotNullish(doc.getHash()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_HASH_SLOTNAME,
                        doc.getHash());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Healthcare Facility Type Code
                //------------------------------
                ClassificationType healthcareFacilityTypeCodeClassification = createClassificationFromCodedData(doc.getFacilityCode(), doc.getFacilityCodeScheme(), doc.getFacilityCodeDisplayName(),
                    EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME,
                    sDocumentUUID);
                if (healthcareFacilityTypeCodeClassification != null)
                {
                    olClassifications.add(healthcareFacilityTypeCodeClassification);
                    bHaveData = true;
                }

                // Intended Recipients
                //--------------------
                List<String> intendedRecipients = new ArrayList<String>();
                if(NullChecker.isNotNullish(doc.getIntendedRecipientPerson()))
                {
                    intendedRecipients.add(doc.getIntendedRecipientPerson());
                }
                else if(NullChecker.isNotNullish(doc.getIntendedRecipientOrganization()))
                {
                    intendedRecipients.add(doc.getIntendedRecipientOrganization());
                }

                if(!intendedRecipients.isEmpty())
                {
                    String[] intendedRecipientArray = intendedRecipients.toArray(new String[intendedRecipients.size()]);
                    SlotType1 oSlot = CreateMultiValueSlot(EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME, intendedRecipientArray);
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Language Code
                //---------------
                if(NullChecker.isNotNullish(doc.getLanguageCode()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_LANGUAGECODE_SLOTNAME,
                        doc.getLanguageCode());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // LegalAuthenticator Code
                //------------------------
                if(NullChecker.isNotNullish(doc.getLegalAuthenticator()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME,
                        doc.getLegalAuthenticator());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Mime Type
                //----------
                if(NullChecker.isNotNullish(doc.getMimeType()))
                {
                    oExtObj.setMimeType(doc.getMimeType());
                    bHaveData = true;
                }

                // Patient ID
                //-----------
                if(NullChecker.isNotNullish(doc.getPatientId()))
                {
                    String formattedPatientId = PatientIdFormatUtil.hl7EncodePatientId(doc.getPatientId(), homeCommunityId);
                    ExternalIdentifierType oExtId = new ExternalIdentifierType();
                    oExtId.setId("");
                    oExtId.setIdentificationScheme(EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME);
                    InternationalStringType oPatIdName = CreateSingleValueInternationalStringType(EBXML_RESPONSE_PATIENTID_NAME);
                    oExtId.setName(oPatIdName);
                    oExtId.setRegistryObject(sDocumentUUID);
                    oExtId.setValue(formattedPatientId);
                    oExtObj.getExternalIdentifier().add(oExtId);
                    bHaveData = true;
                }

                // Practice Setting Code
                //----------------------
                ClassificationType practiceSettingCodeClassification = createClassificationFromCodedData(doc.getPracticeSetting(), doc.getPracticeSettingScheme(), doc.getPracticeSettingDisplayName(),
                    EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME,
                    sDocumentUUID);
                if (practiceSettingCodeClassification != null)
                {
                    olClassifications.add(practiceSettingCodeClassification);
                    bHaveData = true;
                }

                // Service Start Time
                //-------------------
                if(doc.getServiceStartTime() != null)
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getServiceStartTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Service Stop Time
                //------------------
                if(doc.getServiceStopTime() != null)
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getServiceStopTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Size
                //-----
                if((doc.getSize() != null) && (doc.getSize().intValue() > 0))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_SIZE_SLOTNAME,
                        doc.getSize().toString());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Source Patient Id
                //------------------
                if(NullChecker.isNotNullish(doc.getSourcePatientId()))
                {
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME,
                        doc.getSourcePatientId());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Source Patient Info
                //--------------------
                List<String> sourcePatientInfoValues = new ArrayList<String>();
                if(NullChecker.isNotNullish(doc.getPid3()))
                {
                    sourcePatientInfoValues.add("PID-3|" + doc.getPid3());
                }
                if(NullChecker.isNotNullish(doc.getPid5()))
                {
                    sourcePatientInfoValues.add("PID-5|" + doc.getPid5());
                }
                if(NullChecker.isNotNullish(doc.getPid7()))
                {
                    sourcePatientInfoValues.add("PID-7|" + doc.getPid7());
                }
                if(NullChecker.isNotNullish(doc.getPid8()))
                {
                    sourcePatientInfoValues.add("PID-8|" + doc.getPid8());
                }
                if(NullChecker.isNotNullish(doc.getPid11()))
                {
                    sourcePatientInfoValues.add("PID-11|" + doc.getPid11());
                }

                if(!sourcePatientInfoValues.isEmpty())
                {
                    String[] sourcePatientInfoValuesArray = sourcePatientInfoValues.toArray(new String[sourcePatientInfoValues.size()]);
                    SlotType1 oSlot = CreateMultiValueSlot(EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME,
                        sourcePatientInfoValuesArray);
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Title
                //-------
                if(NullChecker.isNotNullish(doc.getDocumentTitle()))
                {
                    InternationalStringType oTitle = CreateSingleValueInternationalStringType(doc.getDocumentTitle());
                    oExtObj.setName(oTitle);
                    bHaveData = true;
                }

                // Type Code
                //----------
                ClassificationType typeCodeClassification = createClassificationFromCodedData(doc.getTypeCode(), doc.getTypeCodeScheme(), doc.getTypeCodeDisplayName(),
                    EBXML_RESPONSE_TYPECODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (typeCodeClassification != null)
                {
                    olClassifications.add(typeCodeClassification);
                    bHaveData = true;
                }

                // URI
                //----
                if(NullChecker.isNotNullish(doc.getDocumentUri()))
                {
                    SlotType1 oSlot = null;
                    String documentUri = doc.getDocumentUri();
                    if (documentUri.length() <= EBXML_RESPONSE_URI_LINE_LENGTH)
                    {
                        oSlot = CreateSingleValueSlot(EBXML_RESPONSE_URI_SLOTNAME, documentUri);
                    }
                    else
                    {
                        int iStart = 0;
                        String sURI = documentUri;
                        int iTotalLen = sURI.length();
                        int iIndex = 1;
                        String saURIPart[] = null;

                        if ((iTotalLen % EBXML_RESPONSE_URI_LINE_LENGTH) == 0)
                        {
                            saURIPart = new String[iTotalLen / EBXML_RESPONSE_URI_LINE_LENGTH];
                        }
                        else
                        {
                            saURIPart = new String[iTotalLen / EBXML_RESPONSE_URI_LINE_LENGTH + 1];
                        }
                        while (iStart < iTotalLen)
                        {
                            if ((iStart + EBXML_RESPONSE_URI_LINE_LENGTH) > iTotalLen)
                            {
                                saURIPart[iIndex - 1] = iIndex + "|" + sURI.substring(iStart, iTotalLen);
                                iStart = iTotalLen;
                            }
                            else
                            {
                                saURIPart[iIndex - 1] = iIndex + "|" + sURI.substring(iStart, iStart + EBXML_RESPONSE_URI_LINE_LENGTH);
                                iStart += EBXML_RESPONSE_URI_LINE_LENGTH;
                            }
                            iIndex++;
                        }

                        oSlot = CreateMultiValueSlot(EBXML_RESPONSE_URI_SLOTNAME,
                            saURIPart);
                    }   // else

                    if (oSlot != null)
                    {
                        olSlot.add(oSlot);
                        bHaveData = true;
                    }
                }

                if(bHaveData)
                {
                    // Home community ID
                    //------------------
                    oExtObj.setHome(homeCommunityId);

                    // Repository Unique ID
                    //---------------------
                    SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME,
                        REPOSITORY_UNIQUE_ID);
                    olSlot.add(oSlot);

                    olRegObjs.add(oJAXBExtObj);

                }
            }
            // if we have any Object References, add them in now.
            //---------------------------------------------------
            if (olObjRef.size() > 0)
            {
                olRegObjs.addAll(olObjRef);
            }

            // if we have any associations, add them in now.
            //---------------------------------------------------
            if (olAssoc.size() > 0)
            {
                olRegObjs.addAll(olAssoc);
            }

        }

    }

    /**
     * This method creates a classification from a coded item.
     *
     * @param oCoded The coded to be transformed.
     * @param sClassificationScheme The classification scheme value.
     * @param sDocumentId The document ID for the document associated with this classificaation.
     * @return The classification created based on the information in the coded.
     */
    private ClassificationType createClassificationFromCodedData(String code, String codeScheme, String codeDisplayName, String sClassificationScheme, String sDocumentId)
    {
        log.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- Begin");
        ClassificationType oClassification = new ClassificationType();
        oClassification.setId("");
        boolean bHasCode = false;
        oClassification.setClassificationScheme(sClassificationScheme);
        oClassification.setClassifiedObject(sDocumentId);
        oClassification.setNodeRepresentation("");
        List<SlotType1> olClassificationSlot = oClassification.getSlot();

        // Code
        //-----
        if (NullChecker.isNotNullish(code))
        {
            oClassification.setNodeRepresentation(code);
            bHasCode = true;
        }

        // Code System
        //------------
        if (NullChecker.isNotNullish(codeScheme))
        {
            SlotType1 oSlot = CreateSingleValueSlot(EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME,
                codeScheme);
            olClassificationSlot.add(oSlot);
            bHasCode = true;
        }

        // DisplayName
        //------------
        if (NullChecker.isNotNullish(codeDisplayName))
        {
            InternationalStringType oDisplayName = CreateSingleValueInternationalStringType(codeDisplayName);
            oClassification.setName(oDisplayName);
            bHasCode = true;
        }

        if (bHasCode)
        {
            return oClassification;
        }
        else
        {
            log.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- End");
            return null;
        }
    }
    private String retrieveHomeCommunityId()
    {
        String homeCommunityId = null;
        try
        {
            homeCommunityId = "urn:oid:" + PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
        }
        catch(Throwable t)
        {
            log.error("Error retrieving the home community id: " + t.getMessage(), t);
        }
        return homeCommunityId;
    }

    private List<String> extractSlotValues(List<SlotType1> slots, String slotName)
    {
        List<String> returnValues = null;
        if(slots != null)
        {
            for(SlotType1 slot : slots)
            {
                if ((slot.getName() != null) &&
                    (slot.getName().length() > 0) &&
                    (slot.getValueList() != null) &&
                    (slot.getValueList().getValue() != null) &&
                    (slot.getValueList().getValue().size() > 0))
                {

                    if(slot.getName().equals(slotName))
                    {
                        ValueListType valueListType = slot.getValueList();
                        List<String> slotValues = valueListType.getValue();
                        returnValues = new ArrayList<String>();
                        for(String slotValue : slotValues)
                        {
                            returnValues.add(slotValue);
                        }
                    }
                }

            }
        }
        return returnValues;
    }

    public void parseParamFormattedString(String paramFormattedString, List<String> resultCollection)
    {
        if((paramFormattedString != null) && (resultCollection != null))
        {
            if(paramFormattedString.startsWith("("))
            {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if(endIndex != -1)
                {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if(multiValueString != null)
                {
                    for(int i = 0; i < multiValueString.length; i++)
                    {
                        String singleValue = multiValueString[i];
                        if(singleValue != null)
                        {
                            singleValue = singleValue.trim();
                        }
                        if(singleValue.startsWith("'"))
                        {
                            singleValue = singleValue.substring(1);
                            int endTickIndex = singleValue.indexOf("'");
                            if(endTickIndex != -1)
                            {
                                singleValue = singleValue.substring(0, endTickIndex);
                            }
                        }
                        resultCollection.add(singleValue);
                        if(log.isDebugEnabled())
                        {
                            log.debug("Added single value: " + singleValue + " to query parameters");
                        }
                    }
                }
            }
            else
            {
                resultCollection.add(paramFormattedString);
                if(log.isDebugEnabled())
                {
                    log.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
                }
            }
        }
    }

    /**
     * This method creates a Slot containing a single value.
     *
     * @param sSlotName The name of the slot.
     * @param sSlotValue The value for the slot.
     * @return The SlotType1 object containing the data passed in.
     */
    private SlotType1 CreateSingleValueSlot(String sSlotName, String sSlotValue)
    {
        log.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- Begin");
        String saSlotValue[] = new String[1];
        saSlotValue[0] = sSlotValue;
        log.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- End");
        return CreateMultiValueSlot(sSlotName, saSlotValue);
    }

    /**
     * This method creates a Slot containing a single value.
     *
     * @param sSlotName The name of the slot.
     * @param saSlotValue The array of values for the slot.
     * @return The SlotType1 object containing the data passed in.
     */
    private SlotType1 CreateMultiValueSlot(String sSlotName, String[] saSlotValue)
    {
        log.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- Begin");
        SlotType1 oSlot = new SlotType1();
        oSlot.setName(sSlotName);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        List<String> olValue = oValueList.getValue();
        for (int i = 0; i < saSlotValue.length; i++)
        {
            olValue.add(saSlotValue[i]);
        }
        log.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- End");
        return oSlot;
    }

    /**
     * This method creates an InternationalStringType with a single value.
     *
     * @param sLocStrValue The value to be placed in the string.
     * @return The InternationStringType that is being returned.
     */
    private InternationalStringType CreateSingleValueInternationalStringType(String sLocStrValue)
    {
        log.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- Begin");
        InternationalStringType oName = new InternationalStringType();
        List<LocalizedStringType> olLocStr = oName.getLocalizedString();
        LocalizedStringType oNameLocStr = new LocalizedStringType();
        olLocStr.add(oNameLocStr);
        oNameLocStr.setValue(sLocStrValue);
        log.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- End");
        return oName;
    }

}
