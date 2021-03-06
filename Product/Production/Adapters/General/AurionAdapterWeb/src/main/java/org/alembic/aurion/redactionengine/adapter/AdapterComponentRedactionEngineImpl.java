/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.redactionengine.adapter;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentRedactionEngineImpl {

    private Log log = null;

    public AdapterComponentRedactionEngineImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType filterDocQueryResultsRequest, WebServiceContext context) {
        log.debug("Begin filterDocQueryResults");
        FilterDocQueryResultsResponseType response = null;
        AssertionType assertion = getAssertion(context);
        getSoapLogger().logRawAssertion(assertion);

        if (filterDocQueryResultsRequest == null) {
            log.warn("FilterDocQueryResultsRequestType was null");
        } else {
            AdhocQueryResponse adhocQueryResponse = invokeRedactionEngineForQuery(filterDocQueryResultsRequest);
            response = new FilterDocQueryResultsResponseType();
            response.setAdhocQueryResponse(adhocQueryResponse);
        }
        log.debug("end filterDocQueryResults");
        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest, WebServiceContext context) {
        FilterDocRetrieveResultsResponseType response = null;
        AssertionType assertion = getAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        
        if (filterDocRetrieveResultsRequest == null) {
            log.warn("FilterDocRetrieveResultsRequestType was null");
        } else {
            RetrieveDocumentSetResponseType retrieveDocSetResonse = invokeRedactionEngineForRetrieve(filterDocRetrieveResultsRequest);
            response = new FilterDocRetrieveResultsResponseType();
            response.setRetrieveDocumentSetResponse(retrieveDocSetResonse);
        }
        return response;
    }

    protected AssertionType getAssertion(WebServiceContext context) {
        AssertionType assertion = new AssertionType();

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));

        return assertion;
    }

    protected AdhocQueryResponse invokeRedactionEngineForQuery (FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
        AdhocQueryResponse adhocQueryResponse = new AdapterRedactionEngineOrchImpl().filterAdhocQueryResults(filterDocQueryResultsRequest.getAdhocQueryRequest(), filterDocQueryResultsRequest.getAdhocQueryResponse());

        return adhocQueryResponse;
    }

    protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
        RetrieveDocumentSetResponseType retrieveDocSetResponse = new AdapterRedactionEngineOrchImpl().filterRetrieveDocumentSetResults(filterDocRetrieveResultsRequest.getRetrieveDocumentSetRequest(), filterDocRetrieveResultsRequest.getRetrieveDocumentSetResponse());

        return retrieveDocSetResponse;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
