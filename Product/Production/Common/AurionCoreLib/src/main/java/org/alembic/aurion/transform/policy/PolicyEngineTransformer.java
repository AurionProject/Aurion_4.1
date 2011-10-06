/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.transform.policy;

import org.alembic.aurion.common.eventcommon.AdhocQueryRequestEventType;
import org.alembic.aurion.common.eventcommon.AdhocQueryResultEventType;
import org.alembic.aurion.common.eventcommon.DocRetrieveEventType;
import org.alembic.aurion.common.eventcommon.DocRetrieveResultEventType;
import org.alembic.aurion.common.eventcommon.FindAuditEventsEventType;
import org.alembic.aurion.common.eventcommon.NotifyEventType;
import org.alembic.aurion.common.eventcommon.PatDiscReqEventType;
import org.alembic.aurion.common.eventcommon.SubjectAddedEventType;
import org.alembic.aurion.common.eventcommon.SubjectReidentificationEventType;
import org.alembic.aurion.common.eventcommon.SubjectRevisedEventType;
import org.alembic.aurion.common.eventcommon.SubscribeEventType;
import org.alembic.aurion.common.eventcommon.UnsubscribeEventType;
import org.alembic.aurion.common.eventcommon.XDREventType;
import org.alembic.aurion.common.eventcommon.XDRResponseEventType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;


/**
 *
 * @author rayj
 */
public class PolicyEngineTransformer {

    public CheckPolicyRequestType transformSubjectAddedToCheckPolicy(SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest) {
        return SubjectAddedTransformHelper.transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformPatDiscReqToCheckPolicy(PatDiscReqEventType transformPatDiscReqToCheckPolicyRequest) {
        return new PatientDiscoveryPolicyTransformHelper().transformPatientDiscoveryNhincToCheckPolicy(transformPatDiscReqToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest) {
        return SubjectRevisedTransformHelper.transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        return AdhocQueryTransformHelper.transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        return AdhocQueryTransformHelper.transformAdhocQueryResponseToCheckPolicy(transformAdhocQueryResultToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        return DocRetrieveTransformHelper.transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        return FindAuditEventsTransformHelper.transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest) {
        return SubjectReidentificationTransformHelper.transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType transformSubscribeToCheckPolicyRequest) {
        return SubscribeTransformHelper.transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformUnsubscribeToCheckPolicy(UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest) {
        return UnsubscribeTransformHelper.transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformNotifyToCheckPolicy(NotifyEventType transformNotifyToCheckPolicyRequest) {
        return NotifyTransformHelper.transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }
    public CheckPolicyRequestType transformXDRRequestToCheckPolicy(XDREventType request) {
        return new XDRPolicyTransformHelper().transformXDRToCheckPolicy(request);
    }
    public CheckPolicyRequestType transformXDRResponseInputToCheckPolicy(XDRResponseEventType request) {
        return new XDRPolicyTransformHelper().transformXDRResponseToCheckPolicy(request);
    }

    public CheckPolicyRequestType transformDRResponseInputToCheckPolicy(DocRetrieveResultEventType request) {
        return new DocRetrieveDeferredTransformHelper().transformDocRetrieveDeferredRespToCheckPolicy(request);
    }
    
}
