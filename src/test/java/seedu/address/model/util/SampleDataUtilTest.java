package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Tests for {@link SampleDataUtil} to ensure sample data generation is valid.
 */
public class SampleDataUtilTest {

    @Test
    public void getSampleSeniors_nonEmptyAndValid() {
        Senior[] seniors = SampleDataUtil.getSampleSeniors();
        assertTrue(seniors.length > 0);
        for (Senior s : seniors) {
            assertNotNull(s.getName());
            assertNotNull(s.getPhone());
            assertNotNull(s.getAddress());
            assertNotNull(s.getRiskTag());
        }
    }

    @Test
    public void getSampleCaregivers_nonEmptyAndValid() {
        Caregiver[] cgs = SampleDataUtil.getSampleCaregivers();
        assertTrue(cgs.length > 0);
        for (Caregiver c : cgs) {
            assertNotNull(c.getName());
            assertNotNull(c.getPhone());
            assertNotNull(c.getAddress());
        }
    }

    @Test
    public void getSampleAddressBook_containsSamplesAndSeqComputed() {
        ReadOnlyAddressBook ab = SampleDataUtil.getSampleAddressBook();
        // Should contain at least as many entries as the arrays
        assertTrue(ab.getSeniorList().size() >= SampleDataUtil.getSampleSeniors().length);
        assertTrue(ab.getCaregiverList().size() >= SampleDataUtil.getSampleCaregivers().length);

        // Re-invoking to ensure determinism
        ReadOnlyAddressBook ab2 = SampleDataUtil.getSampleAddressBook();
        assertEquals(ab.getSeniorList().size(), ab2.getSeniorList().size());
        assertEquals(ab.getCaregiverList().size(), ab2.getCaregiverList().size());
    }
}
