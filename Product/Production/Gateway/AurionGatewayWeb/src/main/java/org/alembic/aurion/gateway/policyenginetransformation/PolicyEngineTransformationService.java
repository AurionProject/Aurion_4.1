/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.gateway.policyenginetransformation;

import javax.jws.WebService;
import org.alembic.aurion.transform.policy.PolicyEngineTransformer;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentInternalPolicyEngineTransformService", portName = "NhincInternalComponentPolicyEngineTransformPort", endpointInterface = "org.alembic.aurion.nhincinternalcomponentpolicyenginetransform.NhincInternalComponentPolicyEngineTransformPortType", targetNamespace = "urn:org:alembic:aurion:nhincinternalcomponentpolicyenginetransform", wsdlLocation = "WEB-INF/wsdl/PolicyEngineTransformationService/NhincComponentInternalPolicyEngineTransform.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PolicyEngineTransformationService
{

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectAddedToCheckPolicy(org.alembic.aurion.common.eventcommon.SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(org.alembic.aurion.common.eventcommon.SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryToCheckPolicy(org.alembic.aurion.common.eventcommon.AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(org.alembic.aurion.common.eventcommon.AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveToCheckPolicy(org.alembic.aurion.common.eventcommon.DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(org.alembic.aurion.common.eventcommon.DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(org.alembic.aurion.common.eventcommon.FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(org.alembic.aurion.common.eventcommon.SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformSubscribeToCheckPolicy(org.alembic.aurion.common.eventcommon.SubscribeEventType transformSubscribeToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformUnsubscribeToCheckPolicy(org.alembic.aurion.common.eventcommon.UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);
    }

    public org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType transformNotifyToCheckPolicy(org.alembic.aurion.common.eventcommon.NotifyEventType transformNotifyToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }

}
