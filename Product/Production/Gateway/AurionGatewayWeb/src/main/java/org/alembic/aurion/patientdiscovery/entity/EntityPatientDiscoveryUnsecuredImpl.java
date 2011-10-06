/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.performance.model.AuditPerformance;
import org.alembic.aurion.performance.monitor.PerformanceMonitorUtil;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryUnsecuredImpl
{
    private Log log = null;

    public EntityPatientDiscoveryUnsecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
    {
        return new EntityPatientDiscoveryOrchImpl();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request, WebServiceContext context)
    {
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if(respondingGatewayPRPAIN201305UV02Request == null)
        {
            log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
        }
        else
        {
            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if(processor != null)
            {
                String interfaceName = getServiceNameFromContext(context);
                AuditPerformance oAuditPerformance = getAuditPerformanceRequestStr( interfaceName,context);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
                response = processor.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, respondingGatewayPRPAIN201305UV02Request.getAssertion());
                oAuditPerformance = getAuditPerformanceResponseStr( interfaceName,context);
                PerformanceMonitorUtil.getPerformanceProxy().logPerformance(oAuditPerformance);
            }
            else
            {
                log.warn("EntityPatientDiscoveryProcessor was null.");
            }
        }
        return response;
    }

    /**
     *
     * @param interfaceName
     * @param context
     * @return AuditPerformance
     */
    protected AuditPerformance getAuditPerformanceRequestStr(String interfaceName, WebServiceContext context) {
        AuditPerformance oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_REQUEST_STRING);
        return oAuditPerformance;
    }

    /**
     * 
     * @param interfaceName
     * @param context
     * @return AuditPerformance
     */
    protected AuditPerformance getAuditPerformanceResponseStr(String interfaceName, WebServiceContext context) {
        AuditPerformance oAuditPerformance;
        oAuditPerformance = PerformanceMonitorUtil.buildAuditPerfromance(0, interfaceName, AsyncMessageIdExtractor.GetAsyncMessageId(context), NhincConstants.SERVICE_RESPONSE_STRING);
        return oAuditPerformance;
    }

    /**
     * This method returns the interface name from WebServiceContext
     * @param context
     * @return String
     */
    protected String getServiceNameFromContext(WebServiceContext context) {
        String interfaceName = null;
        if (null != context && null != context.getMessageContext() && null != context.getMessageContext().get(MessageContext.WSDL_SERVICE)) {
            QName qName = (QName) context.getMessageContext().get(MessageContext.WSDL_SERVICE);
            interfaceName = qName.getLocalPart();
        }
        return interfaceName;
    }
}
