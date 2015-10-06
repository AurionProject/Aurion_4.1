package org.alembic.aurion.docquery.entity;

import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.alembic.aurion.common.nhinccommon.QualifiedSubjectIdentifiersType;
import static org.mockito.Mockito.*;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class DocQueryCorrelationFilterTest {

    private Log mockLog = null;

    @Before
    public void setUp() {

        mockLog = mock(Log.class);
    }

    private DocQueryCorrelationFilter createTestSubject(QualifiedSubjectIdentifiersType correlations, final boolean filterEnabled) {
        return new DocQueryCorrelationFilter(correlations) {
            @Override
            protected Log createLogger() {
                return mockLog;
            }
            @Override
            protected boolean filterEnabled() {
                return filterEnabled;
            }

        };
    }
    
    private void addCorrelation(QualifiedSubjectIdentifiersType correlations, String assigningAuthority, String patientId) {
        QualifiedSubjectIdentifierType qualifiedIdentifier = new QualifiedSubjectIdentifierType();
        qualifiedIdentifier.setAssigningAuthorityIdentifier(assigningAuthority);
        qualifiedIdentifier.setSubjectIdentifier(patientId);
        correlations.getQualifiedSubjectIdentifier().add(qualifiedIdentifier);
    }

    private void validateFilteredCorrelations(QualifiedSubjectIdentifiersType filteredCorrelations, int expectedCount) {
        assertNotNull("Filtered correlation object was null", filteredCorrelations);
        assertNotNull("Filtered correlation list was null", filteredCorrelations.getQualifiedSubjectIdentifier());
        assertEquals("Filtered correlation list size incorrect", expectedCount, filteredCorrelations.getQualifiedSubjectIdentifier().size());
    }

    @Test
    public void testNullCorrelations() {
        QualifiedSubjectIdentifiersType correlations = null;
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 0);
    }

    @Test
    public void testEmptyCorrelations() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 0);
    }

    @Test
    public void testOneCorrelationOneDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 1);
    }

    @Test
    public void testTwoCorrelationsOneDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.1", "1235");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 1);
    }

    @Test
    public void testTwoCorrelationsTwoDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.2", "1235");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 2);
    }

    @Test
    public void testThreeCorrelationsOneDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.1", "1235");
        addCorrelation(correlations, "1.1", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 1);
    }

    @Test
    public void testThreeCorrelationsTwoDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.2", "1235");
        addCorrelation(correlations, "1.1", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 2);
    }

    @Test
    public void testThreeCorrelationsThreeDistinct() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.2", "1235");
        addCorrelation(correlations, "1.3", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, true);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 3);
    }

    @Test
    public void testThreeCorrelationsOneDistinctNoFilter() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.1", "1235");
        addCorrelation(correlations, "1.1", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, false);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 3);
    }

    @Test
    public void testThreeCorrelationsTwoDistinctNofilter() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.2", "1235");
        addCorrelation(correlations, "1.1", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, false);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 3);
    }

    @Test
    public void testThreeCorrelationsThreeDistinctNoFilter() {
        QualifiedSubjectIdentifiersType correlations = new QualifiedSubjectIdentifiersType();
        addCorrelation(correlations, "1.1", "1234");
        addCorrelation(correlations, "1.2", "1235");
        addCorrelation(correlations, "1.3", "1236");
        DocQueryCorrelationFilter testSubject = createTestSubject(correlations, false);
        QualifiedSubjectIdentifiersType filteredCorrelations = testSubject.filterDuplicateCorrelations();
        validateFilteredCorrelations(filteredCorrelations, 3);
    }

}
