/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.connectmgr;

import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunityType;
import org.alembic.aurion.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class TestHelper {
    public static NhinTargetCommunityType createTargetCommunity (String hcid, String list, String region) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        
        if (NullChecker.isNotNullish(hcid)) {
            HomeCommunityType community = new HomeCommunityType();
            community.setHomeCommunityId(hcid);
            target.setHomeCommunity(community);
        }

        if (NullChecker.isNotNullish(region)) {
            target.setRegion(region);
        }

        if (NullChecker.isNotNullish(list)) {
            target.setList(list);
        }

        if (NullChecker.isNullish(hcid) && NullChecker.isNullish(region) && NullChecker.isNullish(list)) {
            target = null;
        }
        
        return target;
    }

    
}
