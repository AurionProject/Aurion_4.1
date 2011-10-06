/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.common.connectionmanager.proxy;

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

/**
 *
 * @author Sai Valluripalli
 */
public class ConnectionManagerProxyNoOpImpl implements ConnectionManagerProxy {

    public AcknowledgementType storeAssigningAuthorityToHomeCommunityMapping(StoreAssigningAuthorityToHomeCommunityMappingRequestType storeAssigningAuthorityToHomeCommunityMappingRequest) {
        return new AcknowledgementType();
    }

    public HomeCommunitiesType getAllCommunities(GetAllCommunitiesRequestType getAllCommunitiesRequest) {
        return new HomeCommunitiesType();
    }

    public BusinessEntitiesType getAllBusinessEntities(GetAllBusinessEntitiesRequestType getAllBusinessEntitiesRequest) {
        return new BusinessEntitiesType();
    }

    public BusinessEntityType getBusinessEntity(GetBusinessEntityRequestType getBusinessEntityRequest) {
        return new BusinessEntityType();
    }

    public ConnectionInfosType getConnectionInfoSet(HomeCommunitiesType homeCommunities) {
        return new ConnectionInfosType();
    }

    public ConnectionInfoEndpointsType getConnectionInfoEndpointSet(GetConnectionInfoEndpointSetRequestType getConnectionInfoEndpointSetRequest) {
        return new ConnectionInfoEndpointsType();
    }

    public BusinessEntitiesType getBusinessEntitySet(GetBusinessEntitySetRequestType getBusinessEntitySetRequest) {
        return new BusinessEntitiesType();
    }

    public ConnectionInfosType getConnectionInfoSetByServiceName(HomeCommunitiesWithServiceNameType getConnectionInfoSetByServiceNameRequest) {
        return new ConnectionInfosType();
    }

    public ConnectionInfoEndpointsType getConnectionInfoEndpointSetByServiceName(GetConnectionInfoEndpointSetByServiceNameRequestType getConnectionInfoEndpointSetByServiceNameRequest) {
        return new ConnectionInfoEndpointsType();
    }

    public BusinessEntitiesType getBusinessEntitySetByServiceName(GetBusinessEntitySetByServiceNameRequestType getBusinessEntitySetByServiceNameRequest) {
        return new BusinessEntitiesType();
    }

    public ConnectionInfoType getConnectionInfoByServiceName(HomeCommunityWithServiceNameType homeCommunityWithServiceName) {
        return new ConnectionInfoType();
    }

    public ConnectionInfoEndpointType getConnectionInfoEndpointByServiceName(GetConnectionInfoEndpointByServiceNameRequestType getConnectionInfoEndpointByServiceNameRequest) {
        return new ConnectionInfoEndpointType();
    }

    public BusinessEntityType getBusinessEntityByServiceName(GetBusinessEntityByServiceNameRequestType getBusinessEntityByServiceNameRequest) {
        return new BusinessEntityType();
    }

    public ConnectionInfosType getAllConnectionInfoSetByServiceName(GetAllConnectionInfoSetByServiceNameRequestType getAllConnectionInfoSetByServiceNameRequest) {
        return new ConnectionInfosType();
    }

    public ConnectionInfoEndpointsType getAllConnectionInfoEndpointSetByServiceName(GetAllConnectionInfoEndpointSetByServiceNameRequestType getAllConnectionInfoEndpointSetByServiceNameRequest) {
        return new ConnectionInfoEndpointsType();
    }

    public BusinessEntitiesType getAllBusinessEntitySetByServiceName(GetAllBusinessEntitySetByServiceNameRequestType getAllBusinessEntitySetByServiceNameRequest) {
        return new BusinessEntitiesType();
    }

    public SuccessOrFailType forceRefreshUDDICache(ForceRefreshUDDICacheRequestType forceRefreshUDDICacheRequest) {
        return new SuccessOrFailType();
    }

    public SuccessOrFailType forceRefreshInternalConnectCache(ForceRefreshInternalConnectCacheRequestType forceRefreshInternalConnectCacheRequest) {
        return new SuccessOrFailType();
    }

    public GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(GetAssigningAuthoritiesByHomeCommunityRequestType getAssigningAuthoritiesByHomeCommunityRequest) {
        return new GetAssigningAuthoritiesByHomeCommunityResponseType();
    }

    public GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthority(GetHomeCommunityByAssigningAuthorityRequestType getHomeCommunityByAssigningAuthorityRequest) {
        return new GetHomeCommunityByAssigningAuthorityResponseType();
    }

    public EPRType getConnectionInfoEndpontFromNhinTarget(GetConnectionInfoEndpontFromNhinTargetType getConnectionInfoEndpontFromNhinTargetRequest) {
        return new EPRType();
    }

    public UrlSetType getUrlSetFromNhinTargetCommunities(GetUrlSetByServiceNameType getConnectionInfoEndpontFromNhinTargetRequest) {
        return new UrlSetType();
    }
}
