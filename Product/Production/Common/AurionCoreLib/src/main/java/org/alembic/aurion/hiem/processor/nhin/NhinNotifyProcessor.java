/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.processor.nhin;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.adapter.notify.proxy.AdapterHiemNotifyProxy;
import org.alembic.aurion.hiem.adapter.notify.proxy.AdapterHiemNotifyProxyObjectFactory;
import org.alembic.aurion.hiem.configuration.ConfigurationManager;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.processor.common.HiemProcessorConstants;

import org.alembic.aurion.xmlCommon.XmlUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;
import org.alembic.aurion.hiem.dte.Namespaces;
import org.alembic.aurion.hiem.dte.marshallers.NotificationMessageMarshaller;
import org.alembic.aurion.hiem.dte.marshallers.NotifyMarshaller;
import org.alembic.aurion.subscription.repository.data.HiemSubscriptionItem;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.alembic.aurion.subscription.repository.service.HiemSubscriptionRepositoryService;
import java.util.List;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinNotifyProcessor {

    private static Log log = LogFactory.getLog(NhinNotifyProcessor.class);

    /**
     * Perform processing for an NHIN notify message.
     *
     * @param notify NHIN notify message
     * @param assertion Assertion information extracted from the SOAP header
     * @return void
     * @throws java.lang.Exception
     */
    public void processNhinNotify(Element soapMessage, AssertionType assertion) throws Exception {
        log.debug("In processNhinNotify");

        Element notify = XmlUtility.getSingleChildElement(soapMessage, Namespaces.WSNT, "Notify");

        try {

            ConfigurationManager config = new ConfigurationManager();
            String serviceMode = config.getNotificationServiceMode();

            if (HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH.equals(serviceMode)) {
                log.debug("In passthrough mode");

                log.debug("extracting reference parameters from soap header");
                ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
                ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSoapMessage(soapMessage);
                log.debug("extracted reference parameters from soap header");

                forwardToAgency(notify, referenceParametersElements, assertion);
            } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED.equals(serviceMode)) {
                log.debug("Notfications are not supported");
                // TODO: Figure out how to create this fault and throw it
                //throw new NotifyMessageNotSupportedFaultType();
                throw new Exception("Notification not supported");
            } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED.equalsIgnoreCase(serviceMode)) {
                log.debug("Notifications are supported. Processing notify message");
                nhinNotify(notify, assertion);
            } else {
                log.error("Unknown notification service mode: " + serviceMode);
                throw new Exception("Unsupported notification service mode of \"" + serviceMode + "\" for property: " + HiemProcessorConstants.HIEM_SERVICE_MODE_SUBSCRIPTION_PROPERTY);
            }
        } catch (Throwable t) {
            log.error("Error processing a notify message: " + t.getMessage(), t);
            // TODO: Change to a SubscribeCreationFailedFaultType exception
            throw new Exception(t);
        }
    }

    private void nhinNotify(Element notify,   AssertionType assertion) throws Exception {
        log.debug("In nhinNotify");

        NotifyMarshaller marshaller = new NotifyMarshaller();
        Notify nhinNotify = marshaller.unmarshal(notify);

        performPolicyCheck(nhinNotify, assertion);

        // Perform patient check
        performPatientValidation(nhinNotify);

        // Read subscription from notify message
        HiemSubscriptionRepositoryService repositoryService = new HiemSubscriptionRepositoryService();

        List<NotificationMessageHolderType> notificationMessageList = nhinNotify.getNotificationMessage();
        if (notificationMessageList != null) {
            log.debug("Notifcation message count: " + notificationMessageList.size());
            for (NotificationMessageHolderType notificationMessage : notificationMessageList) {
                log.debug("Processing a single notification message");
                NotificationMessageMarshaller notificationMessageMarshaller = new NotificationMessageMarshaller();
                Element notificationMessageElement = notificationMessageMarshaller.marshal(notificationMessage);
                if (log.isDebugEnabled()) {
                    log.debug("Notification message: " + XmlUtility.serializeElementIgnoreFaults(notificationMessageElement));
                }
                List<HiemSubscriptionItem> subscriptionItems = repositoryService.RetrieveByNotificationMessage(notificationMessageElement, HiemProcessorConstants.PRODUCER_NHIN);
                if (subscriptionItems != null) {
                    log.debug("Subscription item list count: " + subscriptionItems.size());
                    for (HiemSubscriptionItem subscriptionItem : subscriptionItems) {
                        log.debug("extracting reference parameters from subscribe consumer reference");
                        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
                        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(subscriptionItem.getSubscribeXML());
                        log.debug("extracted reference parameters from subscribe consumer reference");


                        Notify adapterNotify = createAdapterNotify(notificationMessage, assertion, subscriptionItem);
                        Element adapterNotifyElement = marshaller.marshal(adapterNotify);
                        AdapterHiemNotifyProxyObjectFactory adapterFactory = new AdapterHiemNotifyProxyObjectFactory();
                        AdapterHiemNotifyProxy adapterProxy = adapterFactory.getAdapterHiemNotifyProxy();
                        AcknowledgementType responseElement = adapterProxy.notify(adapterNotifyElement, referenceParametersElements, assertion);
                        if (responseElement != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("Adapter notify response: " + responseElement);
                            }
                        } else {
                            log.debug("Adapter notify response was null");
                        }
                    }
                } else {
                    log.debug("Subscription item list was null");
                }
            }
        } else {
            log.info("Notification message list was null in notify message");
        }
    }

    private Notify createAdapterNotify(NotificationMessageHolderType notificationMessage, AssertionType assertion, HiemSubscriptionItem subscriptionItem) {
        // Create notify from inbound notification message
        Notify adapterNotify = new Notify();
        adapterNotify.getNotificationMessage().add(notificationMessage);

        return adapterNotify;
    }

    private void performPatientValidation(Notify notify) throws Exception {
        // TODO: Perform patient validation
        // if patient is unknown, throw ResourceUnknownFault
    }

    private void performPolicyCheck(Notify notify, AssertionType assertion) {
        // TODO: Call policy check
    }

    private void forwardToAgency(Element notify, ReferenceParametersElements referenceParametersElements, AssertionType assertion) {
        AdapterHiemNotifyProxyObjectFactory adapterFactory = new AdapterHiemNotifyProxyObjectFactory();
        AdapterHiemNotifyProxy adapterProxy = adapterFactory.getAdapterHiemNotifyProxy();

        try {
            AcknowledgementType resp = adapterProxy.notify(notify, referenceParametersElements, assertion);
        } catch (Exception ex) {
            log.error("adapterProxy.notify threw exception", ex);
        }
    }
}
