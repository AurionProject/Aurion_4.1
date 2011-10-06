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
package org.alembic.aurion.transform.policy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.nhinclib.NullChecker;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author rayj
 */
public class SubjectHelper {

    public static final String SubjectCategory = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
//    private static final String UserAttributeId = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String UserRoleAttributeId = "urn:org:alembic:aurion:user-role-code";
    public static final String PurposeAttributeId = "urn:org:alembic:aurion:purpose-for-use";
    public static final String UserHomeCommunityAttributeId = "urn:org:alembic:aurion:home-community-id";

    public SubjectType subjectFactory(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectCategory);
        //subject.getAttribute().add(AttributeHelper.attributeFactory(UserAttributeId, Constants.DataTypeString, AssertionHelper.extractUserName(assertion)));
        AttributeHelper attrHelper = new AttributeHelper();
        subject.getAttribute().add(attrHelper.attributeFactory(UserHomeCommunityAttributeId, Constants.DataTypeString, determineSendingHomeCommunityId(sendingHomeCommunity, assertion)));
        return subject;
    }

    public SubjectType subjectFactoryReident(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectCategory);
        // removed as this causes the user-role-code to show up twice
        //subject.getAttribute().add(AttributeHelper.attributeFactory(UserRoleAttributeId, Constants.DataTypeString, AssertionHelper.extractUserRole(assertion)));
        //subject.getAttribute().add(AttributeHelper.attributeFactory(PurposeAttributeId, Constants.DataTypeString, AssertionHelper.extractPurpose(assertion)));
        AttributeHelper attrHelper = new AttributeHelper();
        subject.getAttribute().add(attrHelper.attributeFactory(UserHomeCommunityAttributeId, Constants.DataTypeString, determineSendingHomeCommunityId(sendingHomeCommunity, assertion)));
        return subject;
    }

    public String determineSendingHomeCommunityId(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        String homeCommunityId = null;

        if (sendingHomeCommunity != null) {
            homeCommunityId = sendingHomeCommunity.getHomeCommunityId();
        }

        if (NullChecker.isNullish(homeCommunityId)) {
            AssertionHelper assertHelp = new AssertionHelper();
            homeCommunityId = assertHelp.extractUserHomeCommunity(assertion);
        }

        return homeCommunityId;
    }
}
