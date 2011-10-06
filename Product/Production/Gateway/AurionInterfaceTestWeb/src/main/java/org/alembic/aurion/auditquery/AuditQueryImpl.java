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
package org.alembic.aurion.auditquery;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import org.alembic.aurion.common.nhinccommoninternalorch.FindAuditEventsRequestType;
import org.alembic.aurion.nhincauditlogquery.NhincAuditLogQueryPortType;
import org.alembic.aurion.nhincauditlogquery.NhincAuditLogQueryService;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AuditQueryImpl {

    private static Log log = LogFactory.getLog(AuditQueryImpl.class);
    private static final String SERVICE_NAME = "mockauditquery";

    public static FindAuditEventsResponseType auditQuery(FindAuditEventsType query, WebServiceContext context) {
        log.debug("Entering AuditQueryImpl.auditQuery");
        FindAuditEventsResponseType resp = new FindAuditEventsResponseType();
        FindAuditEventsRequestType request = new FindAuditEventsRequestType();

        request.setFindAuditEvents(query);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincAuditLogQueryService service = new NhincAuditLogQueryService();
            NhincAuditLogQueryPortType port = service.getNhincAuditLogcQueryPortTypeBindingPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

            resp = port.nhincAuditLogQuery(request);
        } else {
            return null;
        }

        log.debug("Exiting AuditQueryImpl.auditQuery");
        return resp;
    }
}
