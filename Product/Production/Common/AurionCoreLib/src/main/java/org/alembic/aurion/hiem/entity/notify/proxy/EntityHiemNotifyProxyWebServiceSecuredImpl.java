/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.hiem.entity.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.subscriptionb2overrideforcdc.NotifyCdcBioPackageType;
import org.alembic.aurion.common.subscriptionb2overridefordocuments.NotifyDocumentType;
import org.alembic.aurion.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityHiemNotifyProxyWebServiceSecuredImpl implements EntityHiemNotifyProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:entitynotificationconsumersecured";
    private static final String SERVICE_LOCAL_PART = "EntityNotificationConsumerSecured";
    private static final String PORT_LOCAL_PART = "EntityNotificationConsumerSecuredPortSoap";
    private static final String WSDL_FILE = "EntityNotificationConsumerSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:org:alembic:aurion:entitynotificationconsumersecured:NotifyRequestSecured";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityHiemNotifyProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected EntityNotificationConsumerSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityNotificationConsumerSecuredPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityNotificationConsumerSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public AcknowledgementType notifySubscribersOfDocument(NotifyDocumentType notifySubscribersOfDocumentRequest, AssertionType assertion, NhinTargetCommunitiesType targets) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AcknowledgementType notifySubscribersOfCdcBioPackage(NotifyCdcBioPackageType notifySubscribersOfCdcBioPackageRequest, AssertionType assertion, NhinTargetCommunitiesType targets) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AcknowledgementType notify(Notify notifyRequest, AssertionType assertion, NhinTargetCommunitiesType targets, String rawNotifyXml) {
        log.debug("Begin EntityNotifyProxyWebserviceSecuredImpl.notify");
        AcknowledgementType ack = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
            EntityNotificationConsumerSecuredPortType port = getPort(url, NhincConstants.NOTIFY_ACTION, WS_ADDRESSING_ACTION, assertion);
            if (port != null)
            {
                ack = (AcknowledgementType) oProxyHelper.invokePort(port, EntityNotificationConsumerSecuredPortType.class, "notify", notifyRequest);
            }
            else
            {
                log.error("EntityNotificationConsumerSecuredPortType is null");
            }
        } catch (Exception ex) {
            log.error("Error calling notify: " + ex.getMessage(), ex);
        }
        log.debug("End EntityNotifyProxyWebserviceSecuredImpl.notify");
        return ack;
    }
}
