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
package universalclientgui;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.CeType;
import org.alembic.aurion.common.nhinccommon.HomeCommunityType;
import org.alembic.aurion.common.nhinccommon.PersonNameType;
import org.alembic.aurion.common.nhinccommon.SamlAuthnStatementType;
import org.alembic.aurion.common.nhinccommon.UserType;
import org.alembic.aurion.properties.PropertyAccessException;
import org.alembic.aurion.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AssertionCreator {

    private static final String PROPERTY_FILE_NAME = "universalClient";
    private static final String PROPERTY_KEY_MSG_ORG = "AssertionMsgOrganization";
    private static final String PROPERTY_KEY_MSG_ORG_HC_ID = "AssertionMsgOrganizationHcId";
    private static final String PROPERTY_KEY_PURPOSE_CODE = "AssertionPurposeCode";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM = "AssertionPurposeSystem";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM_NAME = "AssertionPurposeSystemName";
    private static final String PROPERTY_KEY_PURPOSE_DISPLAY = "AssertionPurposeDisplay";
    private static final String PROPERTY_KEY_USER_FIRST = "AssertionUserFirstName";
    private static final String PROPERTY_KEY_USER_MIDDLE = "AssertionUserMiddleName";
    private static final String PROPERTY_KEY_USER_LAST = "AssertionUserLastName";
    private static final String PROPERTY_KEY_USER_NAME = "AssertionUserName";
    private static final String PROPERTY_KEY_USER_ORG = "AssertionUserOrganization";
    private static final String PROPERTY_KEY_USER_ORG_HC_ID = "AssertionUserOrganizationHcId";
    private static final String PROPERTY_KEY_USER_CODE = "AssertionUserCode";
    private static final String PROPERTY_KEY_USER_SYSTEM = "AssertionUserSystem";
    private static final String PROPERTY_KEY_USER_SYSTEM_NAME = "AssertionUserSystemName";
    private static final String PROPERTY_KEY_USER_DISPLAY = "AssertionUserDisplay";
    private static final String PROPERTY_KEY_EXPIRE = "AssertionExpiration";
    private static final String PROPERTY_KEY_SIGN = "AssertionSignDate";
    private static final String PROPERTY_KEY_ACCESS_CONSENT = "AssertionAccessConsent";
    private static final String PROPERTY_KEY_AUTHN_CONTEXT_CLASS_REF = "AssertionAuthContextClassRef";
    private static final String PROPERTY_KEY_AUTHN_INSTANT = "AssertionAuthInstant";
    private static final String PROPERTY_KEY_AUTHN_SESSION_INDEX = "AssertionAuthSessionIndex";
    private static final String PROPERTY_KEY_AUTHN_SUBJECT_LOCALITY_ADDRESS = "AssertionAuthSubjectLocalityAddress";
    private static final String PROPERTY_KEY_AUTHN_SUBJECT_LOCALITY_DNS = "AssertionAuthSubjectLocalityDNS";

    private static Log log = LogFactory.getLog(AssertionCreator.class);

    AssertionType createAssertion() {

        AssertionType assertOut = new AssertionType();
        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        HomeCommunityType msgHc = new HomeCommunityType();
        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setHomeCommunity(msgHc);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);
        SamlAuthnStatementType oAuthnStatement = new SamlAuthnStatementType();
        assertOut.setSamlAuthnStatement(oAuthnStatement);

        try {
            msgHc.setName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_MSG_ORG));
            msgHc.setHomeCommunityId(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_MSG_ORG_HC_ID));

            userPerson.setGivenName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_FIRST));
            userPerson.setFamilyName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_LAST));
            userPerson.setSecondNameOrInitials(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_MIDDLE));
            
            userHc.setName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_ORG));
            userHc.setHomeCommunityId(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_ORG_HC_ID));

            user.setUserName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_NAME));

            userRole.setCode(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_CODE));
            userRole.setCodeSystem(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM));
            userRole.setCodeSystemName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_SYSTEM_NAME));
            userRole.setDisplayName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_USER_DISPLAY));

            purposeCoded.setCode(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_CODE));
            purposeCoded.setCodeSystem(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM));
            purposeCoded.setCodeSystemName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_SYSTEM_NAME));
            purposeCoded.setDisplayName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_PURPOSE_DISPLAY));

            // Fill in the AuthnStatement
            //---------------------------
            oAuthnStatement.setAuthInstant(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_AUTHN_INSTANT));
            oAuthnStatement.setAuthContextClassRef(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_AUTHN_CONTEXT_CLASS_REF));
            oAuthnStatement.setSessionIndex(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_AUTHN_SESSION_INDEX));
            oAuthnStatement.setSubjectLocalityAddress(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_AUTHN_SUBJECT_LOCALITY_ADDRESS));
            oAuthnStatement.setSubjectLocalityDNSName(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_AUTHN_SUBJECT_LOCALITY_DNS));

//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_SIGN));
//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_EXPIRE));
//            assertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_ACCESS_CONSENT));

        } catch (PropertyAccessException ex) {
            log.error("Universal Client can not access property: " + ex.getMessage());
        }
        return assertOut;
    }

}
