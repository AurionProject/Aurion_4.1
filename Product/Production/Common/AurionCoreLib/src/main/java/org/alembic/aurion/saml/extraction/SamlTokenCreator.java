/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.saml.extraction;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.nhinclib.NullChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenCreator {

    private static Log log = LogFactory.getLog(SamlTokenCreator.class);

    /**
     * This method will populate a Map with information from the assertion that is used within
     * the SAML Token.  This Map can be used to set up the requestContext prior to sending
     * a message on the Nhin.
     *
     * @param assertion The assertion object that contains information required by the SAML Token.
     * @param url The URL to the destination service.
     * @param action The specified Action for this message.
     * @return A Map containing all of the information needed for creation of the SAML Token.
     */
    public Map CreateRequestContext(AssertionType assertion, String url, String action) {
        log.debug("Entering SamlTokenCreator.CreateRequestContext...");

        Map requestContext = new HashMap();

        if (assertion != null) {
            if (assertion.getUserInfo() != null) {
                if (NullChecker.isNotNullish(assertion.getUserInfo().getUserName())) {
                    requestContext.put(NhincConstants.USER_NAME_PROP, assertion.getUserInfo().getUserName());
                }
                if (assertion.getUserInfo().getOrg() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getOrg().getName())) {
                        requestContext.put(NhincConstants.USER_ORG_PROP, assertion.getUserInfo().getOrg().getName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getOrg().getHomeCommunityId())) {
                        requestContext.put(NhincConstants.USER_ORG_ID_PROP, assertion.getUserInfo().getOrg().getHomeCommunityId());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user org is null");
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCode())) {
                        requestContext.put(NhincConstants.USER_CODE_PROP, assertion.getUserInfo().getRoleCoded().getCode());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystem())) {
                        requestContext.put(NhincConstants.USER_SYST_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystem());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystemName())) {
                        requestContext.put(NhincConstants.USER_SYST_NAME_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystemName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getDisplayName())) {
                        requestContext.put(NhincConstants.USER_DISPLAY_PROP, assertion.getUserInfo().getRoleCoded().getDisplayName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user info role is null");
                }
                if (assertion.getUserInfo().getPersonName() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getGivenName())) {
                        requestContext.put(NhincConstants.USER_FIRST_PROP, assertion.getUserInfo().getPersonName().getGivenName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getSecondNameOrInitials())) {
                        requestContext.put(NhincConstants.USER_MIDDLE_PROP, assertion.getUserInfo().getPersonName().getSecondNameOrInitials());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getFamilyName())) {
                        requestContext.put(NhincConstants.USER_LAST_PROP, assertion.getUserInfo().getPersonName().getFamilyName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user person name is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion user info is null");
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (assertion.getPurposeOfDisclosureCoded() != null) {
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCode())) {
                        requestContext.put(NhincConstants.PURPOSE_CODE_PROP, assertion.getPurposeOfDisclosureCoded().getCode());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystem())) {
                        requestContext.put(NhincConstants.PURPOSE_SYST_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystem());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystemName())) {
                        requestContext.put(NhincConstants.PURPOSE_SYST_NAME_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystemName());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                        requestContext.put(NhincConstants.PURPOSE_DISPLAY_PROP, assertion.getPurposeOfDisclosureCoded().getDisplayName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion purpose coded is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion purpose is null");
            }
            if (assertion.getHomeCommunity() != null) {
                if (NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
                    requestContext.put(NhincConstants.HOME_COM_PROP, assertion.getHomeCommunity().getHomeCommunityId());
                }

            } else {
                log.error("Error: samlSendOperation input assertion Home Community is null");
            }
            if (assertion.getUniquePatientId() != null && assertion.getUniquePatientId().size() > 0) {
                // take first non-null item in the List as the identifier
                for (String patId : assertion.getUniquePatientId()) {
                    if (NullChecker.isNotNullish(patId)) {
                        requestContext.put(NhincConstants.PATIENT_ID_PROP, patId);
                        break;
                    }
                }
            }

            // Check to see if any generic attributes are specified
            if (NullChecker.isNotNullish(assertion.getSamlAttributeAssertion())) {
                requestContext.put(NhincConstants.GENERIC_ATTRS_PROP, assertion.getSamlAttributeAssertion());
            }
            
            if (assertion.getSamlAuthnStatement() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthInstant())) {
                    requestContext.put(NhincConstants.AUTHN_INSTANT_PROP, assertion.getSamlAuthnStatement().getAuthInstant());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSessionIndex())) {
                    requestContext.put(NhincConstants.AUTHN_SESSION_INDEX_PROP, assertion.getSamlAuthnStatement().getSessionIndex());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthContextClassRef())) {
                    requestContext.put(NhincConstants.AUTHN_CONTEXT_CLASS_PROP, assertion.getSamlAuthnStatement().getAuthContextClassRef());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityAddress())) {
                    requestContext.put(NhincConstants.SUBJECT_LOCALITY_ADDR_PROP, assertion.getSamlAuthnStatement().getSubjectLocalityAddress());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityDNSName())) {
                    requestContext.put(NhincConstants.SUBJECT_LOCALITY_DNS_PROP, assertion.getSamlAuthnStatement().getSubjectLocalityDNSName());
                }
            } else {
                log.error("Error: samlSendOperation input assertion AuthnStatement is null");
            }
            if (assertion.getSamlAuthzDecisionStatement() != null) {
                requestContext.put(NhincConstants.AUTHZ_STATEMENT_EXISTS_PROP, "true");
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getAction())) {
                    requestContext.put(NhincConstants.ACTION_PROP, assertion.getSamlAuthzDecisionStatement().getAction());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getResource())) {
                    requestContext.put(NhincConstants.RESOURCE_PROP, assertion.getSamlAuthzDecisionStatement().getResource());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getDecision())) {
                    requestContext.put(NhincConstants.AUTHZ_DECISION_PROP, assertion.getSamlAuthzDecisionStatement().getDecision());
                }
                if (assertion.getSamlAuthzDecisionStatement().getEvidence() != null && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId())) {
                        requestContext.put(NhincConstants.EVIDENCE_ID_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant())) {
                        requestContext.put(NhincConstants.EVIDENCE_INSTANT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion())) {
                        requestContext.put(NhincConstants.EVIDENCE_VERSION_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer())) {
                        requestContext.put(NhincConstants.EVIDENCE_ISSUER_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat())) {
                        requestContext.put(NhincConstants.EVIDENCE_ISSUER_FORMAT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy())) {
                        requestContext.put(NhincConstants.EVIDENCE_ACCESS_CONSENT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy())) {
                        requestContext.put(NhincConstants.EVIDENCE_INST_ACCESS_CONSENT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy());
                    }
                    if (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null) {
                        if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore())) {
                            requestContext.put(NhincConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore());
                        }
                        if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter())) {
                            requestContext.put(NhincConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter());
                        }
                    } else {
                        log.error("Error: samlSendOperation input assertion AuthzDecisionStatement Evidence Conditions is null");
                    }

