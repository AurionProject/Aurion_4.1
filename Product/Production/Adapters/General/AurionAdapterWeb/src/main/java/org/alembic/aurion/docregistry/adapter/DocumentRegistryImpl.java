/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docregistry.adapter;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 * Helper class for performing document registry operations
 * 
 * @author Neil Webb
 */
public class DocumentRegistryImpl
{

    public AdhocQueryResponse documentRegistryRegistryStoredQuery(AdhocQueryRequest body, WebServiceContext context)
    {
        AssertionType assertion = getAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        return new AdapterComponentDocRegistryOrchImpl().registryStoredQuery(body);
    }

    protected AssertionType getAssertion(WebServiceContext context) {
        AssertionType assertion = new AssertionType();

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));

        return assertion;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
