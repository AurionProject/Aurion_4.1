/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.transform.document;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentDocumentTransformService", portName = "NhincComponentDocumentTransformPortTypeBindingPort", endpointInterface = "org.alembic.aurion.nhinccomponentdocumenttransform.NhincComponentDocumentTransformPortType", targetNamespace = "urn:org:alembic:aurion:nhinccomponentdocumenttransform", wsdlLocation = "WEB-INF/wsdl/DocumentTransform/NhincComponentDocumentTransform.wsdl")
public class DocumentTransform {

    public org.alembic.aurion.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdResponseType replaceAdhocQueryPatientId(org.alembic.aurion.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdRequestType replaceAdhocQueryPatientIdRequest) {
        return new DocumentTransformHelper().replaceAdhocQueryPatientId(replaceAdhocQueryPatientIdRequest);
    }

}
