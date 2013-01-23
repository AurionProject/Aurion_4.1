package org.alembic.aurion.hiem.adapter.unsubscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.UnsubscribeRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author JHOPPESC
 */
public class AdapterSubscriptionManagerImpl {
    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, unsubscribeRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements refParams = getRefParams(context);

        return new AdapterHiemUnsubscribeOrchImpl().unsubscribe(unsubscribeRequest.getUnsubscribe(), refParams, assertion);
    }

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements refParams = getRefParams(context);

        return new AdapterHiemUnsubscribeOrchImpl().unsubscribe(unsubscribeRequest, refParams, assertion);
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
