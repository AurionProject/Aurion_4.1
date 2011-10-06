/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.entity.deferred.request.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.docretrieve.entity.deferred.request.EntityDocRetrieveDeferredReqOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * Java implementation for Entity Document retrieve deferred unsecured request webservice call
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqProxyJavaImpl implements EntityDocRetrieveDeferredReqProxy {

    /**
     * 
     * @param message
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target) {
        return getEntityDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     * 
     * @return EntityDocRetrieveDeferredReqImpl
     */
    protected EntityDocRetrieveDeferredReqOrchImpl getEntityDocRetrieveDeferredReqOrchImpl()
    {
        return new EntityDocRetrieveDeferredReqOrchImpl();
    }
}
