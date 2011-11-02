package org.alembic.aurion.docregistry.adapter;

import java.util.ArrayList;
import java.util.List;
import org.alembic.aurion.docrepository.adapter.model.CodedElement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class AdapterComponentDocRegistryOrchImplTest {
    private AdapterComponentDocRegistryOrchImpl testSubject = null;

    @Before
    public void setUp() {
        testSubject = new AdapterComponentDocRegistryOrchImpl();
    }

    @Test
    public void testParseCodeSystemFromClassCodeSlotSingle() {

        List<CodedElement> classCodeElements = new ArrayList<CodedElement>();
        List<String> classCodeSlotValues = new ArrayList<String>();
        classCodeSlotValues.add("('code1^^coding-scheme1')");
        testSubject.parseCodeSystemFromClassCodeSlot(classCodeElements, classCodeSlotValues);
        assertEquals("Class code list size", 1, classCodeElements.size());
        CodedElement classCode = classCodeElements.get(0);
        assertNotNull("Class code was null", classCode);
        assertEquals("Code", "code1", classCode.getCode());
        assertEquals("Code system", "coding-scheme1", classCode.getCodeSystem());
    }

    @Test
    public void testParseCodeSystemFromClassCodeSlotMultiple() {

        List<CodedElement> classCodeElements = new ArrayList<CodedElement>();
        List<String> classCodeSlotValues = new ArrayList<String>();
        classCodeSlotValues.add("('code1^^coding-scheme1','code2^^coding-scheme2')");
        testSubject.parseCodeSystemFromClassCodeSlot(classCodeElements, classCodeSlotValues);
        assertEquals("Class code list size", 2, classCodeElements.size());
        CodedElement classCode1 = classCodeElements.get(0);
        assertNotNull("Class code 1 was null", classCode1);
        assertEquals("Code 1", "code1", classCode1.getCode());
        assertEquals("Code system 1", "coding-scheme1", classCode1.getCodeSystem());
        CodedElement classCode2 = classCodeElements.get(1);
        assertNotNull("Class code 2 was null", classCode2);
        assertEquals("Code 2", "code2", classCode2.getCode());
        assertEquals("Code system 2", "coding-scheme2", classCode2.getCodeSystem());
    }

}