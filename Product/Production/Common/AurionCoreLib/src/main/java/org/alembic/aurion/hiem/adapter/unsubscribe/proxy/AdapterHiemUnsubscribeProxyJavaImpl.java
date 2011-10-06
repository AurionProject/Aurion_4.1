/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.unsubscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.adapter.unsubscribe.AdapterHiemUnsubscribeOrchImpl;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.alembic.aurion.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class AdapterHiemUnsubscribeProxyJavaImpl implements AdapterHiemUnsubscribeProxy {

    public UnsubscribeResponse unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion) {
        AdapterHiemUnsubscribeOrchImpl unsubscribeOrchImpl = new AdapterHiemUnsubscribeOrchImpl();
        WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Unsubscribe unsubscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

        return unsubscribeOrchImpl.unsubscribe(unsubscribe, referenceParametersElements, assertion);
    }

}
