/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.hiem.processor.entity.handler;

import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.alembic.aurion.nhinclib.NullChecker;

/**
 * Factory to create the correct entity subscribe handler based on a topic
 * configuration.
 *
 * @author Neil Webb
 */
public class EntitySubscribeHandlerFactory
{
    /**
     * Create the appropriate entity subscription message handler based on the 
     * topic configuration for the topic of the subscribe message.
     * 
     * @param topicConfig Topic configuration
     * @param patientIdentifier Optional patient identifier
     * @return Entity subscribe message handler
     */
    public EntitySubscribeHandler getEntitySubscribeHandler(TopicConfigurationEntry topicConfig, QualifiedSubjectIdentifierType patientIdentifier)
    {
        /*
         * The only initial processors will be patient-centric and non-patient-centric.
         * Additional processors may be useful in the future such as document based.
         */
        if(patientIdentifier != null)
        {
            EntitySubscribeHandler handler = new PatientCentricEntitySubscribeHandler();
            handler.setPatientIdentifier(patientIdentifier);
            handler.setPatientIdentiferLocation(topicConfig.getPatientIdentifierSubscribeLocation());
            return handler;
        }
        else
        {
            return new TargetedEntitySubscribeHandler();
        }
    }
}
