/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.connectmgr.uddi;

import javax.jws.WebService;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentUDDIUpdateManager", portName = "NhincComponentUDDIUpdateManagerPortSoap11", endpointInterface = "org.alembic.aurion.nhinccomponentuddiupdatemanager.NhincComponentUDDIUpdateManagerPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentuddiupdatemanager", wsdlLocation = "WEB-INF/wsdl/UDDIUpdateManager/NhincComponentUDDIUpdateManager.wsdl")
public class UDDIUpdateManager {

    private static Log log = LogFactory.getLog(UDDIUpdateManager.class);

    static {
        //WORKAROUND NEEDED IN METRO1.4. TO BE REMOVED LATER.
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String hostname,
                            javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });
    }

    /**
     * Default constructor.
     */
    public UDDIUpdateManager() {

        try {
            UDDITimer.startTimer();
        } catch (Exception e) {
            String sErrorMessage = "Failed to start UDDIUpdateManager's timer.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    public org.alembic.aurion.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshResponseType forceRefreshFileFromUDDIServer(org.alembic.aurion.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshRequestType uddiUpdateManagerForceRefreshRequest) {
        UDDIUpdateManagerHelper helper = new UDDIUpdateManagerHelper();
        return helper.forceRefreshFileFromUDDIServer(uddiUpdateManagerForceRefreshRequest);
    }
}
