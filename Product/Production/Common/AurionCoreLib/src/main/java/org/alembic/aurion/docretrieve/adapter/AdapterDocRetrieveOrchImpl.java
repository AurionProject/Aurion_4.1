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
package org.alembic.aurion.docretrieve.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxy;
import org.alembic.aurion.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxyObjectFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import org.alembic.aurion.redactionengine.adapter.proxy.AdapterRedactionEngineProxyObjectFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author westberg
 */
public class AdapterDocRetrieveOrchImpl
{
    private Log log = null;

    public AdapterDocRetrieveOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        log.debug("Enter AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        RetrieveDocumentSetResponseType response = null;

        try
        {
            AdapterComponentDocRepositoryProxy proxy = new AdapterComponentDocRepositoryProxyObjectFactory().getAdapterDocumentRepositoryProxy();
            response = proxy.retrieveDocument(body, assertion);
            response = callRedactionEngine(body, response, assertion);
        }
        catch(Throwable t)
        {
            log.error("Error processing an adapter document retrieve message: " + t.getMessage(), t);
            response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }
        log.debug("Leaving AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

    protected RetrieveDocumentSetResponseType callRedactionEngine(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse, AssertionType assertion)
    {
        RetrieveDocumentSetResponseType response = null;
        if(retrieveResponse == null)
        {
            log.warn("Did not call redaction engine because the retrieve response was null.");
        }
        else
        {
            response = getRedactionEngineProxy().filterRetrieveDocumentSetResults(retrieveRequest, retrieveResponse, assertion);
        }
        return response;
    }

    protected AdapterRedactionEngineProxy getRedactionEngineProxy()
    {
        return new AdapterRedactionEngineProxyObjectFactory().getRedactionEngineProxy();
    }

}
