/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.common.auditlog.AdhocQueryResponseMessageType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifiersType;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.gateway.aggregator.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.gateway.aggregator.GetAggResultsDocQueryRequestType;
import org.alembic.aurion.gateway.aggregator.GetAggResultsDocQueryResponseType;
import org.alembic.aurion.gateway.aggregator.StartTransactionDocQueryRequestType;
import org.alembic.aurion.gateway.aggregator.document.DocQueryAggregator;
import java.util.HashMap;

public class EntityDocQueryOrchImpl
{

    private Log log = null;
    private String localHomeCommunity = null;
    private String localAssigningAuthorityId = null;
    private static final long SLEEP_MS = 1000;
    private static final long AGGREGATOR_TIMEOUT_MS = 40000;

    public EntityDocQueryOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        log.debug("Entering EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = null;
        CMUrlInfos urlInfoList = null;
        boolean isTargeted = false;
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        DocQueryAuditLog auditLog = createAuditLog();
        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setNhinTargetCommunities(targets);
        if (isEntityAudit) {
            auditDocQueryRequest(request, assertion, auditLog);
        }

        try
        {
            DocQueryAggregator aggregator = createDocQueryAggregator();

            if (targets != null &&
                    NullChecker.isNotNullish(targets.getNhinTargetCommunity()))
            {
                isTargeted = true;
                log.debug("targets has data.  (is not null)");
            }
            else
            {
                log.debug("targets is null. ");
            }

            // Obtain all the URLs for the targets being sent to
            try
            {
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targets, NhincConstants.DOC_QUERY_SERVICE_NAME);
                if ((urlInfoList != null) && (urlInfoList.getUrlInfo() != null))
                {
                    log.debug("urlInfoList.count = " + urlInfoList.getUrlInfo().size());
                    int i = 0;
                    for (CMUrlInfo oUrlInfo : urlInfoList.getUrlInfo())
                    {
                        log.debug("Urlinfo[" + i + "]: HomeCommunityId: " + oUrlInfo.getHcid() + ", URL: " +  oUrlInfo.getUrl());
                        i++;
                    }
                }
                else
                {
                    log.debug("urlInfoList is null" );
                }
            } catch (ConnectionManagerException ex)
            {
                log.error("Failed to obtain target URLs");
                return null;
            }

            // Validate that the message is not null
            if (adhocQueryRequest != null &&
                    adhocQueryRequest.getAdhocQuery() != null &&
                    NullChecker.isNotNullish(adhocQueryRequest.getAdhocQuery().getSlot()))
            {
                List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
                String localAA = new EntityDocQueryHelper().getLocalAssigningAuthority(slotList);

                List<QualifiedSubjectIdentifierType> correlationsResult = new EntityDocQueryHelper().retreiveCorrelations(slotList, urlInfoList, assertion, isTargeted, getLocalHomeCommunityId());

                // Make sure the valid results back
                if (NullChecker.isNotNullish(correlationsResult))
                {

                    QualifiedSubjectIdentifiersType subjectIds = new QualifiedSubjectIdentifiersType();
                    for (QualifiedSubjectIdentifierType subjectId : correlationsResult)
                    {
                        if (subjectId != null)
                        {
                            subjectIds.getQualifiedSubjectIdentifier().add(subjectId);
                        }
                    }
                    DocQueryCorrelationFilter correlationFilter = new DocQueryCorrelationFilter(subjectIds);
                    subjectIds = correlationFilter.filterDuplicateCorrelations();

                    String transactionId = startTransaction(aggregator, subjectIds);

                    sendQueryMessages(transactionId, correlationsResult, adhocQueryRequest, assertion, localAA);

                    response = retrieveDocQueryResults(aggregator, transactionId);
                } else
                {
                    log.error("No patient correlations found.");
                    response = createErrorResponse("No patient correlations found.");
                }
            } else
            {
                log.error("Incomplete doc query message");
                response = createErrorResponse("Incomplete doc query message");
            }
        } catch (Throwable t)
        {
            log.error("Error occured processing doc query on entity interface: " + t.getMessage(), t);
            response = createErrorResponse("Fault encountered processing internal document query");
        }
        if (isEntityAudit) {
            auditDocQueryResponse(response, assertion, auditLog);
        }
        log.debug("Exiting EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    protected DocQueryAuditLog createAuditLog()
    {
        return new DocQueryAuditLog();
    }

    protected DocQueryAggregator createDocQueryAggregator()
    {
        return new DocQueryAggregator();
    }

    private void auditDocQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request, AssertionType assertion, DocQueryAuditLog auditLog)
    {
        if (auditLog != null)
        {
            auditLog.audit(request, assertion);
        }
    }

