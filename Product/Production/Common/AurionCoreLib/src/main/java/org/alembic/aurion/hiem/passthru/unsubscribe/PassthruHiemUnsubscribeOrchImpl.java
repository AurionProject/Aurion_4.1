/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.passthru.unsubscribe;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxy;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxyObjectFactory;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author rayj
 */
public class PassthruHiemUnsubscribeOrchImpl
{

    private static Log log = LogFactory.getLog(PassthruHiemUnsubscribeOrchImpl.class);

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements) throws UnableToDestroySubscriptionFault, ResourceUnknownFault
    {
        log.debug("Entering ProxyHiemUnsubscribeImpl.unsubscribe...");

        SoapUtil soaputil = new SoapUtil();

        log.debug("extracting unsubscribe");
        WsntUnsubscribeMarshaller wsntUnsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = wsntUnsubscribeMarshaller.marshal(unsubscribeRequest);
        //Element unsubscribeElement = soaputil.extractFirstElement(context, "unsubscribeSoapMessage", "Unsubscribe");
        log.debug(XmlUtility.formatElementForLogging("unsubscribe", unsubscribeElement));


        UnsubscribeResponse response = null;
        NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
        NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
        try
        {
            log.debug("invoke unsubscribe nhin component proxy");
            Element responseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, assertion, target);
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
