/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.passthru.subscribe.PassthruHiemSubscribeOrchImpl;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemSubscribeProxyJavaImpl implements PassthruHiemSubscribeProxy {

    public SubscribeResponse subscribe(Subscribe subscribeRequest, AssertionType assertion, NhinTargetSystemType target, Element subscribeElement) throws NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, TopicNotSupportedFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, InvalidMessageContentExpressionFault, ResourceUnknownFault {
        return new PassthruHiemSubscribeOrchImpl().subscribe(subscribeRequest, assertion, target, subscribeElement);
    }

}
