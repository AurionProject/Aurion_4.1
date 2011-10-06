/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.component.deferred.request.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docretrieve.adapter.component.deferred.request.AdapterComponentDocRetrieveDeferredReqOrchImpl;
import org.alembic.aurion.docretrieve.adapter.deferred.request.AdapterDocRetrieveDeferredReqOrchImpl;
import org.alembic.aurion.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterComponentDocRetrieveDeferredReqProxyJavaImpl implements AdapterComponentDocRetrieveDeferredReqProxy {
    private Log log = null;

     public AdapterComponentDocRetrieveDeferredReqProxyJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetRequestType body, AssertionType assertion) {
         DocRetrieveAcknowledgementType                 response = new DocRetrieveAcknowledgementType();
         AdapterComponentDocRetrieveDeferredReqOrchImpl adapter = new AdapterComponentDocRetrieveDeferredReqOrchImpl();

         log.info("AdapterComponentDocRetrieveDeferredReqJavaImpl.sendToAdapter() - JavaImpl called");

         response = adapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }
}
