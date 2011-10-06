/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.redactionengine.adapter;

import org.alembic.aurion.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import org.alembic.aurion.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterComponentRedactionEngineService", portName = "AdapterComponentRedactionEnginePortTypeBindingPort", endpointInterface = "org.alembic.aurion.adaptercomponentredaction.AdapterComponentRedactionEnginePortType", targetNamespace = "urn:org:alembic:aurion:adaptercomponentredaction", wsdlLocation = "WEB-INF/wsdl/AdapterComponentRedactionEngine/AdapterComponentRedactionEngine.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterComponentRedactionEngine
{
    @Resource
    private WebServiceContext context;

    public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType filterDocQueryResultsRequest)
    {
        FilterDocQueryResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocQueryResults(filterDocQueryResultsRequest, context);
        }

        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest)
    {
        FilterDocRetrieveResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocRetrieveResults(filterDocRetrieveResultsRequest, context);
        }

        return response;
    }

    protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
    {
        return new AdapterComponentRedactionEngineImpl();
    }
}
