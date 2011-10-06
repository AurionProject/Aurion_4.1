/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.entity.subscribe;

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
@WebService(serviceName = "EntityNotificationProducerSecured", portName = "EntityNotificationProducerSecuredPortSoap", endpointInterface = "org.alembic.aurion.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType", targetNamespace = "urn:org:alembic:aurion:entitysubscriptionmanagementsecured", wsdlLocation = "WEB-INF/wsdl/EntityNotificationProducerSecured/EntitySubscriptionManagementSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntitySubscribeSoapHeaderHandler.xml")
@Addressing(enabled = true)
public class EntityNotificationProducerSecured {
    @Resource
    private WebServiceContext context;

    public org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(org.alembic.aurion.common.nhinccommonentity.SubscribeDocumentRequestSecuredType subscribeDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.alembic.aurion.common.nhinccommonentity.SubscribeRequestSecuredType subscribeRequestSecured) throws SubscribeCreationFailedFault, UnacceptableInitialTerminationTimeFault, UnsupportedPolicyRequestFault, InvalidMessageContentExpressionFault, NotifyMessageNotSupportedFault, TopicNotSupportedFault, InvalidFilterFault, ResourceUnknownFault, InvalidProducerPropertiesExpressionFault, TopicExpressionDialectUnknownFault, UnrecognizedPolicyRequestFault, InvalidTopicExpressionFault {
        return new EntityNotificationProducerImpl().subscribe(subscribeRequestSecured, context);
    }

}
