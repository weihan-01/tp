package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CaregiverTest {

    @Test
    public void constructor_validFields_success() {
        Caregiver caregiver = new Caregiver(
                new Name("Voon Shu Ting"),
                new Phone("91234567"),
                new Address("123 Toa Payoh Lorong 3"),
                new Note("Loves to dance"),
                1,
                true
        );

        assertEquals("Voon Shu Ting", caregiver.getName().fullName);
        assertEquals("91234567", caregiver.getPhone().value);
        assertEquals("123 Toa Payoh Lorong 3", caregiver.getAddress().value);
        assertEquals("Loves to dance", caregiver.getNote().value);
        assertEquals(1, caregiver.getId());
    }

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Caregiver(
                null,
                new Phone("91234567"),
                new Address("123 Toa Payoh Lorong 3"),
                new Note("Loves to dance"),
                null,
                false
        ));
    }
}
