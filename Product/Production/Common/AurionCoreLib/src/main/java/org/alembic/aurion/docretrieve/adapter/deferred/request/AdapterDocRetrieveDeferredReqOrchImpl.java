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

package org.alembic.aurion.docretrieve.adapter.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docquery.adapter.component.deferred.request.proxy.AdapterComponentDocQueryDeferredRequestProxy;
import org.alembic.aurion.docquery.adapter.component.deferred.request.proxy.AdapterComponentDocQueryDeferredRequestProxyObjectFactory;
import org.alembic.aurion.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxy;
import org.alembic.aurion.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredReqOrchImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredReqOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion)
    {
        DocRetrieveAcknowledgementType      result = null;
        
        log.debug("Enter AdapterDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");
        AdapterComponentDocRetrieveDeferredReqProxyObjectFactory factory = new AdapterComponentDocRetrieveDeferredReqProxyObjectFactory();
        AdapterComponentDocRetrieveDeferredReqProxy proxy = factory.getAdapterDocRetrieveDeferredRequestProxy();

        result = proxy.sendToAdapter(body, assertion);

        log.debug("Leaving AdapterDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        return result;
    }

}
