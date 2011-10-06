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

package org.alembic.aurion.xdr;
import org.alembic.aurion.nhincproxyxdr.ProxyXDRPortType;
import org.alembic.aurion.nhincproxyxdr.ProxyXDRService;
import org.alembic.aurion.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.saml.extraction.SamlTokenExtractorHelper;
/**
 *
 * @author dunnek
 */
public class NhinXDRImpl {
    private static Log log = LogFactory.getLog(NhinXDRImpl.class);
    private static final String SERVICE_NAME = "mockxdr";
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
                ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body,
                javax.xml.ws.WebServiceContext context
                )
    {
        RegistryResponseType response =null;
        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = null;

        request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();

        request.setProvideAndRegisterDocumentSetRequest(body);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {
            ProxyXDRService service = new ProxyXDRService();
            ProxyXDRPortType port = service.getProxyXDRPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

            response = port.provideAndRegisterDocumentSetB(request);
        } else {
           response = null;
        }
        return response;
    }
}
