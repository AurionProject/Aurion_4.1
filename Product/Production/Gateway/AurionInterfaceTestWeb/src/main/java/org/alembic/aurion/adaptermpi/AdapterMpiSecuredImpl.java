/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adaptermpi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.adaptercomponentmpi.AdapterComponentMpiService;
import org.alembic.aurion.adaptercomponentmpi.AdapterComponentMpiPortType;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author dunnek
 */
public class AdapterMpiSecuredImpl {
    private static Log log = LogFactory.getLog(AdapterMpiSecuredImpl.class);
    private static final String SERVICE_NAME = "mockadaptermpi";
    private static AdapterComponentMpiService service = new AdapterComponentMpiService();
    
    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest,WebServiceContext context) {
        PRPAIN201306UV02 response =null;

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {

            AdapterComponentMpiPortType port = service.getAdapterComponentMpiPort();
            response = port.findCandidates(findCandidatesRequest);
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        }

        return response;
    }
}
