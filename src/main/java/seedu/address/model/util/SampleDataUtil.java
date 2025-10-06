package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    /** Sample seniors with REQUIRED risk tags (HR/MR/LR). */
    public static Senior[] getSampleSeniors() {
        return new Senior[] {
                new Senior(new Name("Lim Ah Kow"), new Phone("91234567"),
                        new Address("Blk 123 Bedok North Rd #02-45"),
                        getTagSet("HR"), new Note("Has dementia")),
                new Senior(new Name("Mdm Tan"), new Phone("98887766"),
                        new Address("Blk 88 Hougang Ave 7 #05-12"),
                        getTagSet("MR"), new Note("Lives alone")),
                new Senior(new Name("Ong Siew Ling"), new Phone("97776655"),
                        new Address("Blk 321 Clementi Ave 5 #03-09"),
                        getTagSet("LR"), new Note("")),
                new Senior(new Name("Siti Nurhaliza"), new Phone("93330011"),
                        new Address("Blk 20 Toa Payoh Lor 7 #09-10"),
                        getTagSet("HR"), new Note("Fall risk"))
        };
    }

    /** Sample caregivers with FIXED IDs so the allocator starts after these. */
    public static Caregiver[] getSampleCaregivers() {
        return new Caregiver[] {
                new Caregiver(new Name("John Tan"), new Phone("90000001"),
                        new Address("Blk 10 Jurong West St 65 #07-21"),
                        new Note("Experienced with dementia care"), "c1"),
                new Caregiver(new Name("Mei Hui"), new Phone("90000002"),
                        new Address("Blk 620 Punggol Field Walk #08-23"),
                        new Note("Bilingual (EN/MS)"), "c2")
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook ab = new AddressBook();

        // add caregivers, then seniors (order doesn’t matter; ensure unique (name, phone))
        for (Caregiver cg : getSampleCaregivers()) {
            ab.addPerson(cg);
        }
        for (Senior s : getSampleSeniors()) {
            ab.addPerson(s);
        }

        // IMPORTANT: set the next caregiver id AFTER the highest c<number> present
        ab.recomputeCaregiverSeqFromData();

        return ab;
    }

    /** Returns a Tag set from strings (used for risk tags: HR/MR/LR). */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
