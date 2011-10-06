/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.connectmgr;

import org.alembic.aurion.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class ConnectionManagerCommunityMapping {

    /**
     *
     * @param requestType
     * @return GetHomeCommunityByAssigningAuthorityResponseType
     */
    public static HomeCommunityType getHomeCommunityByAssigningAuthority(String assigningAuthId) {
        HomeCommunityType hc = new HomeCommunityType();

        // Verify assigning authority id is valid
        if (NullChecker.isNotNullish(assigningAuthId)) {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
            hc.setHomeCommunityId(mappingDao.getHomeCommunityId(assigningAuthId));
        }
        else {
            hc = null;
        }

        return hc;
    }
}
