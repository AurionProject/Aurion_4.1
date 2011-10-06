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
package org.alembic.aurion.docquery.adapter.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docquery.adapter.component.deferred.request.proxy.AdapterComponentDocQueryDeferredRequestProxy;
import org.alembic.aurion.docquery.adapter.component.deferred.request.proxy.AdapterComponentDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocQueryDeferredRequestOrchImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        AdapterComponentDocQueryDeferredRequestProxyObjectFactory factory = new AdapterComponentDocQueryDeferredRequestProxyObjectFactory();
        AdapterComponentDocQueryDeferredRequestProxy proxy = factory.getAdapterDocQueryDeferredRequestProxy();

        return proxy.respondingGatewayCrossGatewayQuery(msg, assertion);
    }
}
