/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxy;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxyObjectFactory;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.w3c.dom.Element;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;

/**
 *
 * @author rayj
 */
public class ProxyHiemUnsubscribeImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemUnsubscribeImpl.class);

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        SoapUtil soaputil = new SoapUtil();

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        //Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = unsubscribeRequest.getAssertion();
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
        try
        {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion, target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        }
        catch (ResourceUnknownFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        catch (UnableToDestroySubscriptionFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }

        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        SoapUtil soaputil = new SoapUtil();

        log.debug("extracting unsubscribe");
        Unsubscribe unsubscribe = unsubscribeRequest.getUnsubscribe();
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribe);
        //Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));

        log.debug("extracting target");
        NhinTargetSystemType target = unsubscribeRequest.getNhinTargetSystem();
        log.debug("extracted target");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        log.debug("extracting consumer reference elements");
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted consumer reference elements");

        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
        try
        {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, consumerReferenceElements, assertion, target);
            log.debug("invoked unsubscribe nhin component proxy");
            log.debug("unmarshall unsubscribe response to object");
            WsntUnsubscribeResponseMarshaller marshaller = new WsntUnsubscribeResponseMarshaller();
            response = marshaller.unmarshal(responseElement);
            log.debug("unmarshalled unsubscribe response to object");
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault ex)
        {
            log.error("error occurred", ex);
            //todo: throw proper exception
            response = new UnsubscribeResponse();
            response.getAny().add(ex);
        }

        log.debug("Exiting ProxyHiemUnsubscribeImpl.unsubscribe...");
        return response;
    }
}
