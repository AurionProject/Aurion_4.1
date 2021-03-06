/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.common.connectionmanager.proxy;

/**
 *
 * @author Sai Valluripalli
 */
public interface ConnectionManagerProxy
{
    public org.alembic.aurion.common.nhinccommon.AcknowledgementType storeAssigningAuthorityToHomeCommunityMapping(org.alembic.aurion.common.connectionmanagerinfo.StoreAssigningAuthorityToHomeCommunityMappingRequestType storeAssigningAuthorityToHomeCommunityMappingRequest);
    public org.alembic.aurion.common.nhinccommon.HomeCommunitiesType getAllCommunities(org.alembic.aurion.common.connectionmanagerinfo.GetAllCommunitiesRequestType getAllCommunitiesRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getAllBusinessEntities(org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitiesRequestType getAllBusinessEntitiesRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntityType getBusinessEntity(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityRequestType getBusinessEntityRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getConnectionInfoSet(org.alembic.aurion.common.nhinccommon.HomeCommunitiesType homeCommunities);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getConnectionInfoEndpointSet(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetRequestType getConnectionInfoEndpointSetRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getBusinessEntitySet(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetRequestType getBusinessEntitySetRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getConnectionInfoSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.HomeCommunitiesWithServiceNameType getConnectionInfoSetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getConnectionInfoEndpointSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointSetByServiceNameRequestType getConnectionInfoEndpointSetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getBusinessEntitySetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType getBusinessEntitySetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoType getConnectionInfoByServiceName(org.alembic.aurion.common.connectionmanagerinfo.HomeCommunityWithServiceNameType homeCommunityWithServiceName);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointType getConnectionInfoEndpointByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpointByServiceNameRequestType getConnectionInfoEndpointByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntityType getBusinessEntityByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType getBusinessEntityByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfosType getAllConnectionInfoSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoSetByServiceNameRequestType getAllConnectionInfoSetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.ConnectionInfoEndpointsType getAllConnectionInfoEndpointSetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllConnectionInfoEndpointSetByServiceNameRequestType getAllConnectionInfoEndpointSetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.BusinessEntitiesType getAllBusinessEntitySetByServiceName(org.alembic.aurion.common.connectionmanagerinfo.GetAllBusinessEntitySetByServiceNameRequestType getAllBusinessEntitySetByServiceNameRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.SuccessOrFailType forceRefreshUDDICache(org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshUDDICacheRequestType forceRefreshUDDICacheRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.SuccessOrFailType forceRefreshInternalConnectCache(org.alembic.aurion.common.connectionmanagerinfo.ForceRefreshInternalConnectCacheRequestType forceRefreshInternalConnectCacheRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType getAssigningAuthoritiesByHomeCommunity(org.alembic.aurion.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityRequestType getAssigningAuthoritiesByHomeCommunityRequest);
    public org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityResponseType getHomeCommunityByAssigningAuthority(org.alembic.aurion.common.connectionmanagerinfo.GetHomeCommunityByAssigningAuthorityRequestType getHomeCommunityByAssigningAuthorityRequest);
    public org.alembic.aurion.common.nhinccommon.EPRType getConnectionInfoEndpontFromNhinTarget(org.alembic.aurion.common.connectionmanagerinfo.GetConnectionInfoEndpontFromNhinTargetType getConnectionInfoEndpontFromNhinTargetRequest);
    public org.alembic.aurion.common.nhinccommon.UrlSetType getUrlSetFromNhinTargetCommunities(org.alembic.aurion.common.connectionmanagerinfo.GetUrlSetByServiceNameType getConnectionInfoEndpontFromNhinTargetRequest);
}
