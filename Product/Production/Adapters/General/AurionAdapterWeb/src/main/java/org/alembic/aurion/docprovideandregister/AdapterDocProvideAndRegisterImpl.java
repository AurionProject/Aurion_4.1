/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docprovideandregister;

import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import org.alembic.aurion.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType;
import org.alembic.aurion.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxy;
import org.alembic.aurion.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocProvideAndRegisterImpl {
    public RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType adapterDocProvideAndRegisterOperation(RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType part1)
    {
        RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType res =
                    new RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType();
        if(part1 != null)
        {
            getSoapLogger().logRawAssertion(part1.getAssertion());
            AdapterDocumentRepositoryProxyObjectFactory objFactory = new AdapterDocumentRepositoryProxyObjectFactory();
            AdapterDocumentRepositoryProxy registryProxy = objFactory.getAdapterDocumentRepositoryProxy();
            RegistryResponseType response = registryProxy.provideAndRegisterDocumentSet(part1.getProvideAndRegisterDocumentSetRequest());
            res.setRegistryResponse(response);
        }
        return res;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
