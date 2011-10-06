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
package org.alembic.aurion.docquery.adapter.deferred.response;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docquery.adapter.component.deferred.response.proxy.AdapterComponentDocQueryDeferredResponseProxy;
import org.alembic.aurion.docquery.adapter.component.deferred.response.proxy.AdapterComponentDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocQueryDeferredResponseOrchImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        AdapterComponentDocQueryDeferredResponseProxyObjectFactory factory = new AdapterComponentDocQueryDeferredResponseProxyObjectFactory();
        AdapterComponentDocQueryDeferredResponseProxy proxy = factory.getAdapterDocQueryDeferredResponseProxy();

        return proxy.respondingGatewayCrossGatewayQuery(msg, assertion);
    }
}