// JAH - COMMENTING OUT UNTIL A LATER DATE WHEN A MORE ELEGENT SOLUTION CAN BE FOUND TO HAVE GENERIC ATTRIBUTES IN TWO PLACES
//                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAttributeStatement())) {
//                        requestContext.put(NhincConstants.EVIDENCE_ATTRS_STATEMENT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAttributeStatement());
//                    }
                } else {
                    log.error("Error: samlSendOperation input assertion AuthzDecisionStatement Evidence is null");
                }

            } else {
                requestContext.put(NhincConstants.AUTHZ_STATEMENT_EXISTS_PROP, "false");
                log.info("AuthzDecisionStatement is null.  It will not be part of the SAML Assertion");
            }
            if (assertion.getSamlIssuer() != null) {
                log.info("The Saml Issuer is not null");
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuer())) {
                    requestContext.put(NhincConstants.ASSERTION_ISSUER_PROP, assertion.getSamlIssuer().getIssuer());
                } else {
                    log.info("The saml issuer - issuer was null");
                }
                if (NullChecker.isNotNullish(assertion.getSamlIssuer().getIssuerFormat())) {
                    requestContext.put(NhincConstants.ASSERTION_ISSUER_FORMAT_PROP, assertion.getSamlIssuer().getIssuerFormat());
                } else {
                    log.info("The saml issuer - format was null");
                }
            } else {
                log.info("samlSendOperation input assertion Saml Issuer is null");
            }
        } else {
            log.error("Error: samlSendOperation input assertion is null");
        }

        // This will be overwrite any value that is available in
        // assertion.getSamlAuthzDecisionStatement().getResource()
        if (NullChecker.isNotNullish(url)) {
            requestContext.put(NhincConstants.RESOURCE_PROP, url);
        }



        // This will be overwrite any value that is available in
        // assertion.getSamlAuthzDecisionStatement().getAction()
        if (NullChecker.isNotNullish(action)) {
            requestContext.put(NhincConstants.ACTION_PROP, action);
        }

        log.info("Request Context:");
        Set allKeys = requestContext.keySet();
        for (Object key : allKeys) {
            log.info(key + " = " + requestContext.get(key));
        }

        log.debug("Exiting SamlTokenCreator.CreateRequestContext...");
        return requestContext;
    }
}
