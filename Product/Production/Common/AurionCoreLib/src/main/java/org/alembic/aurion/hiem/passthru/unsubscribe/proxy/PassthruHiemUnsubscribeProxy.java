/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.unsubscribe.proxy;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;


public interface PassthruHiemUnsubscribeProxy {
    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements) throws UnableToDestroySubscriptionFault, ResourceUnknownFault;

}
