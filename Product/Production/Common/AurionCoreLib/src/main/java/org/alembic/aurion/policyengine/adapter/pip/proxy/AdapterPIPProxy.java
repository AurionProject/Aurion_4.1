/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.policyengine.adapter.pip.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import org.alembic.aurion.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import org.alembic.aurion.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import org.alembic.aurion.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import org.alembic.aurion.common.nhinccommonadapter.StorePtConsentRequestType;
import org.alembic.aurion.common.nhinccommonadapter.StorePtConsentResponseType;

/**
 * Component proxy Java interface for Adapter PIP (Policy Informaion Point)
 *
 * @author Les Westberg
 */
public interface AdapterPIPProxy
{
    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request, AssertionType assertion);

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request, AssertionType assertion);

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request, AssertionType assertion);

}
