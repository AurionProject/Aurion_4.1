/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.common.connectionmanager.proxy;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType;
import org.alembic.aurion.common.connectionmanagerinfo.BusinessEntityType;
import org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointType;
import org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType;
import org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoType;
import org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType;
import org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshInternalConnectCacheRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshUDDICacheRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitiesRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitySetByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAllCommunitiesRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoEndpointSetByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoSetByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType;
import org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetByServiceNameRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpontFromNhinTargetType;
import org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityResponseType;
import org.alembic.aurion.common.connectionmanagerinfo.GetUrlSetByServiceNameType;
import org.alembic.aurion.common.connectionmanagerinfo.HomeCommunitiesWithServiceNameType;
import org.alembic.aurion.common.connectionmanagerinfo.HomeCommunityWithServiceNameType;
import org.alembic.aurion.common.connectionmanagerinfo.StoreAssigningAuthorityToHomeCommunityMappingRequestType;
import org.alembic.aurion.common.connectionmanagerinfo.SuccessOrFailType;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.EPRType;
import org.alembic.aurion.common.nhinccommon.HomeCommunitiesType;
import org.alembic.aurion.common.nhinccommon.UrlSetType;
import org.alembic.aurion.nhinccomponentconnectionmanager.NhincComponentConnectionManagerPortType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class ConnectionManagerProxyWebServiceUnsecuredImpl implements ConnectionManagerProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:org:alembic:aurion:nhinccomponentconnectionmanager";
    private static final String SERVICE_LOCAL_PART = "NhincComponentConnectionManager";
    private static final String PORT_LOCAL_PART = "NhincComponentConnectionManagerPortSoap";
    private static final String WSDL_FILE = "NhincComponentConnectionManager.wsdl";
    private final String WS_ADDRESSING_ACTION_STORE_AA_HC_MAPPING = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:storeAssigningAuthorityToHomeCommunityMapping";
    private final String WS_ADDRESSING_ACTION_GET_ALL_COMMUNITIES = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAllCommunities";
    private final String WS_ADDRESSING_ACTION_GET_ALL_BUSINESS_ENTITIES = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAllBusinessEntities";
    private final String WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITIES = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getBusinessEntity";
    private final String WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_SET = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoSet";
    private final String WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_ENDPOINT_SET = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoEndpointSet";
    private final String WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SET = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getBusinessEntitySet";
    private final String WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_SET_BY_SRVC_NAME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoSetByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_ENDPOINT_SET_BY_SRVC_NAME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoEndpointSetByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SET_BY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getBusinessEntitySetByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_BY_SVRC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_CONN_INFO_ENDPOINT_BY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoEndpointByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getBusinessEntityByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_ALL_CONN_INFO_SET_BY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAllConnectionInfoSetByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_ALL_CONN_INFO_ENDPOINT_SET_BY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAllConnectionInfoEndpointSetByServiceName";
    private final String WS_ADDRESSING_ACTION_GET_ALL_BUSS_ENTITY_SET_BY_SRVC_NME = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAllBusinessEntitySetByServiceName";
    private final String WS_ADDRESSING_ACTION_FORCE_REFRESH_UDDI_CACHE = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:forceRefreshUDDICache";
    private final String WS_ADDRESSING_ACTION_FORCE_REFRESH_INTERNAL_CONNECT_CACHE = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:forceRefreshInternalConnectCache";
    private final String WS_ADDRESSING_ACTION_GET_AA_BY_HC = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getAssigningAuthoritiesByHomeCommunity";
    private final String WS_ADDRESSING_ACTION_GET_HC_BY_AA = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getHomeCommunityByAssigningAuthority";
    private final String WS_ADDRESSING_ACTION_GET_CONN_INFO_ENDPOINT_FRM_NHINTARGET = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getConnectionInfoEndpontFromNhinTarget";
    private final String WS_ADDRESSING_ACTION_GET_URL_SET_FRM_NHIN_TARGET_COMMUNITIES = "urn:org:alembic:aurion:nhinccomponentconnectionmanager:getUrlSetFromNhinTargetCommunities";
    private WebServiceProxyHelper oProxyHelper = null;
    private Log log = null;

    /**
     * default constructor
     */
    public ConnectionManagerProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     * 
     * @return Log
     */
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * 
     * @return WebServiceProxyHelper
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected NhincComponentConnectionManagerPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        NhincComponentConnectionManagerPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincComponentConnectionManagerPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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

    /**
     * 
     * @param storeAssigningAuthorityToHomeCommunityMappingRequest
     * @param null
     * @return AcknowledgementType
     */
    public AcknowledgementType storeAssigningAuthorityToHomeCommunityMapping(StoreAssigningAuthorityToHomeCommunityMappingRequestType storeAssigningAuthorityToHomeCommunityMappingRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->storeAssigningAuthorityToHomeCommunityMapping() Begin");
        AcknowledgementType ack = null;
        
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_STORE_AA_HC_MAPPING, null);
                ack = (AcknowledgementType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "storeAssigningAuthorityToHomeCommunityMapping", storeAssigningAuthorityToHomeCommunityMappingRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->storeAssigningAuthorityToHomeCommunityMapping() End");
        return ack;
    }

    /**
     * 
     * @param getAllCommunitiesRequest
     * @param null
     * @return HomeCommunitiesType
     */
    public HomeCommunitiesType getAllCommunities(GetAllCommunitiesRequestType getAllCommunitiesRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllCommunities() Begin");
        HomeCommunitiesType homeCommunitiesType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_ALL_COMMUNITIES, null);
                homeCommunitiesType = (HomeCommunitiesType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAllCommunities", getAllCommunitiesRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllCommunities() End");
        return homeCommunitiesType;
    }

    /**
     * 
     * @param getAllBusinessEntitiesRequest
     * @param null
     * @return BusinessEntitiesType
     */
    public BusinessEntitiesType getAllBusinessEntities(GetAllBusinessEntitiesRequestType getAllBusinessEntitiesRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllBusinessEntities() Begin");
        BusinessEntitiesType businessEntitiesType = null;
        try {
            String url = createWebServiceProxyHelper().getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_ALL_BUSINESS_ENTITIES, null);
                businessEntitiesType = (BusinessEntitiesType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAllBusinessEntities", getAllBusinessEntitiesRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllBusinessEntities() End");
        return businessEntitiesType;
    }

    /**
     * 
     * @param getBusinessEntityRequest
     * @param null
     * @return BusinessEntityType
     */
    public BusinessEntityType getBusinessEntity(GetBusinessEntityRequestType getBusinessEntityRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntity() Begin");
        BusinessEntityType businessEntityType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITIES, null);
                businessEntityType = (BusinessEntityType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getBusinessEntity", getBusinessEntityRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntity() End");
        return businessEntityType;
    }

    /**
     * 
     * @param homeCommunities
     * @param null
     * @return ConnectionInfosType
     */
    public ConnectionInfosType getConnectionInfoSet(HomeCommunitiesType homeCommunities) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoSet() Begin");
        ConnectionInfosType connectionInfosType = null;
        
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);

            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_SET, null);
                connectionInfosType = (ConnectionInfosType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoSet", homeCommunities);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoSet() End");
        return connectionInfosType;
    }

    /**
     * 
     * @param getConnectionInfoEndpointSetRequest
     * @param null
     * @return ConnectionInfoEndpointsType
     */
    public ConnectionInfoEndpointsType getConnectionInfoEndpointSet(GetConnectionInfoEndpointSetRequestType getConnectionInfoEndpointSetRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointSet() Begin");
        ConnectionInfoEndpointsType connectionInfoEndpointsType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_ENDPOINT_SET, null);
                connectionInfoEndpointsType = (ConnectionInfoEndpointsType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoEndpointSet", getConnectionInfoEndpointSetRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointSet() End");
        return connectionInfoEndpointsType;
    }

    /**
     * 
     * @param getBusinessEntitySetRequest
     * @param null
     * @return BusinessEntitiesType
     */
    public BusinessEntitiesType getBusinessEntitySet(GetBusinessEntitySetRequestType getBusinessEntitySetRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntitySet() Begin");
        BusinessEntitiesType businessEntitiesType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SET, null);
                businessEntitiesType = (BusinessEntitiesType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getBusinessEntitySet", getBusinessEntitySetRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntitySet() End");
        return businessEntitiesType;
    }

    /**
     * 
     * @param getConnectionInfoSetByServiceNameRequest
     * @param null
     * @return ConnectionInfosType
     */
    public ConnectionInfosType getConnectionInfoSetByServiceName(HomeCommunitiesWithServiceNameType getConnectionInfoSetByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoSetByServiceName() Begin");
        ConnectionInfosType connectionInfosType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_SET_BY_SRVC_NAME, null);
                connectionInfosType = (ConnectionInfosType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoSetByServiceName", getConnectionInfoSetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoSetByServiceName() End");
        return connectionInfosType;
    }

    /**
     * 
     * @param getConnectionInfoEndpointSetByServiceNameRequest
     * @param null
     * @return ConnectionInfoEndpointsType
     */
    public ConnectionInfoEndpointsType getConnectionInfoEndpointSetByServiceName(GetConnectionInfoEndpointSetByServiceNameRequestType getConnectionInfoEndpointSetByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointSetByServiceName() Begin");
        ConnectionInfoEndpointsType connectionInfoEndpointsType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_ENDPOINT_SET_BY_SRVC_NAME, null);
                connectionInfoEndpointsType = (ConnectionInfoEndpointsType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoEndpointSetByServiceName", getConnectionInfoEndpointSetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointSetByServiceName() End");
        return connectionInfoEndpointsType;
    }

    /**
     * 
     * @param getBusinessEntitySetByServiceNameRequest
     * @param null
     * @return BusinessEntitiesType
     */
    public BusinessEntitiesType getBusinessEntitySetByServiceName(GetBusinessEntitySetByServiceNameRequestType getBusinessEntitySetByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntitySetByServiceName() Begin");
        BusinessEntitiesType businessEntitiesType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SET_BY_SRVC_NME, null);
                businessEntitiesType = (BusinessEntitiesType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getBusinessEntitySetByServiceName", getBusinessEntitySetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntitySetByServiceName() End");
        return businessEntitiesType;
    }

    /**
     * 
     * @param homeCommunityWithServiceName
     * @param null
     * @return ConnectionInfoType
     */
    public ConnectionInfoType getConnectionInfoByServiceName(HomeCommunityWithServiceNameType homeCommunityWithServiceName) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoByServiceName() Begin");
        ConnectionInfoType connectionInfoType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONNECTION_INFO_BY_SVRC_NME, null);
                connectionInfoType = (ConnectionInfoType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoByServiceName", homeCommunityWithServiceName);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoByServiceName() End");
        return connectionInfoType;
    }

    /**
     * 
     * @param getConnectionInfoEndpointByServiceNameRequest
     * @param null
     * @return ConnectionInfoEndpointType
     */
    public ConnectionInfoEndpointType getConnectionInfoEndpointByServiceName(GetConnectionInfoEndpointByServiceNameRequestType getConnectionInfoEndpointByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointByServiceName() Begin");
        ConnectionInfoEndpointType connectionInfoEndpointType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONN_INFO_ENDPOINT_BY_SRVC_NME, null);
                connectionInfoEndpointType = (ConnectionInfoEndpointType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoEndpointByServiceName", getConnectionInfoEndpointByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpointByServiceName() End");
        return connectionInfoEndpointType;
    }

    /**
     * 
     * @param getBusinessEntityByServiceNameRequest
     * @param null
     * @return BusinessEntityType
     */
    public BusinessEntityType getBusinessEntityByServiceName(GetBusinessEntityByServiceNameRequestType getBusinessEntityByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntityByServiceName() Begin");
        BusinessEntityType businessEntityType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_BUSINESS_ENTITY_SRVC_NME, null);
                businessEntityType = (BusinessEntityType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getBusinessEntityByServiceName", getBusinessEntityByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getBusinessEntityByServiceName() End");
        return businessEntityType;
    }

    /**
     * 
     * @param getAllConnectionInfoSetByServiceNameRequest
     * @param null
     * @return ConnectionInfosType
     */
    public ConnectionInfosType getAllConnectionInfoSetByServiceName(GetAllConnectionInfoSetByServiceNameRequestType getAllConnectionInfoSetByServiceNameRequest) {
                log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllConnectionInfoSetByServiceName() Begin");
        ConnectionInfosType connectionInfosType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_ALL_CONN_INFO_SET_BY_SRVC_NME, null);
                connectionInfosType = (ConnectionInfosType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAllConnectionInfoSetByServiceName", getAllConnectionInfoSetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllConnectionInfoSetByServiceName() End");
        return connectionInfosType;
    }

    /**
     * 
     * @param getAllConnectionInfoEndpointSetByServiceNameRequest
     * @param null
     * @return ConnectionInfoEndpointsType
     */
    public ConnectionInfoEndpointsType getAllConnectionInfoEndpointSetByServiceName(GetAllConnectionInfoEndpointSetByServiceNameRequestType getAllConnectionInfoEndpointSetByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllConnectionInfoEndpointSetByServiceName() Begin");
        ConnectionInfoEndpointsType connectionInfoEndpointsType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_ALL_CONN_INFO_ENDPOINT_SET_BY_SRVC_NME, null);
                connectionInfoEndpointsType = (ConnectionInfoEndpointsType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAllConnectionInfoEndpointSetByServiceName", getAllConnectionInfoEndpointSetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllConnectionInfoEndpointSetByServiceName() End");
        return connectionInfoEndpointsType;
    }

    /**
     * 
     * @param getAllBusinessEntitySetByServiceNameRequest
     * @param null
     * @return BusinessEntitiesType
     */
    public BusinessEntitiesType getAllBusinessEntitySetByServiceName(GetAllBusinessEntitySetByServiceNameRequestType getAllBusinessEntitySetByServiceNameRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllBusinessEntitySetByServiceName() Begin");
        BusinessEntitiesType businessEntitiesType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_ALL_BUSS_ENTITY_SET_BY_SRVC_NME, null);
                businessEntitiesType = (BusinessEntitiesType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAllBusinessEntitySetByServiceName", getAllBusinessEntitySetByServiceNameRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAllBusinessEntitySetByServiceName() End");
        return businessEntitiesType;
    }

    /**
     * 
     * @param forceRefreshUDDICacheRequest
     * @param null
     * @return SuccessOrFailType
     */
    public SuccessOrFailType forceRefreshUDDICache(ForceRefreshUDDICacheRequestType forceRefreshUDDICacheRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->forceRefreshUDDICache() Begin");
        SuccessOrFailType successOrFailType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_FORCE_REFRESH_UDDI_CACHE, null);
                successOrFailType = (SuccessOrFailType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "forceRefreshUDDICache", forceRefreshUDDICacheRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->forceRefreshUDDICache() End");
        return successOrFailType;
    }

    /**
     * 
     * @param forceRefreshInternalConnectCacheRequest
     * @param null
     * @return SuccessOrFailType
     */
    public SuccessOrFailType forceRefreshInternalConnectCache(ForceRefreshInternalConnectCacheRequestType forceRefreshInternalConnectCacheRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->forceRefreshInternalConnectCache() Begin");
        SuccessOrFailType successOrFailType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_FORCE_REFRESH_INTERNAL_CONNECT_CACHE, null);
                successOrFailType = (SuccessOrFailType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "forceRefreshInternalConnectCache", forceRefreshInternalConnectCacheRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->forceRefreshInternalConnectCache() End");
        return successOrFailType;
    }

    /**
     * 
     * @param getAssigningAuthoritiesByHomeCommunityRequest
     * @param null
     * @return GetAssigningAuthoritiesByHomeCommunityResponseType
     */
    public GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(GetAssigningAuthoritiesByHomeCommunityRequestType getAssigningAuthoritiesByHomeCommunityRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAssigningAuthoritiesByHomeCommunity() Begin");
        GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunityResponseType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_AA_BY_HC, null);
                getAssigningAuthoritiesByHomeCommunityResponseType = (GetAssigningAuthoritiesByHomeCommunityResponseType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getAssigningAuthoritiesByHomeCommunity", getAssigningAuthoritiesByHomeCommunityRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getAssigningAuthoritiesByHomeCommunity() End");
        return getAssigningAuthoritiesByHomeCommunityResponseType;
    }

    /**
     * 
     * @param getHomeCommunityByAssigningAuthorityRequest
     * @param null
     * @return GetHomeCommunityByAssigningAuthorityResponseType
     */
    public GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthority(GetHomeCommunityByAssigningAuthorityRequestType getHomeCommunityByAssigningAuthorityRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getHomeCommunityByAssigningAuthority() Begin");
        GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthorityResponseType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_HC_BY_AA, null);
                getHomeCommunityByAssigningAuthorityResponseType = (GetHomeCommunityByAssigningAuthorityResponseType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getHomeCommunityByAssigningAuthority", getHomeCommunityByAssigningAuthorityRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getHomeCommunityByAssigningAuthority() End");
        return getHomeCommunityByAssigningAuthorityResponseType;
    }

    /**
     *
     * @param getConnectionInfoEndpontFromNhinTargetRequest
     * @param null
     * @return EPRType
     */
    public EPRType getConnectionInfoEndpontFromNhinTarget(GetConnectionInfoEndpontFromNhinTargetType getConnectionInfoEndpontFromNhinTargetRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpontFromNhinTarget() Begin");
        EPRType eprType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_CONN_INFO_ENDPOINT_FRM_NHINTARGET, null);
                eprType = (EPRType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getConnectionInfoEndpontFromNhinTarget", getConnectionInfoEndpontFromNhinTargetRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getConnectionInfoEndpontFromNhinTarget() End");
        return eprType;
    }

    /**
     * 
     * @param getConnectionInfoEndpontFromNhinTargetRequest
     * @param null
     * @return UrlSetType
     */
    public UrlSetType getUrlSetFromNhinTargetCommunities(GetUrlSetByServiceNameType getConnectionInfoEndpontFromNhinTargetRequest) {
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getUrlSetFromNhinTargetCommunities() Begin");
        UrlSetType urlSetType = null;
        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.CONNECTION_MANAGER_SERVICE);
            if (NullChecker.isNotNullish(url)) {
                NhincComponentConnectionManagerPortType port = getPort(url, WS_ADDRESSING_ACTION_GET_URL_SET_FRM_NHIN_TARGET_COMMUNITIES, null);
                urlSetType = (UrlSetType) oProxyHelper.invokePort(port, NhincComponentConnectionManagerPortType.class, "getUrlSetFromNhinTargetCommunities", getConnectionInfoEndpontFromNhinTargetRequest);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        log.debug("ConnectionManagerProxyWebServiceUnsecuredImpl->getUrlSetFromNhinTargetCommunities() End");
        return urlSetType;
    }
}
