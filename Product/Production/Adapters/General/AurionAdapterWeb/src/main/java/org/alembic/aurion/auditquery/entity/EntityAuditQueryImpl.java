/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.auditquery.entity;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.FindAuditEventsRequestType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType;
import org.alembic.aurion.entityauditlogquerysaml.EntityAuditLogQuerySamlService;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditQueryImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityAuditQueryImpl.class);
    private static EntityAuditLogQuerySamlService service = new EntityAuditLogQuerySamlService();

    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_AUDIT_QUERY_SECURED_SERVICE_NAME);

            EntityAuditLogQuerySamlPortType port = getPort(url);

            AssertionType assertIn = findAuditEventsRequest.getAssertion();
            getSoapLogger().logRawAssertion(assertIn);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.AUDIT_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            org.alembic.aurion.common.nhinccommonentity.FindAuditEventsSecuredRequestType body = new org.alembic.aurion.common.nhinccommonentity.FindAuditEventsSecuredRequestType();
            body.setFindAuditEvents(findAuditEventsRequest.getFindAuditEvents());
            body.setNhinTargetCommunities(findAuditEventsRequest.getNhinTargetCommunities());
            response = port.findAuditEvents(body);
        }
        catch (Exception ex)
        {
            log.error("Failed to send entity audit query from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private EntityAuditLogQuerySamlPortType getPort(String url) {
        EntityAuditLogQuerySamlPortType port = service.getEntityAuditLogQuerySamlPortTypeBindingPort();

        log.info("Setting endpoint address to Entity Audit Query Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
