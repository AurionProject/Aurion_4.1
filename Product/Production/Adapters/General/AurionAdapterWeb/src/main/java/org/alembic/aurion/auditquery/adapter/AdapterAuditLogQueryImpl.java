/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */

package org.alembic.aurion.auditquery.adapter;

import javax.xml.ws.WebServiceContext;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 * This class performs the actual work of class methods to query the audit log.
 *
 * @author Clark Shaw
 */
public class AdapterAuditLogQueryImpl
{
    private static Log log = LogFactory.getLog(AdapterAuditLogQueryImpl.class);

    protected AdapterAuditLogQueryOrchImpl getAdapterAuditLogQueryOrchImpl() 
    {
    	return new AdapterAuditLogQueryOrchImpl();
    }
    
	protected AssertionType extractAssertionFromContext(WebServiceContext context) {
		return SamlTokenExtractor.GetAssertion(context);
	}

    /**
     * This method will perform an audit log query to the local audit repository.  A list of audit
     * log records will be returned to the calling community that match the search criteria.
     *
     * @param query The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType queryAdapter(com.services.nhinc.schema.auditmessage.FindAuditEventsType query, WebServiceContext context)
    {
        log.debug("Entering AdapterSecuredAuditLogQueryImpl.queryAdapter(FindAuditEventsType, WebServiceContext)...");

        AssertionType assertion = extractAssertionFromContext(context);
        
        FindAuditEventsResponseType queryResponse = queryAdapter(query, assertion);
        
        log.debug("Exiting AdapterSecuredAuditLogQueryImpl.queryAdapter(FindAuditEventsType, WebServiceContext)...");
        return queryResponse;
    }

    public FindAuditEventsResponseType queryAdapter(FindAuditEventsType queryRequest, AssertionType assertion)
    {
        log.debug("Entering AdapterSecuredAuditLogQueryImpl.queryAdapter(FindAuditEventsType, AssertionType)...");
        getSoapLogger().logRawAssertion(assertion);
        if(queryRequest != null)
        {
            log.debug("incomming adapter audit query request: " + queryRequest.toString());
        }

        FindAuditEventsResponseType queryResponse = getAdapterAuditLogQueryOrchImpl().performAuditLogQuery(queryRequest, assertion);

        log.debug("Exiting AdapterSecuredAuditLogQueryImpl.queryAdapter(FindAuditEventsType, AssertionType)...");
        return queryResponse;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
