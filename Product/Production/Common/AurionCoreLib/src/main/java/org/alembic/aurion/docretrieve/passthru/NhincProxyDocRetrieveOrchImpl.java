/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docretrieve.DocRetrieveAuditLog;
import org.alembic.aurion.docretrieve.nhin.proxy.NhinDocRetrieveProxyObjectFactory;
import org.alembic.aurion.docretrieve.nhin.proxy.NhinDocRetrieveProxy;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveOrchImpl
{
    private static final String HOME_COMMUNITY_PREFIX = "urn:oid:";
    private Log log = null;

    public NhincProxyDocRetrieveOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;
        DocRetrieveAuditLog auditLog = null;
        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        // Audit request message
        if (isNhinAudit) {
            auditLog = new DocRetrieveAuditLog();
            auditLog.auditDocRetrieveRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        try
        {
            log.debug("Creating NHIN doc retrieve proxy");
            NhinDocRetrieveProxyObjectFactory objFactory = new NhinDocRetrieveProxyObjectFactory();
            NhinDocRetrieveProxy docRetrieveProxy = objFactory.getNhinDocRetrieveProxy(extractHomeCommunityId(request));

            log.debug("Calling doc retrieve proxy");
            response = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request, assertion, targetSystem);
        }
        catch(Throwable t)
        {
            log.error("Error occured sending doc query to NHIN target: " + t.getMessage(), t);
            response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Processing NHIN Proxy document retrieve");
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
        }

        // Audit response message
        // Audit response message
        if (isNhinAudit) {
            auditLog.auditResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        log.debug("End NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }

    private String extractHomeCommunityId(RetrieveDocumentSetRequestType request) {
        String homeCommunityId = null;
        if((request != null) && (!request.getDocumentRequest().isEmpty())) {
            String formattedHomeCommunityId = request.getDocumentRequest().get(0).getHomeCommunityId();
            if(NullChecker.isNotNullish(formattedHomeCommunityId)) {
                if(formattedHomeCommunityId.startsWith(HOME_COMMUNITY_PREFIX) && (formattedHomeCommunityId.length() > HOME_COMMUNITY_PREFIX.length())) {
                    homeCommunityId = formattedHomeCommunityId.substring(HOME_COMMUNITY_PREFIX.length());
                } else {
                    homeCommunityId = formattedHomeCommunityId;
                }
            }
        }
        return homeCommunityId;
    }

    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
