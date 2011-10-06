/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.adapterauthentication.proxy;

import org.alembic.aurion.common.nhinccommonadapter.AuthenticateUserResponseType;
import org.alembic.aurion.common.nhinccommonadapter.AuthenticateUserRequestType;

/**
 * Component proxy Java interface for Adapter Authentication
 */
public interface AdapterAuthenticationProxy
{

    /**
     * Given a request to authenticate a user, this service will determine if
     * this is an identifiable user within OpenSSO and if so will provide an
     * identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest);
}
