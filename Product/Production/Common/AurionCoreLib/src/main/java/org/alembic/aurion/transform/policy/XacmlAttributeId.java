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

/**
 *
 * @author rayj
 */
public class XacmlAttributeId {

    public static final String AssertionIssuer = "urn:org:alembic:aurion:saml-issuer";
    public static final String AuthnStatementAuthnInstant = "urn:org:alembic:aurion:saml-authn-statement:auth-instant";
    public static final String AuthnStatementSessionIndex = "urn:org:alembic:aurion:saml-authn-statement:session-index";
    public static final String AuthnStatementAthnContextClassRef = "urn:org:alembic:aurion:saml-authn-statement:auth-context-class-ref";
    public static final String AuthnStatementSubjectLocalityAddress = "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:ip-address";
    public static final String AuthnStatementDNSName = "urn:oasis:names:tc:xacml:1.0:subject:authn-locality:dns-name";
    public static final String UserPersonName = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String UserOrganizationName = "urn:org:alembic:aurion:user-organization-name";
    public static final String UserOrganizationId = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
    public static final String HomeCommunityName = "http://www.hhs.gov/healthit/nhin#HomeCommunityId";
    public static final String UniquePatientId = "http://www.hhs.gov/healthit/nhin#subject-id";
    public static final String UserRoleCode = "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static final String UserRoleCodeSystem = "urn:org:alembic:aurion:user-role-code-system";
    public static final String UserRoleCodeSystemName = "urn:org:alembic:aurion:user-role-code-system-name";
    public static final String UserRoleCodeDiplayName = "urn:org:alembic:aurion:user-role-description";
    public static final String PurposeForUseCode = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
    public static final String PurposeForUseCodeSystem = "urn:org:alembic:aurion:purpose-of-use-code-system";
    public static final String PurposeForUseCodeSystemName = "urn:org:alembic:aurion:purpose-of-use-code-system-name";
    public static final String PurposeForUseCodeDisplayName = "urn:org:alembic:aurion:purpose-of-use-display-name";
    public static final String AuthzDecisionStatementDecision = "urn:org:alembic:aurion:saml-authz-decision-statement-decision";
    public static final String AuthzDecisionStatementResource = "urn:org:alembic:aurion:saml-authz-decision-statement-resource";
    public static final String AuthzDecisionStatementAction = "urn:org:alembic:aurion:saml-authz-decision-statement-action";
    public static final String AuthzDecisionStatementEvidenceAssertionID = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-id";
    public static final String AuthzDecisionStatementEvidenceAssertionIssueInstant = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-issue-instant";
    public static final String AuthzDecisionStatementEvidenceAssertionVersion = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-version";
    public static final String AuthzDecisionStatementEvidenceAssertionIssuer = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-issuer";
    public static final String AuthzDecisionStatementEvidenceAssertionConditionsNotBefore = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-not-before";
    public static final String AuthzDecisionStatementEvidenceAssertionConditionsNotOnOrAfter = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-not-on-or-after";
    public static final String AuthzDecisionStatementEvidenceAssertionAccessConsentPolicy = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-access-consent";
    public static final String AuthzDecisionStatementEvidenceAssertionInstanceAccessConsentPolicy = "urn:org:alembic:aurion:saml-authz-decision-statement-evidence-assertion-instance-access-consent";
    public static final String SignatureKeyModulus = "urn:org:alembic:aurion:saml-signature-rsa-key-value-modulus";
    public static final String SignatureKeyExponent = "urn:org:alembic:aurion:saml-signature-rsa-key-value-exponent";
    public static final String SignatureValue = "urn:org:alembic:aurion:saml-signature-value";
    
}
