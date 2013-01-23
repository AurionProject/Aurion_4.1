/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.nhin.deferred.response;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncRespImpl
{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryAsyncRespImpl.class);

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(PRPAIN201306UV02 body, WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            getSoapLogger().logRawAssertion(assertion);
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return new NhinPatientDiscoveryDeferredRespOrchImpl().respondingGatewayPRPAIN201306UV02Orch(body, assertion);
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
