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

package org.alembic.aurion.hiem.adapter.notify.proxy;

import org.alembic.aurion.common.nhinccommon.AcknowledgementType;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public interface AdapterHiemNotifyProxy {

    /**
     * Performs a Generic HIEM notify request to an Agency
     *
     * @param request Generic Hiem Notify request.
     * @return an Acknowledgement
     */
    public AcknowledgementType notify(Element notify, ReferenceParametersElements referenceParametersElements,AssertionType assertion) throws Exception;

    /**
     * Performs a Document HIEM notify request to an Agency
     *
     * @param request Document Hiem Notify request.
     * @return an Acknowledgement
     */
    public AcknowledgementType notifySubscribersOfDocument(Element docNotify, AssertionType assertion) throws Exception;

    /**
     * Performs a CDC HIEM notify request to an Agency
     *
     * @param request CDC Hiem Notify request.
     * @return an Acknowledgement
     */
    public AcknowledgementType notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion) throws Exception;

}
