/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.hiem.passthru.notify;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.NotifyRequestSecuredType;
import org.alembic.aurion.common.nhinccommonproxy.NotifyRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemNotifyImpl {

    public void notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, notifyRequest.getAssertion());
        ReferenceParametersElements referenceParametersElements = getRefParameters(context);
        Element notifyElement = getNotifyElement (context);
        new PassthruHiemNotifyOrchImpl().notify(notifyRequest.getNotify(), assertion, notifyRequest.getNhinTargetSystem(), referenceParametersElements, notifyElement);
    }

    public void notify(NotifyRequestSecuredType notifyRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        ReferenceParametersElements referenceParametersElements = getRefParameters(context);
        Element notifyElement = getNotifyElement (context);
        new PassthruHiemNotifyOrchImpl().notify(notifyRequest.getNotify(), assertion, notifyRequest.getNhinTargetSystem(), referenceParametersElements, notifyElement);

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

    private ReferenceParametersElements getRefParameters(WebServiceContext context) {
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "notifySoapMessage");
        return referenceParametersElements;
    }

    private Element getNotifyElement(WebServiceContext context) {
        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");
        return notifyElement;
    }
}
