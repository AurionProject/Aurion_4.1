/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.notify;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author JHOPPESC
 */
public class AdapterHiemNotifyOrchImpl {
    public AcknowledgementType notify(Notify notify, ReferenceParametersElements referenceParametersElements,AssertionType assertion) throws Exception {
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("Success");
        return ack;
    }

}
