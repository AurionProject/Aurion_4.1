/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
public class EntityDocRetreiveImpl {

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, final WebServiceContext context) {
        String interfaceName = getServiceNameFromContext(context);
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        setMessageID (assertion, context);

        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        RetrieveDocumentSetResponseType oResponse = this.respondingGatewayCrossGatewayQuery(body, assertion);
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        return oResponse;
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion, final WebServiceContext context) {
        String interfaceName = getServiceNameFromContext(context);
        getSoapLogger().logRawAssertion(assertion);
        AssertionType assertionWithId = getAssertion(context, assertion);
        setMessageID (assertionWithId, context);
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        RetrieveDocumentSetResponseType oResponse = this.respondingGatewayCrossGatewayQuery(body, assertionWithId);
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
        return oResponse;
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion) {
        return getEntityOrchImpl().respondingGatewayCrossGatewayRetrieve(body, assertion);
    }

    protected EntityDocRetrieveOrchImpl getEntityOrchImpl() {
        return new EntityDocRetrieveOrchImpl();
    }

    protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    /**
     * This method returns the interface name from WebServiceContext
     * @param context
     * @return String
     */
    private String getServiceNameFromContext(WebServiceContext context) {
        String interfaceName = null;
        if (null != context && null != context.getMessageContext() && null != context.getMessageContext().get(MessageContext.WSDL_SERVICE)) {
            QName qName = (QName) context.getMessageContext().get(MessageContext.WSDL_SERVICE);
            interfaceName = qName.getLocalPart();
        }
        return interfaceName;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
