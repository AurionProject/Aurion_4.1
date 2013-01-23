package org.alembic.aurion.hiem.passthru.unsubscribe;
import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestSecuredType;
import org.alembic.aurion.common.nhinccommonproxy.UnsubscribeRequestType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersHelper;
import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.util.soap.SoapLogger;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemUnsubscribeImpl {
    public UnsubscribeResponse unsubscribe(UnsubscribeRequestSecuredType unsubscribeRequestSecured, WebServiceContext context) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements refParams = getRefParams(context);
        return new PassthruHiemUnsubscribeOrchImpl().unsubscribe(unsubscribeRequestSecured.getUnsubscribe(), assertion, unsubscribeRequestSecured.getNhinTargetSystem(), refParams);
    }

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        AssertionType assertion = getAssertion(context, unsubscribeRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);
        ReferenceParametersElements refParams = getRefParams(context);
        return new PassthruHiemUnsubscribeOrchImpl().unsubscribe(unsubscribeRequest.getUnsubscribe(), assertion, unsubscribeRequest.getNhinTargetSystem(), refParams);
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
        ReferenceParametersHelper consumerReferenceHelper = new ReferenceParametersHelper();
        ReferenceParametersElements consumerReferenceElements = consumerReferenceHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        return consumerReferenceElements;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
