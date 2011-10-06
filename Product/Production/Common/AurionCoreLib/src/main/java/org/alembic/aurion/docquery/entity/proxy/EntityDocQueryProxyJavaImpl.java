/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.entity.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.docquery.entity.EntityDocQueryOrchImpl;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public class EntityDocQueryProxyJavaImpl implements EntityDocQueryProxy
{

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        AdhocQueryResponse response = null;

        EntityDocQueryOrchImpl orchImpl = new EntityDocQueryOrchImpl();
        response = orchImpl.respondingGatewayCrossGatewayQuery(msg, assertion, targets);

        return response;
    }
}
