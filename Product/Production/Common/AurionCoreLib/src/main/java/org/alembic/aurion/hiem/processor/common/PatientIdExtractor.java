/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.processor.common;

import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.util.format.PatientIdFormatUtil;
import org.alembic.aurion.xmlCommon.XmlUtility;
import org.alembic.aurion.xmlCommon.XpathHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.alembic.aurion.hiem.processor.faults.SoapFaultFactory;

/**
 * Utility for extracting a patient id
 *
 * @author Neil Webb
 */
public class PatientIdExtractor
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientIdExtractor.class);

    /**
     * Extract a patient identifier from an element
     *
     * @param subscribeElement Element containing subscribe message
     * @param topicConfig topic configuration containing patient id location and rules.
     * @return Extracted patient identifier
     * @throws org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault
     * @throws org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault
     */
    public QualifiedSubjectIdentifierType extractPatientIdentifier(Element subscribeElement, TopicConfigurationEntry topicConfig) throws ResourceUnknownFault, SubscribeCreationFailedFault
    {
        log.debug("begin extractPatientIdentifier");
        QualifiedSubjectIdentifierType patientIdentifier = null;
        if(log.isDebugEnabled())
        {
            log.debug("Subscribe Patient id location: " + topicConfig.getPatientIdentifierSubscribeLocation());
            log.debug("Subscribe element: " + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
        }
        String serializedPatientIdentifier = extractPatientId(subscribeElement, topicConfig.getPatientIdentifierSubscribeLocation());
        log.debug("Extracted patient identifier: " + serializedPatientIdentifier);

        if(NullChecker.isNotNullish(serializedPatientIdentifier))
        {
            patientIdentifier = new QualifiedSubjectIdentifierType();
            patientIdentifier.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(serializedPatientIdentifier));
            patientIdentifier.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(serializedPatientIdentifier));
            log.debug("Extracted Patient id: " + patientIdentifier.getSubjectIdentifier());
            log.debug("Extracted Assigning Authority: " + patientIdentifier.getAssigningAuthorityIdentifier());
        }

        if ((patientIdentifier == null) && topicConfig.isPatientRequired())
        {
            throw new SoapFaultFactory().getPatientNotInSubscribeMessage();
        }
        return patientIdentifier;
    }

    private String extractPatientId(Element subscribeElement, String patientIdentifierLocation)
    {
        log.debug("Begin extractPatientId");
        String patientId = null;
        if(NullChecker.isNotNullish(patientIdentifierLocation))
        {
            try
            {
                Node targetNode = XpathHelper.performXpathQuery(subscribeElement, patientIdentifierLocation);
                if(targetNode != null)
                {
                    patientId = XmlUtility.getNodeValue(targetNode);
                }
            }
            catch (XPathExpressionException ex)
            {
                Logger.getLogger(PatientIdExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.debug("End extractPatientId - patient id: '" + patientId + "'");
        return patientId;
    }
}
