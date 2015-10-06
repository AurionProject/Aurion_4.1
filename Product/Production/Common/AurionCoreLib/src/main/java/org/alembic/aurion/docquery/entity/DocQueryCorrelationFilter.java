package org.alembic.aurion.docquery.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.HashSet;
import java.util.Set;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifiersType;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.properties.PropertyAccessor;

/**
 *
 * @author Neil Webb
 */
class DocQueryCorrelationFilter {
    private static final String PROPERTIES_FILE_GATEWAY = "gateway";
    private static final String PROPERTY_KEY_DQ_CORR_FILTER = "documentQueryOutboundCorrelationFilterEnabled";
    private Log log = null;
    private QualifiedSubjectIdentifiersType correlations = null;
    private Set<String> correlationAssigningAuthorities = null;

    public DocQueryCorrelationFilter(QualifiedSubjectIdentifiersType correlations) {
        super();
        log = createLogger();
        correlationAssigningAuthorities = new HashSet<String>();
        this.correlations = correlations;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean filterEnabled() {
        boolean filterEnabled = false;
        try {
            filterEnabled = PropertyAccessor.getPropertyBoolean(PROPERTIES_FILE_GATEWAY,
                    PROPERTY_KEY_DQ_CORR_FILTER);
        } catch(Exception e) {
            log.error("Error encountered looking up " +
                    PROPERTY_KEY_DQ_CORR_FILTER + " property in the " +
                    PROPERTIES_FILE_GATEWAY + " properties file: " +
                    e.getMessage(), e);
        }
        return filterEnabled;
    }

    public QualifiedSubjectIdentifiersType filterDuplicateCorrelations() {
        if(!filterEnabled()) {
            return correlations;
        }
        QualifiedSubjectIdentifiersType filteredCorrelations = new QualifiedSubjectIdentifiersType();
        if(correlationsExist()) {
            for(QualifiedSubjectIdentifierType correlation : correlations.getQualifiedSubjectIdentifier()) {
                String aa = getAssigningAuthorityFromCorrelation(correlation);
                if(NullChecker.isNotNullish(aa) && assigningAuthorityNotEncountered(aa)) {
                    filteredCorrelations.getQualifiedSubjectIdentifier().add(correlation);
                    setAssigningAuthorityEncountered(aa);
                }
            }
        }
        return filteredCorrelations;
    }

    private boolean correlationsExist() {
        return ((correlations != null) &&
                (NullChecker.isNotNullish(correlations.getQualifiedSubjectIdentifier())));
    }

    private String getAssigningAuthorityFromCorrelation(QualifiedSubjectIdentifierType correlation) {
        String assigningAuthority = null;
        if(correlation != null) {
            assigningAuthority = correlation.getAssigningAuthorityIdentifier();
        }
        return assigningAuthority;
    }

    private boolean assigningAuthorityNotEncountered(String assigningAuthority) {
        return !correlationAssigningAuthorities.contains(assigningAuthority);
    }

    private void setAssigningAuthorityEncountered(String assigningAuthority) {
        correlationAssigningAuthorities.add(assigningAuthority);
    }

}
