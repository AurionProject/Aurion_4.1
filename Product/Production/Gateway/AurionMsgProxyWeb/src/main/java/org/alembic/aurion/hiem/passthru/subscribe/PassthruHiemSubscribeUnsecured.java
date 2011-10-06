/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.passthru.subscribe;

import org.alembic.aurion.nhincproxysubscriptionmanagement.InvalidFilterFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import org.alembic.aurion.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyNotificationProducer", portName = "NhincProxyNotificationProducerPortSoap", endpointInterface = "org.alembic.aurion.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType", targetNamespace = "urn:org:alembic:aurion:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/PassthruHiemSubscribeUnsecured/NhincProxySubscriptionManagement.wsdl")
@HandlerChain(file = "PassthruHiemSubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PassthruHiemSubscribeUnsecured {
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.alembic.aurion.common.nhinccommonproxy.SubscribeRequestType subscribeRequest) throws UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, SubscribeCreationFailedFault, ResourceUnknownFault, InvalidTopicExpressionFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault, InvalidFilterFault, NotifyMessageNotSupportedFault, UnacceptableInitialTerminationTimeFault, InvalidMessageContentExpressionFault, UnrecognizedPolicyRequestFault {
        PassthruHiemSubscribeImpl hiemSubscribeImpl = new PassthruHiemSubscribeImpl();
        try
        {
            return hiemSubscribeImpl.subscribe(subscribeRequest, context);
        }
        catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex)
        {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex)
        {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex)
        {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex)
        {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex)
        {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex)
        {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex)
        {
            throw new InvalidFilterFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex)
        {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }
    }

}
