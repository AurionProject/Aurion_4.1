package org.alembic.aurion.hiem.entity.subscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentRequestSecuredType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentResponseType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeRequestSecuredType;
import org.alembic.aurion.common.nhinccommonentity.SubscribeRequestType;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidFilterFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.ResourceUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.TopicNotSupportedFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault;
import org.alembic.aurion.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.WebServiceContext;
import org.alembic.aurion.hiem.dte.SoapUtil;
import org.w3c.dom.Element;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import org.alembic.aurion.util.soap.SoapLogger;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityNotificationProducerImpl
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityNotificationProducerImpl.class);

    public SubscribeDocumentResponseType subscribeDocument(SubscribeDocumentRequestSecuredType arg0)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(org.alembic.aurion.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequest, WebServiceContext context) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        log.debug("In secure subscribe");
        AssertionType assertion = getAssertion(context, null);
        getSoapLogger().logRawAssertion(assertion);

        Element subscribeElement = getSubscribeElement(context);
        SubscribeResponse response = new EntityHiemSubscribeOrchImpl().subscribe(subscribeRequest.getSubscribe(), assertion, subscribeRequest.getNhinTargetCommunities(), subscribeElement);

        return response;
    }

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context) throws NotifyMessageNotSupportedFault, UnsupportedPolicyRequestFault, InvalidMessageContentExpressionFault, UnacceptableInitialTerminationTimeFault, InvalidProducerPropertiesExpressionFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, UnrecognizedPolicyRequestFault, InvalidFilterFault, InvalidTopicExpressionFault, ResourceUnknownFault, SubscribeCreationFailedFault {
        log.debug("In unsecure subscribe");
        AssertionType assertion = getAssertion(context, subscribeRequest.getAssertion());
        getSoapLogger().logRawAssertion(assertion);

        Element subscribeElement = getSubscribeElement(context);
        SubscribeResponse response = new EntityHiemSubscribeOrchImpl().subscribe(subscribeRequest.getSubscribe(), assertion, subscribeRequest.getNhinTargetCommunities(), subscribeElement);

        return response;
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

    private Element getSubscribeElement(WebServiceContext context) {
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");
        return subscribeElement;
    }

    protected SoapLogger getSoapLogger() {
        return new SoapLogger();
    }

}
