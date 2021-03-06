/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docrepository.adapter;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 * Helper class for the document repository service.
 * 
 * @author Neil Webb
 */
public class DocumentRepositoryServiceImpl {


    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        return new AdapterComponentDocRepositoryOrchImpl().documentRepositoryRetrieveDocumentSet(body);
    }


    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSet(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context);
        getSoapLogger().logRawAssertion(assertion);
        return new AdapterComponentDocRepositoryOrchImpl().documentRepositoryProvideAndRegisterDocumentSet(body);
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
