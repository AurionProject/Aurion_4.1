/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//##
//## Copyright (c) 2010 Harris Corporation
//##
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//##
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//##
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//##
//####################################################################
//********************************************************************
// FILE: TestClientHandshaker.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: TestClientHandshaker.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package org.alembic.aurion.lift.proxy.client;

import org.alembic.aurion.lift.common.util.LiftConnectionRequestToken;
import java.io.IOException;
import java.io.StringReader;

import org.alembic.aurion.lift.proxy.util.ClientHandshaker;
import org.alembic.aurion.lift.proxy.util.ProtocolWrapper;
import org.alembic.aurion.lift.proxy.util.ProxyUtil;
import org.alembic.aurion.lift.proxy.util.LiftConnectionResponseToken;
import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestClientHandshaker implements ClientHandshaker {

    private Log log = null;

    public TestClientHandshaker() {
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    @Override
    public boolean handshake(ProtocolWrapper wrapper, Client client) throws IOException {
        try {
            log.debug("Initiating handshake.");
            // Need to send security info to server for validation.
            LiftConnectionRequestToken token = new LiftConnectionRequestToken(client.getToken().getRequest());
            wrapper.sendLine(ProxyUtil.marshalToString(token));
            String response = wrapper.readLine();
            LiftConnectionResponseToken resp = (LiftConnectionResponseToken) ProxyUtil.unmarshalFromReader(
                    new StringReader(response), LiftConnectionResponseToken.class);
            log.debug("Response to handshake: " + resp.getPermission());
            // Return if was a good response or not.
            return resp.isPermitted();
        } catch (JAXBException ex) {
            log.error("JAXB exception in handshake: " + ex.getMessage());
            throw new IOException("JAXB exception in handshake: " + ex.getMessage());
        }
    }
}
