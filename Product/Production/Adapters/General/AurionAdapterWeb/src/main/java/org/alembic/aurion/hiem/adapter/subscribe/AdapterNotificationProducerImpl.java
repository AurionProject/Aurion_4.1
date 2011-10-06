/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.adapter.subscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.SubscribeRequestType;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author JHOPPESC
 */
public class AdapterNotificationProducerImpl {

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, subscribeRequest.getAssertion());

        return new AdapterHiemSubscribeOrchImpl().subscribe(subscribeRequest.getSubscribe(), assertion);
    }

    public SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return new AdapterHiemSubscribeOrchImpl().subscribe(subscribeRequest, assertion);
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

}
