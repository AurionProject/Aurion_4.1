/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docsubmission.adapter.component.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docsubmission.adapter.component.deferred.response.AdapterComponentDocSubmissionResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the java implementation of the AdapterPatientDiscovery
 * component proxy.
 *
 * @author Les westberg
 */
public class AdapterComponentDocSubmissionResponseProxyJavaImpl implements AdapterComponentDocSubmissionResponseProxy
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterComponentDocSubmissionResponseProxyJavaImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Receive document deferred document submission response.
     *
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion)
    {
        log.debug("Entering AdapterComponentDocSubmissionResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        AdapterComponentDocSubmissionResponseOrchImpl oOrchestrator = new AdapterComponentDocSubmissionResponseOrchImpl();
        log.debug("Leaving AdapterComponentDocSubmissionResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        return oOrchestrator.provideAndRegisterDocumentSetBResponse(body, assertion);

    }
}
