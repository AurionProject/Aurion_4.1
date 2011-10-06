/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.adapter.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:41:47 PM
 */
public interface AdapterDocRetrieveDeferredRespProxy {

    public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetResponseType body, AssertionType assertion);
}
