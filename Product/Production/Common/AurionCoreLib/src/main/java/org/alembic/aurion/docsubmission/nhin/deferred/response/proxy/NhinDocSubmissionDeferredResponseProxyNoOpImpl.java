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

package org.alembic.aurion.docsubmission.nhin.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseProxyNoOpImpl implements NhinDocSubmissionDeferredResponseProxy {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhinDocSubmissionDeferredResponseProxyNoOpImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(RegistryResponseType body, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Using NoOp Implementation for Nhin Doc Submission Deferred Response Service");
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_RESP_ACK_STATUS_MSG);
        ack.setMessage(regResp);
        return ack;
    }

}
