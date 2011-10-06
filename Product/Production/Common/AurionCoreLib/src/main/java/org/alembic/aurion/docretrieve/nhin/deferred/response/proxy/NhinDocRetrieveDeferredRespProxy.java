/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.nhin.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;


/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 10:56:29 PM
 */
public interface NhinDocRetrieveDeferredRespProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetResponseType body, AssertionType assertion, NhinTargetSystemType target);
}
