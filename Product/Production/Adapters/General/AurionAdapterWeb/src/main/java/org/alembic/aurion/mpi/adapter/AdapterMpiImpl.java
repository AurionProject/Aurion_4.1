/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.mpi.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;

import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author Jon Hoppesch
 */

public class AdapterMpiImpl {
   private static Log log = LogFactory.getLog(AdapterMpiImpl.class);

    /**
     * Perform a look up on the MPI.
     *
     * @param bIsSecure  TRUE if this is being called from a secure web service.
     * @param findCandidatesRequest The information about the patient that is being used for the lookup.
     * @param assertionFromBody Assertion provided in the body of the message.
     * @param context The web service context information.
     * @return The results of the lookup.
     */
    public  PRPAIN201306UV02 query(boolean bIsSecure, PRPAIN201305UV02 findCandidatesRequest, AssertionType assertionFromBody, WebServiceContext context)
    {
        log.debug("Entering AdapterMpiImpl.findCandidates");

        AssertionType assertion = null;
        if ((bIsSecure) && (context != null))
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else if(assertionFromBody != null)
        {
            assertion = assertionFromBody;
        }
        else
        {
            assertion = new AssertionType();
        }
        getSoapLogger().logRawAssertion(assertion);

        AdapterMpiOrchImpl oOrchestrator = new AdapterMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.query(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterMpiImpl.findCandidates");
        return response;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