    private void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion, DocQueryAuditLog auditLog)
    {
        if (auditLog != null)
        {
            AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
            auditMsg.setAdhocQueryResponse(response);
            auditMsg.setAssertion(assertion);
            auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
    }

   

    protected boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }

    protected String getLocalHomeCommunityId()
    {
        String sHomeCommunity = null;

        if (localHomeCommunity != null)
        {
            sHomeCommunity = localHomeCommunity;
        } else
        {
            try
            {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex)
            {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    private void sendQueryMessages(String transactionId, List<QualifiedSubjectIdentifierType> correlationsResult, AdhocQueryRequest queryRequest, AssertionType assertion, String localAA)
    {
        for (QualifiedSubjectIdentifierType subId : correlationsResult)
        {
            DocQuerySender querySender = new DocQuerySender(transactionId, assertion, subId, queryRequest, localAA);
            querySender.sendMessage();
        }
    }

    private AdhocQueryResponse retrieveDocQueryResults(DocQueryAggregator aggregator, String transactionId)
    {
        log.debug("Begin retrieveDocQueryResults");
        AdhocQueryResponse response = null;
        boolean retrieveTimedOut = false;

        // Agggregator query request message
        GetAggResultsDocQueryRequestType aggResultsRequest = new GetAggResultsDocQueryRequestType();
        aggResultsRequest.setTransactionId(transactionId);
        aggResultsRequest.setTimedOut(retrieveTimedOut);

        // Loop until responses are received
        long startTime = System.currentTimeMillis();
        while (response == null)
        {
            GetAggResultsDocQueryResponseType aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
            String retrieveStatus = aggResultsResponse.getStatus();
            if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus))
            {
                response = aggResultsResponse.getAdhocQueryResponse();
            } else if (DocumentConstants.FAIL_TEXT.equals(retrieveStatus))
            {
                log.error("Document query aggregator reports failurt - returning error");
                response = createErrorResponse("Processing internal document query");
            } else
            {
                retrieveTimedOut = retrieveTimedOut(startTime);
                if (retrieveTimedOut)
                {
                    aggResultsRequest.setTimedOut(retrieveTimedOut);
                    aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
                    if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus))
                    {
                        response = aggResultsResponse.getAdhocQueryResponse();
                    } else
                    {
                        log.warn("Document query aggregation timeout - returning error.");
                        response = createErrorResponse("Processing internal document query - failure in timeout");
                    }
                } else
                {
                    try
                    {
                        Thread.sleep(SLEEP_MS);
                    } catch (Throwable t)
                    {
                    }
                }
            }
        }
        log.debug("End retrieveDocQueryResults");
        return response;
    }

    private boolean retrieveTimedOut(long startTime)
    {
        long timeout = startTime + AGGREGATOR_TIMEOUT_MS;
        return (timeout < System.currentTimeMillis());
    }

    private String startTransaction(DocQueryAggregator aggregator, QualifiedSubjectIdentifiersType subjectIds)
    {
        StartTransactionDocQueryRequestType docQueryStartTransaction = new StartTransactionDocQueryRequestType();
        docQueryStartTransaction.setQualifiedPatientIdentifiers(subjectIds);

        HashMap<String, String> assigningAuthorityToHomeCommunityMap = new HashMap<String, String>();
        log.debug("Starting doc query transaction");
        String transactionId = aggregator.startTransaction(docQueryStartTransaction, assigningAuthorityToHomeCommunityMap);
        if (log.isDebugEnabled())
        {
            log.debug("Doc query transaction id: " + transactionId);
        }
        return transactionId;
    }

    private AdhocQueryResponse createErrorResponse(String codeContext)
    {
        AdhocQueryResponse response = new AdhocQueryResponse();

        RegistryErrorList regErrList = new RegistryErrorList();
        response.setRegistryErrorList(regErrList);
        response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return response;
    }
}
