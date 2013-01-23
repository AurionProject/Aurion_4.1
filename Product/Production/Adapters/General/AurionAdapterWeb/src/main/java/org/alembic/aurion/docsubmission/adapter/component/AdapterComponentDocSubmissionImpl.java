/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.component;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author dunnek
 */
public class AdapterComponentDocSubmissionImpl {

    public RegistryResponseType provideAndRegisterDocumentSetb(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());
        getSoapLogger().logRawAssertion(assertion);

        return new AdapterComponentDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion);
    }

    public RegistryResponseType provideAndRegisterDocumentSetb(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);

        return new AdapterComponentDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body, assertion);
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
