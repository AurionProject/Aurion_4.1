/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.hiem.passthru.subscribe;

import org.alembic.aurion.async.AsyncMessageIdExtractor;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestSecuredType;
import org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestType;

import org.alembic.aurion.hiem.dte.SoapUtil;
import org.alembic.aurion.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class PassthruHiemSubscribeImpl {

    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequestSecured, WebServiceContext context) throws InvalidMessageContentExpressionFault, UnacceptableInitialTerminationTimeFault, InvalidTopicExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidFilterFault, NotifyMessageNotSupportedFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, ResourceUnknownFault, SubscribeCreationFailedFault, UnsupportedPolicyRequestFault, UnrecognizedPolicyRequestFault {
        PassthruHiemSubscribeOrchImpl hiemSubscribeImpl = new PassthruHiemSubscribeOrchImpl();
        AssertionType assertion = getAssertion(context, null);
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");
        try {
            return hiemSubscribeImpl.subscribe(subscribeRequestSecured.getSubscribe(), assertion, subscribeRequestSecured.getNhinTargetSystem(), subscribeElement);
        } catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex) {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex) {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex) {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex) {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex) {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex) {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex) {
            throw new InvalidFilterFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex) {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }
    }

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context) throws UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, SubscribeCreationFailedFault, ResourceUnknownFault, InvalidTopicExpressionFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidMessageContentExpressionFault, UnrecognizedPolicyRequestFault {

        PassthruHiemSubscribeOrchImpl hiemSubscribeImpl = new PassthruHiemSubscribeOrchImpl();
        AssertionType assertion = getAssertion(context, subscribeRequest.getAssertion());
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");
        try {
            return hiemSubscribeImpl.subscribe(subscribeRequest.getSubscribe(), assertion, subscribeRequest.getNhinTargetSystem(), subscribeElement);
        } catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex) {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex) {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex) {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex) {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex) {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex) {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex) {
            throw new InvalidFilterFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex) {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), null);
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

    private Element getSubscribeElement(WebServiceContext context) {
        Element subscribeElement = new SoapUtil().extractFirstElement(context, "subscribeSoapMessage", "Subscribe");
        return subscribeElement;
    }
}
