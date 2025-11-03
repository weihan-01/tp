package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ElderlyTest {

    @Test
    public void constructor_validFields_success() {
        Tag riskTag = new Tag("MR");

        Senior senior = new Senior(
                new Name("Mr Tan Kah Li Boon"),
                new Phone("98887666"),
                new Address("456 Kent Ridge Street"),
                riskTag,
                new Note("Needs wheelchair access"),
                null,
                1,
                false
        );

        assertEquals("Mr Tan Kah Li Boon", senior.getName().fullName);
        assertEquals("98887666", senior.getPhone().value);
        assertEquals("456 Kent Ridge Street", senior.getAddress().value);
        assertEquals("Needs wheelchair access", senior.getNote().value);
        assertEquals("MR", senior.getRiskTag().tagName);
    }

    @Test
    public void constructor_nullRiskTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Senior(
                new Name("John Doe"),
                new Phone("80000000"),
                new Address("No Address"),
                null,
                new Note("No note"),
                null, 1,
                false
        ));
    }
}
