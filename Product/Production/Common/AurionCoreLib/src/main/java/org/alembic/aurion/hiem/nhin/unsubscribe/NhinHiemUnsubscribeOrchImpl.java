/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.nhin.unsubscribe;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.processor.faults.ConfigurationException;
import org.alembic.aurion.hiem.adapter.unsubscribe.proxy.AdapterHiemUnsubscribeProxy;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.subscription.repository.data.HiemSubscriptionItem;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.alembic.aurion.hiem.configuration.ConfigurationManager;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.dte.TargetBuilder;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import org.alembic.aurion.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import org.alembic.aurion.hiem.adapter.unsubscribe.proxy.AdapterHiemUnsubscribeProxyObjectFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.subscription.repository.service.HiemSubscriptionRepositoryService;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

/**
 *
 * @author jhoppesc
 */
public class NhinHiemUnsubscribeOrchImpl {

    private static Log log = LogFactory.getLog(NhinHiemUnsubscribeOrchImpl.class);

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, ReferenceParametersElements referenceParametersElements, AssertionType assertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        UnsubscribeResponse response;
        try {
            response = unsubscribeOps(unsubscribeRequest, referenceParametersElements, assertion);
        } catch (Exception ex) {
            throw new SubscriptionManagerSoapFaultFactory().getGenericProcessingExceptionFault(ex);
        }
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(Unsubscribe unsubscribeRequest, ReferenceParametersElements referenceParametersElements, AssertionType assertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        log.debug("Entering NhinHiemUnsubscribeOrchImpl.unsubscribe");

        //retrieve by consumer reference
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;
        try {
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParametersElements);
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex);
            throw new SubscriptionManagerSoapFaultFactory().getErrorDuringSubscriptionRetrieveFault(ex);
        }

        if (subscriptionItem == null) {
            throw new SubscriptionManagerSoapFaultFactory().getUnableToFindSubscriptionFault();
        }

        //todo: if has parent, retrieve parent, forward unsubscribe to agency
        List<HiemSubscriptionItem> childSubscriptions = null;
        try {
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem.getSubscriptionReferenceXML());
        } catch (SubscriptionRepositoryException ex) {
            log.warn("failed to check for child subscription", ex);
        }

        if (NullChecker.isNotNullish(childSubscriptions)) {
            log.debug("send unsubscribe(s) to child");
            for (HiemSubscriptionItem childSubscription : childSubscriptions) {
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
            }
        } else if (isForwardUnsubscribeToAdapter()) {
            log.debug("forward unsubscribe to adapter");
            forwardUnsubscribeToAdapter(unsubscribeRequest, referenceParametersElements, assertion);
        } else {
        }

        //"remove" from local repo
        log.debug("invoking subscription storage service to delete subscription");
        try {
            repo.deleteSubscription(subscriptionItem);
        } catch (SubscriptionRepositoryException ex) {
            log.error("unable to delete subscription.  This should result in a unable to remove subscription fault", ex);
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }

        UnsubscribeResponse response = new UnsubscribeResponse();

        log.debug("Exiting NhinHiemUnsubscribeImpl.unsubscribe");
        return response;
    }

    private boolean isForwardUnsubscribeToAdapter() {
        boolean forward = false;
        ConfigurationManager config = new ConfigurationManager();
        String mode = null;
        try {
            mode = config.getAdapterSubscriptionMode();
        } catch (ConfigurationException ex) {
            log.warn("unable to determine adapter subscription mode");
            forward =
                    false;
        }

        if (NullChecker.isNotNullish(mode)) {
            if (!mode.contentEquals(NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED)) {
                forward = true;
            }

        }
        return forward;
    }

    private void forwardUnsubscribeToAdapter(Unsubscribe parentUnsubscribe, ReferenceParametersElements parentReferenceParametersElements, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault {
//        try {
        log.debug("forwarding unsubscribe to adapter");

        log.debug("target to be filled in by proxy using connection manager");
        NhinTargetSystemType target = null;

        log.debug("unmarshalling unsubscribe");
        WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
        log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

        log.debug("using reference parameters from parent message [" + parentReferenceParametersElements.getElements().size() + " element(s)]");

        log.debug("initialize proxy");
        AdapterHiemUnsubscribeProxyObjectFactory factory = new AdapterHiemUnsubscribeProxyObjectFactory();
        AdapterHiemUnsubscribeProxy proxy = factory.getAdapterHiemUnsubscribeProxy();
        log.debug("initialized proxy");

        log.debug("sending unsubscribe");
        UnsubscribeResponse unsubscribeResponse = proxy.unsubscribe(unsubscribeElement, parentReferenceParametersElements, parentAssertion);
        log.debug("unsubscribe response received");

    }

    private void unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target =
                    new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());

            log.debug("unmarshalling unsubscribe");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            AdapterHiemUnsubscribeProxyObjectFactory factory = new AdapterHiemUnsubscribeProxyObjectFactory();
            AdapterHiemUnsubscribeProxy proxy = factory.getAdapterHiemUnsubscribeProxy();
            log.debug("initialized proxy");

            log.debug("sending unsubscribe");
            UnsubscribeResponse unsubscribeResponse = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, parentAssertion);
            log.debug("unsubscribe response received");

            log.debug("invoking subscription repository to remove child subscription");
            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
            repo.deleteSubscription(childSubscriptionItem);
            log.debug("child subscription deleted");
        } catch (SubscriptionRepositoryException ex) {
            log.error("failed to remove child subscription for repository");
            throw new  SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        } catch (XPathExpressionException ex) {
            log.error("failed to parse subscription reference");
            throw new  SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }
}
