package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class ElderlyTest {

    @Test
    public void constructor_validFields_success() {
        Set<Tag> riskTags = new HashSet<>();
        riskTags.add(new Tag("diabetic"));
        riskTags.add(new Tag("fallRisk"));

        Elderly elderly = new Elderly(
                new Name("Mr Tan Kah Li Boon"),
                new Phone("98887666"),
                new Email("tan@example.com"),
                new Address("456 Kent Ridge Street"),
                riskTags,
                new Note("Needs wheelchair access")
        );

        assertEquals("Mr Tan Kah Li Boon", elderly.getName().fullName);
        assertEquals("98887666", elderly.getPhone().value);
        assertEquals("tan@example.com", elderly.getEmail().value);
        assertEquals("456 Kent Ridge Street", elderly.getAddress().value);
        assertEquals("Needs wheelchair access", elderly.getNote().value);
        assertEquals(Set.of(new Tag("diabetic"), new Tag("fallRisk")), elderly.getRiskTags());
    }

    @Test
    public void getRiskTags_immutableSet_throwsExceptionOnModification() {
        Set<Tag> riskTags = Set.of(new Tag("hypertension"));
        Elderly elderly = new Elderly(
                new Name("Madam Lee"),
                new Phone("81234567"),
                new Email("lee@example.com"),
                new Address("789 Serangoon"),
                riskTags,
                new Note("Requires daily medication")
        );

        Set<Tag> retrievedTags = elderly.getRiskTags();
        assertThrows(UnsupportedOperationException.class, () -> retrievedTags.add(new Tag("asthma")));
    }

    @Test
    public void constructor_nullRiskTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Elderly(
                new Name("John Doe"),
                new Phone("80000000"),
                new Email("john@example.com"),
                new Address("No Address"),
                null,
                new Note("No note")
        ));
    }
}
