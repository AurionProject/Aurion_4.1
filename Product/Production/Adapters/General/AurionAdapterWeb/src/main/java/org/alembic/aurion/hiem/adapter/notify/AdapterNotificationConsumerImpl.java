package org.alembic.aurion.hiem.adapter.notify;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.NotifyRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author JHOPPESC
 */
public class AdapterNotificationConsumerImpl {
    public AcknowledgementType notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, notifyRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements referenceParametersElements = getRefParams(context);

        AdapterHiemNotifyOrchImpl notifyOrchImpl = new AdapterHiemNotifyOrchImpl();
        try {
            return notifyOrchImpl.notify(notifyRequest.getNotify(), referenceParametersElements, assertion);
        } catch (Exception ex) {
            Logger.getLogger(AdapterNotificationConsumerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public AcknowledgementType notify(Notify notifyRequestSecured, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements referenceParametersElements = getRefParams(context);

        AdapterHiemNotifyOrchImpl notifyOrchImpl = new AdapterHiemNotifyOrchImpl();
        try {
            return notifyOrchImpl.notify(notifyRequestSecured, referenceParametersElements, assertion);
        } catch (Exception ex) {
            Logger.getLogger(AdapterNotificationConsumerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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

    private ReferenceParametersElements getRefParams(WebServiceContext context) {
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        return referenceParametersElements;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
