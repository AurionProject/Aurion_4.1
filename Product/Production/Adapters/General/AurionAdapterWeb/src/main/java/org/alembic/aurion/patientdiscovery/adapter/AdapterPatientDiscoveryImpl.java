/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The implementation of the AdapterPatientDiscovery (Secured and unsecured)
 * web service.
 *
 * @author jhoppesc, Les Westberg
 */
public class AdapterPatientDiscoveryImpl
{
   private static Log log = LogFactory.getLog(AdapterPatientDiscoveryImpl.class);

    /**
     * This method is called by the secure and unsecure Adapter Secure and unsecure
     * web service.  It calls the orchestration code.
     *
     * @param bIsSecure TRUE if being called via secure web service.
     * @param request The message payload.
     * @param context The web service context.
     * @return Returns the response from the Adapter patient discovery request.
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(Boolean bIsSecure, RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context)
    {
        log.debug("Entering AdapterPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        AssertionType assertion = null;
        if ((bIsSecure) && (context != null))
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = new AssertionType();
        }

        AdapterPatientDiscoveryOrchImpl oOrchestrator = new AdapterPatientDiscoveryOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.respondingGatewayPRPAIN201305UV02(request, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }
}
