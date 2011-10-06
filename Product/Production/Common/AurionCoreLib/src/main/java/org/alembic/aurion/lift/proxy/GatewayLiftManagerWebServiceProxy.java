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
package org.alembic.aurion.lift.proxy;

import org.alembic.aurion.connectmgr.ConnectionManagerCache;
import org.alembic.aurion.connectmgr.ConnectionManagerException;
import org.alembic.aurion.gateway.lift.CompleteLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.CompleteLiftTransactionResponseType;
import org.alembic.aurion.gateway.lift.FailedLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.FailedLiftTransactionResponseType;
import org.alembic.aurion.gateway.lift.StartLiftTransactionRequestType;
import org.alembic.aurion.gateway.lift.StartLiftTransactionResponseType;
import org.alembic.aurion.nhinccomponentliftmanager.NhincComponentLiftManager;
import org.alembic.aurion.nhinccomponentliftmanager.NhincComponentLiftManagerPortType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GatewayLiftManagerWebServiceProxy implements GatewayLiftManagerProxy {

    private static Log log = LogFactory.getLog(GatewayLiftManagerWebServiceProxy.class);
    static NhincComponentLiftManager liftManagerService = new NhincComponentLiftManager();

    private NhincComponentLiftManagerPortType getPort(String url) {
        NhincComponentLiftManagerPortType port = liftManagerService.getNhincComponentLiftManagerPort();

        if (NullChecker.isNotNullish(url)) {
            log.info("Setting endpoint address to the Lift Manager Service to " + url);
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }

        return port;
    }

    @Override
    public StartLiftTransactionResponseType startLiftTransaction(StartLiftTransactionRequestType startLiftTransactionRequest) {
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.LIFT_MANAGER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.LIFT_MANAGER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }
        NhincComponentLiftManagerPortType port = getPort(url);
        log.debug("Sending Lift request to start transaction to the Lift Manager service");
        return port.startLiftTransaction(startLiftTransactionRequest);
    }

    @Override
    public CompleteLiftTransactionResponseType completeLiftTransaction(CompleteLiftTransactionRequestType completeLiftTransactionRequest) {
                String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.LIFT_MANAGER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.LIFT_MANAGER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }
        NhincComponentLiftManagerPortType port = getPort(url);
        log.debug("Sending Lift request to notify of completed transaction to the Lift Manager service");
        return port.completeLiftTransaction(completeLiftTransactionRequest);
    }

    @Override
    public FailedLiftTransactionResponseType failedLiftTransaction(FailedLiftTransactionRequestType failedLiftTransactionRequest) {
                String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.LIFT_MANAGER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.LIFT_MANAGER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }
        NhincComponentLiftManagerPortType port = getPort(url);
        log.debug("Sending Lift request to notify of failed transaction to the Lift Manager service");
        return port.failedLiftTransaction(failedLiftTransactionRequest);
    }
}
