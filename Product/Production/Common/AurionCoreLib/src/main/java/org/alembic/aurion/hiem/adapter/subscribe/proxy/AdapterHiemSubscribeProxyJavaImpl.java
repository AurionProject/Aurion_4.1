/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.subscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.adapter.subscribe.AdapterHiemSubscribeOrchImpl;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;
import org.alembic.aurion.hiem.dte.marshallers.WsntSubscribeMarshaller;
import org.oasis_open.docs.wsn.b_2.Subscribe;

/**
 *
 * @author JHOPPESC
 */
public class AdapterHiemSubscribeProxyJavaImpl implements AdapterHiemSubscribeProxy {

    public SubscribeResponse subscribe(Element subscribe, AssertionType assertion) throws Exception {
        AdapterHiemSubscribeOrchImpl subscribeOrchImpl = new AdapterHiemSubscribeOrchImpl();
        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Subscribe subscribeMsg = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribe);

        return subscribeOrchImpl.subscribe(subscribeMsg, assertion);
    }

}
