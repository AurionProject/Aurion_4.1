/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docretrieve.adapter.deferred.response.AdapterDocRetrieveDeferredRespOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterDocRetrieveDeferredRespProxyJavaImpl implements AdapterDocRetrieveDeferredRespProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredRespProxyJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetResponseType body, AssertionType assertion) {
         DocRetrieveAcknowledgementType             response = new DocRetrieveAcknowledgementType();
         AdapterDocRetrieveDeferredRespOrchImpl  adapter = new AdapterDocRetrieveDeferredRespOrchImpl();

         response = adapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }
}
