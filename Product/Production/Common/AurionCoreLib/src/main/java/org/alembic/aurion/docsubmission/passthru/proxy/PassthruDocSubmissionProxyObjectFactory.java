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

package org.alembic.aurion.docsubmission.passthru.proxy;

import org.alembic.aurion.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentSubmissionProxyConfig.xml";
    private static final String BEAN_NAME = "passthrudocsubmission";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocSubmissionProxy getPassthruDocSubmissionProxy() {
        return getBean(BEAN_NAME, PassthruDocSubmissionProxy.class);
    }

}
