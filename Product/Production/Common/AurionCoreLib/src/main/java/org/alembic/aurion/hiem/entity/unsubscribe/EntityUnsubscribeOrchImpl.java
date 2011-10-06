/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.entity.unsubscribe;

import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.subscription.repository.data.HiemSubscriptionItem;
import org.alembic.aurion.subscription.repository.service.SubscriptionRepositoryException;
import javax.xml.xpath.XPathExpressionException;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.dte.TargetBuilder;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import org.alembic.aurion.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import org.alembic.aurion.subscription.repository.service.HiemSubscriptionRepositoryService;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxy;
import org.alembic.aurion.hiem.nhin.unsubscribe.proxy.NhinHiemUnsubscribeProxyObjectFactory;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhinclib.NullChecker;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;

/**
 *
 * @author rayj
 */
public class EntityUnsubscribeOrchImpl {

    private static Log log = LogFactory.getLog(EntityUnsubscribeOrchImpl.class);



    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, AssertionType assertion, NhinTargetCommunitiesType targets, ReferenceParametersElements referenceParametersElements) throws org.alembic.aurion.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault, org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault {
        log.debug("Entering EntityUnsubscribeOrchImpl.unsubscribeOps");

        //retrieve by consumer reference
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;
        try {
            log.debug("lookup subscription by reference parameters");
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParametersElements);
            log.debug("subscriptionItem isnull? = " + (subscriptionItem == null));
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
            log.debug("checking to see if subscription has child subscription");
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem.getSubscriptionReferenceXML());
            log.debug("childSubscriptions isnull? = " + (childSubscriptions == null));
        } catch (SubscriptionRepositoryException ex) {
            log.warn("failed to check for child subscription", ex);
        }

        if (NullChecker.isNotNullish(childSubscriptions)) {
            log.debug("send unsubscribe(s) to child [" + childSubscriptions.size() + "]");
            for (HiemSubscriptionItem childSubscription : childSubscriptions) {
                log.debug("sending unsubscribe to child");
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
            }
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

        log.debug("Exiting HiemUnsubscribeImpl.unsubscribe");
        return response;
    }

    private void unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target = new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());
            if (NullChecker.isNullish(target.getUrl())) {
                throw new UnableToDestroySubscriptionFault("Unable to determine where to send the unsubscribe for the child subscription", new UnableToDestroySubscriptionFaultType());
            }

            log.debug("unmarshalling unsubscribe");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
            NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemUnsubscribeProxy();
            log.debug("initialized proxy [" + proxy.toString() + "]");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
            repo.deleteSubscription(childSubscriptionItem);
            log.debug("child subscription deleted");
        } catch (SubscriptionRepositoryException ex) {
            log.error("failed to remove child subscription for repository");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        } catch (XPathExpressionException ex) {
            log.error("failed to parse subscription reference");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }
}
