/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docretrieve.passthru.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docretrieve.passthru.NhincProxyDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocRetrieveProxyJavaImpl implements PassthruDocRetrieveProxy
{
    private Log log = null;

    public PassthruDocRetrieveProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected NhincProxyDocRetrieveOrchImpl getNhincProxyDocRetrieveOrchImpl()
    {
        return new NhincProxyDocRetrieveOrchImpl();
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin PassthruDocRetrieveProxyJavaImpl.respondingGatewayCrossGatewayRetrieve");
        RetrieveDocumentSetResponseType response = null;
        NhincProxyDocRetrieveOrchImpl orchImpl = getNhincProxyDocRetrieveOrchImpl();
        if(orchImpl != null)
        {
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(request, assertion, targetSystem);
        }
        log.debug("End PassthruDocRetrieveProxyJavaImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

}
