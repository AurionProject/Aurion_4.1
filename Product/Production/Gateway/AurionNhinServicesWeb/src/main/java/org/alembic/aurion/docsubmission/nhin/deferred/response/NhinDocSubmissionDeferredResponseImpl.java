/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.nhin.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author patlollav
 */
public class NhinDocSubmissionDeferredResponseImpl
{

    /**
     *
     * @param body
     * @param context
     * @return
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context)
    {
       AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

       if (assertion != null) {
            getSoapLogger().logRawAssertion(assertion);
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

       return new NhinDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(body, assertion);
        
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
