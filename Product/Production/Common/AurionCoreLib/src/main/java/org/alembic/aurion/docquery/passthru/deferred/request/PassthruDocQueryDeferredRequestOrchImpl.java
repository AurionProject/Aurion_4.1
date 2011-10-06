/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.docquery.passthru.deferred.request;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.docquery.DocQueryAuditLog;
import org.alembic.aurion.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxy;
import org.alembic.aurion.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;


/**
 * This implementation class contains the flow
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestOrchImpl {

    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestOrchImpl.class);


    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest adhocQueryRequest,
                                                    AssertionType assertion, NhinTargetSystemType target) {

        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");

        boolean isNhinAudit = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHIN_AUDIT_PROPERTY);
        AcknowledgementType ack = null;
        DocQueryAcknowledgementType docQueryAcknowledgement = null;

        if (adhocQueryRequest == null) {
            getLogger().error("adhocQueryRequest is null");
            return createFailureAck();
        }

        if (assertion == null) {
            getLogger().error("assertion is null");
            return createFailureAck();
        }

        if (target == null) {
            getLogger().error("target is null");
            return createFailureAck();
        }

        // Log the incoming request -- Audit Logging
        if (isNhinAudit) {
            ack = getDocQueryAuditLogger().auditDQRequest(adhocQueryRequest, assertion,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if (ack != null) {
                getLogger().debug("DocQueryDeferred Request Audit Log Acknowledgement " + ack.getMessage());
            }
        }


        //Call Nhin component proxy
        docQueryAcknowledgement = callNhinDocQueryDeferredService(adhocQueryRequest, assertion, target);

        if (docQueryAcknowledgement == null){
            getLogger().error("docQueryAcknowledgement response returned by NhinDocQueryDefferedRequest service is null");
            docQueryAcknowledgement = createFailureAck();
        }

        if (isNhinAudit) {
            ack = getDocQueryAuditLogger().logDocQueryAck(docQueryAcknowledgement, assertion,
                    NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        getLogger().debug("End of PassthruDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");
        
        return docQueryAcknowledgement;
    }


    /**
     * Returns the static logger for this class
     *
     * @return
     */

    protected Log getLogger(){
        return logger;
    }

    /**
     * Reusing the DocQuery Audit Logger for logging DocQuery Deferred Service messages
     * 
     * @return
     */
    protected DocQueryAuditLog getDocQueryAuditLogger(){
        return new DocQueryAuditLog();
    }

    /**
     * This method uses Nhin Component proxy to call Nhin service
     *
     * @return
     */
    protected DocQueryAcknowledgementType callNhinDocQueryDeferredService(AdhocQueryRequest adhocQueryRequest,
                                                    AssertionType assertion, NhinTargetSystemType target){

        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestOrchImpl.callNhinDocQueryDeferredService");

        NhinDocQueryDeferredRequestProxyObjectFactory factory = new NhinDocQueryDeferredRequestProxyObjectFactory();
        NhinDocQueryDeferredRequestProxy proxy = factory.getNhinDocQueryDeferredRequestProxy();
        DocQueryAcknowledgementType respAck = proxy.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, target);

        getLogger().debug("End of PassthruDocQueryDeferredRequestOrchImpl.callNhinDocQueryDeferredService");

        return respAck;
    }

    private DocQueryAcknowledgementType createFailureAck(){
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
        respAck.setMessage(regResp);

        return respAck;
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
            logger.error(ex.getMessage());
        }
        return sPropertyValue;
    }
}
