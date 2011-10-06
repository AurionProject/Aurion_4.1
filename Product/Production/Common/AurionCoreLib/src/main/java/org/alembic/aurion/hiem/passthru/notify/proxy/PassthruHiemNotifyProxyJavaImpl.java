/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.passthru.notify.PassthruHiemNotifyOrchImpl;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemNotifyProxyJavaImpl implements PassthruHiemNotifyProxy {

    public void notify(Notify notifyRequest, AssertionType assertion, NhinTargetSystemType target, ReferenceParametersElements referenceParametersElements, Element notifyElement) {
        PassthruHiemNotifyOrchImpl notifyOrchImpl = new PassthruHiemNotifyOrchImpl();
        notifyOrchImpl.notify(notifyRequest, assertion, target, referenceParametersElements, notifyElement);
    }

}