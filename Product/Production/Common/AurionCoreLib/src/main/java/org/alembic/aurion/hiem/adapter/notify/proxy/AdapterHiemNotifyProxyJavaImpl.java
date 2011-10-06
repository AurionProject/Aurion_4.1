/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.adapter.notify.AdapterHiemNotifyOrchImpl;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class AdapterHiemNotifyProxyJavaImpl implements AdapterHiemNotifyProxy {

    public AcknowledgementType notify(Element notify, ReferenceParametersElements referenceParametersElements, AssertionType assertion) throws Exception {
        AdapterHiemNotifyOrchImpl notifyOrchImpl = new AdapterHiemNotifyOrchImpl();
        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Notify notifyMsg = subscribeMarshaller.unmarshalNotifyRequest(notify);

        return notifyOrchImpl.notify(notifyMsg, referenceParametersElements, assertion);
    }

    public AcknowledgementType notifySubscribersOfDocument(Element docNotify, AssertionType assertion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AcknowledgementType notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
