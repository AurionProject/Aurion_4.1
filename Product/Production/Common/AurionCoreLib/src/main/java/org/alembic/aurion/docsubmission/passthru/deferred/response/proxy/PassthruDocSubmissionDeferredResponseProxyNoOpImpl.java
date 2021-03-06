/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.passthru.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredResponseProxyNoOpImpl implements PassthruDocSubmissionDeferredResponseProxy
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PassthruDocSubmissionDeferredResponseProxyNoOpImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Using NoOp Implementation for Passthru Doc Submission Deferred Response Service");
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_RESP_ACK_STATUS_MSG);
        ack.setMessage(regResp);
        return ack;
    }

}
