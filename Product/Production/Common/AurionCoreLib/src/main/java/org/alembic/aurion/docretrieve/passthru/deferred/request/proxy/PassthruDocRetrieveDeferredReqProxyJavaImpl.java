/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru.deferred.request.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docretrieve.passthru.deferred.request.NhincProxyDocRetrieveDeferredReqOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 *
 * @author Sai Valluripalli
 */
public class PassthruDocRetrieveDeferredReqProxyJavaImpl implements PassthruDocRetrieveDeferredReqProxy
{
    /**
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request,
                                                                      AssertionType assertion,
                                                                      NhinTargetSystemType target) {
        return new NhincProxyDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(request, assertion, target);
    }

}
