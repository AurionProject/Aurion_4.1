/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.patientdiscovery.entity.deferred.response;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.connectmgr.data.CMUrlInfo;
import org.alembic.aurion.connectmgr.data.CMUrlInfos;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.patientdiscovery.NhinPatientDiscoveryUtils;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryAuditLogger;
import org.alembic.aurion.patientdiscovery.passthru.deferred.response.proxy.PassthruPatientDiscoveryDeferredRespProxy;
import org.alembic.aurion.patientdiscovery.passthru.deferred.response.proxy.PassthruPatientDiscoveryDeferredRespProxyObjectFactory;
import org.alembic.aurion.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.alembic.aurion.patientdiscovery.PatientDiscovery201306Processor;
import org.alembic.aurion.patientdiscovery.PatientDiscoveryPolicyChecker;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import org.alembic.aurion.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.II;

/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseOrchImpl
{

    private static Log log = null;

    public EntityPatientDiscoveryDeferredResponseOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncRespOrch(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        AcknowledgementType ackMsg = null;
        PatientDiscovery201306Processor pd201306Processor = new PatientDiscovery201306Processor();
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        boolean isEntityAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ENTITY_AUDIT_PROPERTY);
        if (body != null && assertion != null)
        {
            urlInfoList = getTargets(target);

            //loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null)
            {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo())
                {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201306UV02RequestType newRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
                    PRPAIN201306UV02 new201306 = pd201306Processor.createNewRequest(body, urlInfo.getHcid());

                    newRequest.setAssertion(assertion);
                    newRequest.setPRPAIN201306UV02(new201306);
                    newRequest.setNhinTargetCommunities(target);
                    if (isEntityAudit) {
                        ackMsg = auditLog.auditEntity201306(newRequest, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
                    }
                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk)
                    {
                        ack = sendToProxy(newRequest, urlInfo);
                    }
                    else
                    {
                        ack = HL7AckTransforms.createAckFrom201306(body, "Policy Failed");
                    }

                    if (isEntityAudit) {
                        ackMsg = auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
                    }
                }
            }
            else
            {
                log.warn("No targets were found for the Patient Discovery Response");
                ack = HL7AckTransforms.createAckFrom201306(body, "No Targets Found");
            }
        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
    {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try
        {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
        }
        catch (ConnectionManagerException ex)
        {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201306UV02RequestType request)
    {
        II patientId = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(request.getPRPAIN201306UV02());
        return new PatientDiscoveryPolicyChecker().check201305Policy(request.getPRPAIN201306UV02(), patientId, request.getAssertion());
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetCommunitiesType target, CMUrlInfo urlInfo)
    {
        RespondingGatewayPRPAIN201306UV02RequestType request = new RespondingGatewayPRPAIN201306UV02RequestType();

        request.setAssertion(assertion);
        request.setPRPAIN201306UV02(body);
        request.setNhinTargetCommunities(target);

        return sendToProxy(request, urlInfo);
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201306UV02RequestType request, CMUrlInfo urlInfo)
    {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        PassthruPatientDiscoveryDeferredRespProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryDeferredRespProxyObjectFactory();
        PassthruPatientDiscoveryDeferredRespProxy proxy = patientDiscoveryFactory.getPassthruPatientDiscoveryDeferredRespProxy();

        resp = proxy.proxyProcessPatientDiscoveryAsyncResp(request.getPRPAIN201306UV02(), request.getAssertion(), oTargetSystemType);

        return resp;
    }

    /**
     *
     * @param sPropertiesFile
     * @param sPropertyName
     * @return boolean
     */
    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
