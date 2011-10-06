/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.request.error.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:33:52 PM
 */
public interface AdapterDocRetrieveDeferredReqErrorProxy {

    public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetRequestType body, AssertionType assertion, String errMsg);
}