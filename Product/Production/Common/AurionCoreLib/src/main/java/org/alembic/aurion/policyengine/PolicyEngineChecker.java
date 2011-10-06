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
package org.alembic.aurion.policyengine;

import org.alembic.aurion.common.eventcommon.AdhocQueryRequestEventType;
import org.alembic.aurion.common.eventcommon.AdhocQueryResultEventType;
import org.alembic.aurion.common.eventcommon.DocRetrieveEventType;
import org.alembic.aurion.common.eventcommon.FindAuditEventsEventType;
import org.alembic.aurion.common.eventcommon.NotifyEventType;
import org.alembic.aurion.common.eventcommon.PatDiscReqEventType;
import org.alembic.aurion.common.eventcommon.SubjectAddedEventType;
import org.alembic.aurion.common.eventcommon.SubjectReidentificationEventType;
import org.alembic.aurion.common.eventcommon.SubjectRevisedEventType;
import org.alembic.aurion.common.eventcommon.SubscribeEventType;
import org.alembic.aurion.common.eventcommon.UnsubscribeEventType;
import org.alembic.aurion.common.nhinccommonadapter.CheckPolicyRequestType;
import org.alembic.aurion.transform.policy.PolicyEngineTransformer;
import org.alembic.aurion.common.eventcommon.XDREventType;
import org.alembic.aurion.common.eventcommon.XDRResponseEventType;

/**
 *
 * @author Jon Hoppesch
 */
public class PolicyEngineChecker {

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery announce request
     *
     * @param request Policy check request message for the subject discovery announce
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectAdded(SubjectAddedEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectAddedToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery announce request
     *
     * @param request Policy check request message for the subject discovery announce
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyPatDiscRequest(PatDiscReqEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformPatDiscReqToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery revised request
     *
     * @param request Policy check request message for the subject discovery revised
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectRevised(SubjectRevisedEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectRevisedToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subject discovery reidentification request
     *
     * @param request Policy check request message for the subject discovery reidentification
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubjectReidentification(SubjectReidentificationEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectReidentificationToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a document query request
     *
     * @param request Policy check request message for the document query
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyAdhocQuery(AdhocQueryRequestEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformAdhocQueryToCheckPolicy(request);
    }

    public CheckPolicyRequestType checkPolicyAdhocQueryResponse(AdhocQueryResultEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformAdhocQueryResultToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a document retrieve request
     *
     * @param request Policy check request message for the document retrieve
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyDocRetrieve(DocRetrieveEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformDocRetrieveToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * an audit query request
     *
     * @param request Policy check request message for the audit query
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyFindAuditEvents(FindAuditEventsEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformFindAuditEventsToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a subscribe request
     *
     * @param request Policy check request message for the subscribe
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicySubscribe(SubscribeEventType checkPolicySubscribeRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubscribeToCheckPolicy(checkPolicySubscribeRequest);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * an unsubscribe request.
     *
     * @param request Policy check request message for the unsubscribe
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyUnsubscribe(UnsubscribeEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformUnsubscribeToCheckPolicy(request);
    }

    /**
     * This method will create the generic Policy Check Request Message from
     * a notify request
     *
     * @param request Policy check request message for the notify request
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyNotify(NotifyEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformNotifyToCheckPolicy(request);
    }
    public CheckPolicyRequestType checkPolicyXDRRequest(XDREventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformXDRRequestToCheckPolicy(request);
    }

    public CheckPolicyRequestType checkPolicyXDRResponse(XDRResponseEventType request) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformXDRResponseInputToCheckPolicy(request);
    }

}
