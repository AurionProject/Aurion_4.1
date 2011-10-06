/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.unsubscribe;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author JHOPPESC
 */
public class AdapterHiemUnsubscribeOrchImpl {
    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion)
    {
        UnsubscribeResponse response = new UnsubscribeResponse();
        return response;
    }

}
