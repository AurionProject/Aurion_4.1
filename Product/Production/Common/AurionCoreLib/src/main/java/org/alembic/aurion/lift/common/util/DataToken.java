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
// FILE: DataToken.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: DataToken.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package org.alembic.aurion.lift.common.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataToken")
public class DataToken {
	@XmlElement(name = "ServerProxyData", required = true)
	private ServerProxyDataToken serverProxyData;
	
	@XmlElement(name = "ClientData", required = true)
	private ClientDataToken clientData;

	public DataToken() {
	}

	public DataToken(ServerProxyDataToken serverProxyData,
			ClientDataToken clientData) {
		super();
		this.serverProxyData = serverProxyData;
		this.clientData = clientData;
	}

	public ServerProxyDataToken getServerProxyData() {
		return serverProxyData;
	}

	public void setServerProxyData(ServerProxyDataToken serverProxyData) {
		this.serverProxyData = serverProxyData;
	}

	public ClientDataToken getClientData() {
		return clientData;
	}

	public void setClientData(ClientDataToken clientData) {
		this.clientData = clientData;
	}
	
}
