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

package org.alembic.aurion.docsubmission.passthru.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docsubmission.passthru.deferred.response.PassthruDocSubmissionDeferredResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredResponseProxyJavaImpl implements PassthruDocSubmissionDeferredResponseProxy {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PassthruDocSubmissionDeferredResponseProxyJavaImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Using Java Implementation for Passthru Doc Submission Deferred Response Service");
        return new PassthruDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(request, assertion, targetSystem);
    }

}
