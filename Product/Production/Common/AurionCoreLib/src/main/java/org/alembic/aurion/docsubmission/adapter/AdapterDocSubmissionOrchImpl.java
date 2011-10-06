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

package org.alembic.aurion.docsubmission.adapter;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.docsubmission.adapter.component.proxy.AdapterComponentDocSubmissionProxy;
import org.alembic.aurion.docsubmission.adapter.component.proxy.AdapterComponentDocSubmissionProxyObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocSubmissionOrchImpl {
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion) {
        AdapterComponentDocSubmissionProxyObjectFactory factory = new AdapterComponentDocSubmissionProxyObjectFactory();
        AdapterComponentDocSubmissionProxy proxy = factory.getAdapterComponentDocSubmissionProxy();

        RegistryResponseType response = proxy.provideAndRegisterDocumentSetB(msg, assertion);

        return response;
    }

}
