/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */

package org.alembic.aurion.common.connectionmanager.proxy;

import org.alembic.aurion.common.connectionmanager.CMServiceHelper;
import org.alembic.aurion.common.connectionmanager.nhinc.ConnectionManagerOrchImpl;
import org.alembic.aurion.common.nhinccommon.AssertionType;

/**
 *
 * @author Sai Valluripalli
 */
public class ConnectionManagerProxyJavaImpl implements ConnectionManagerProxy
{

public org.alembic.aurion.common.nhinccommon.AcknowledgementType storeAssigningAuthorityToHomeCommunityMapping(org.alembic.aurion.common.connectionmanagerinfo.StoreAssigningAuthorityToHomeCommunityMappingRequestType storeAssigningAuthorityToHomeCommunityMappingRequest) {
        return new ConnectionManagerOrchImpl().storeAssigningAuthorityToHomeCommunityMapping(storeAssigningAuthorityToHomeCommunityMappingRequest);
    }

    public org.alembic.aurion.common.nhinccommon.HomeCommunitiesType getAllCommunities(org.alembic.aurion.common.connectionmanagerinfo.GetAllCommunitiesRequestType getAllCommunitiesRequest) {
        return new ConnectionManagerOrchImpl().getAllCommunities(getAllCommunitiesRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getAllBusinessEntities(org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitiesRequestType getAllBusinessEntitiesRequest) {
        return new ConnectionManagerOrchImpl().getAllBusinessEntities(getAllBusinessEntitiesRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntityType getBusinessEntity(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityRequestType getBusinessEntityRequest) {
        return new ConnectionManagerOrchImpl().getBusinessEntity(getBusinessEntityRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getConnectionInfoSet(org.alembic.aurion.common.nhinccommon.HomeCommunitiesType homeCommunities) {
        return new ConnectionManagerOrchImpl().getConnectionInfoSet(homeCommunities);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getConnectionInfoEndpointSet(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetRequestType getConnectionInfoEndpointSetRequest) {
        return new ConnectionManagerOrchImpl().getConnectionInfoEndpointSet(getConnectionInfoEndpointSetRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getBusinessEntitySet(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetRequestType getBusinessEntitySetRequest) {
        return new ConnectionManagerOrchImpl().getBusinessEntitySet(getBusinessEntitySetRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getConnectionInfoSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.HomeCommunitiesWithServiceNameType getConnectionInfoSetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getConnectionInfoSetByServiceName(getConnectionInfoSetByServiceNameRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getConnectionInfoEndpointSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetByServiceNameRequestType getConnectionInfoEndpointSetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getConnectionInfoEndpointSetByServiceName(getConnectionInfoEndpointSetByServiceNameRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getBusinessEntitySetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType getBusinessEntitySetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getBusinessEntitySetByServiceName(getBusinessEntitySetByServiceNameRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoType getConnectionInfoByServiceName(org.alembic.aurion.common.connectionmanagerinfo.HomeCommunityWithServiceNameType homeCommunityWithServiceName) {
        return new ConnectionManagerOrchImpl().getConnectionInfoByServiceName(homeCommunityWithServiceName);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointType getConnectionInfoEndpointByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointByServiceNameRequestType getConnectionInfoEndpointByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getConnectionInfoEndpointByServiceName(getConnectionInfoEndpointByServiceNameRequest);
    }

    /**
     * This method retrieves the business entity and Connection Information for a specific service
     * at a specific home community.
     *
     * @param part1 This contains the home community identification and the name of the service that the
     *              connection info is desired.
     * @return The connection information for the service at the specified home community.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntityType getBusinessEntityByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType getBusinessEntityByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getBusinessEntityByServiceName(getBusinessEntityByServiceNameRequest);
    }

    /**
     * This method returns the connection information for all known home communities that support the specified
     * service.
     *
     * @param part1 The name of the service that is desired.
     * @return The connection information for each known home community that supports the specified service.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getAllConnectionInfoSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoSetByServiceNameRequestType getAllConnectionInfoSetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getAllConnectionInfoSetByServiceName(getAllConnectionInfoSetByServiceNameRequest);
    }

    /**
     * This method returns the endpoint connection information for all known home communities that
     * support the specified service.
     *
     * @param part1 The name of the service that is desired.
     * @return The endpoint connection information for each known home community that
     *         supports the specified service.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getAllConnectionInfoEndpointSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoEndpointSetByServiceNameRequestType getAllConnectionInfoEndpointSetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getAllConnectionInfoEndpointSetByServiceName(getAllConnectionInfoEndpointSetByServiceNameRequest);
    }

    /**
     * This method returns the business entity and service connection information for all known
     * home communities that support the specified service.
     *
     * @param part1 The name of the service that is desired.
     * @return The business entity and service connection information for each known
     *         home community that supports the specified service.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getAllBusinessEntitySetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitySetByServiceNameRequestType getAllBusinessEntitySetByServiceNameRequest) {
        return new ConnectionManagerOrchImpl().getAllBusinessEntitySetByServiceName(getAllBusinessEntitySetByServiceNameRequest);
    }

    /**
     * This method causes the UDDI service information to be refreshed.
     *
     * @param part1 The only purpose for this parameter is so that the
     *              web service has a unique document that identifies this
     *              operation.  The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.SuccessOrFailType forceRefreshUDDICache(org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshUDDICacheRequestType forceRefreshUDDICacheRequest) {
        return new ConnectionManagerOrchImpl().forceRefreshUDDICache(forceRefreshUDDICacheRequest);
    }

    /**
     * This method causes the Internal Connection service information to be refreshed.
     *
     * @param part1 The only purpose for this parameter is so that the
     *              web service has a unique document that identifies this
     *              operation.  The values themselves are not used.
     * @return Whether this succeeded or failed.
     */
    public org.alembic.aurion.common.connectionmanagerinfo.SuccessOrFailType forceRefreshInternalConnectCache(org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshInternalConnectCacheRequestType forceRefreshInternalConnectCacheRequest) {
        return new ConnectionManagerOrchImpl().forceRefreshInternalConnectCache(forceRefreshInternalConnectCacheRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityRequestType getAssigningAuthoritiesByHomeCommunityRequest) {
        return new ConnectionManagerOrchImpl().getAssigningAuthoritiesByHomeCommunity(getAssigningAuthoritiesByHomeCommunityRequest);
    }

    public org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthority(org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityRequestType getHomeCommunityByAssigningAuthorityRequest) {
        return new ConnectionManagerOrchImpl().getHomeCommunityByAssigningAuthority(getHomeCommunityByAssigningAuthorityRequest);
    }

    public org.alembic.aurion.common.nhinccommon.EPRType getConnectionInfoEndpontFromNhinTarget(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpontFromNhinTargetType getConnectionInfoEndpontFromNhinTargetRequest) {
        return new ConnectionManagerOrchImpl().getConnectionInfoEndpontFromNhinTarget(getConnectionInfoEndpontFromNhinTargetRequest);
    }

    public org.alembic.aurion.common.nhinccommon.UrlSetType getUrlSetFromNhinTargetCommunities(org.alembic.aurion.common.connectionmanagerinfo.GetUrlSetByServiceNameType getConnectionInfoEndpontFromNhinTargetRequest) {
        return new ConnectionManagerOrchImpl().getUrlSetFromNhinTargetCommunities(getConnectionInfoEndpontFromNhinTargetRequest);
    }
}
