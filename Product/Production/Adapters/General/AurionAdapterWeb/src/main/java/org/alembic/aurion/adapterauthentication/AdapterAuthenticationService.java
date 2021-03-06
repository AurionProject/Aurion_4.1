/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adapterauthentication;

import javax.jws.HandlerChain;
import org.alembic.aurion.common.nhinccommonadapter.AuthenticateUserResponseType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuthentication", portName = "AdapterAuthenticationPortSoap", endpointInterface = "org.alembic.aurion.adapterauthentication.AdapterAuthenticationPortType", targetNamespace = "urn:org:alembic:aurion:adapterauthentication", wsdlLocation = "WEB-INF/wsdl/AdapterAuthenticationService/AdapterAuthentication.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "AdapterAuthenticationSoapHeaderHandler.xml")
public class AdapterAuthenticationService {

    private static Log log = LogFactory.getLog(AdapterAuthenticationService.class);

    /**
     * Given a request to authenticate a user, this service will determine if
     * an authentication service is configured for use (such as OpenSSO) and if
     * so will provide an identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public org.alembic.aurion.common.nhinccommonadapter.AuthenticateUserResponseType authenticateUser(org.alembic.aurion.common.nhinccommonadapter.AuthenticateUserRequestType authenticateUserRequest) {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        AdapterAuthenticationImpl adapterAuthImpl = new AdapterAuthenticationImpl();

        try {
            authResp = adapterAuthImpl.authenticateUser(authenticateUserRequest);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterAuthenticationImpl.authenticateUser.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return authResp;
    }
}
