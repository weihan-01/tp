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
import seedu.address.model.person.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    private static final Senior SENIOR_LIM = new Senior(new Name("Lim Ah Kow"), new Phone("91234567"),
            new Address("Blk 123 Bedok North Rd #02-45"),
            new Tag("HR"), new Note("Has dementia"), null, 1, false);
    private static final Senior SENIOR_TAN = new Senior(new Name("Mdm Tan"), new Phone("98887766"),
            new Address("Blk 88 Hougang Ave 7 #05-12"),
            new Tag("MR"), new Note("Lives alone"), null, 2, false);
    private static final Senior SENIOR_ONG = new Senior(new Name("Ong Siew Ling"), new Phone("97776655"),
            new Address("Blk 321 Clementi Ave 5 #03-09"),
            new Tag("LR"), new Note(""), null, 3, false);
    private static final Senior SENIOR_SITI = new Senior(new Name("Siti Nurhaliza"), new Phone("93330011"),
            new Address("Blk 20 Toa Payoh Lor 7 #09-10"),
            new Tag("HR"), new Note("Fall risk"), null, 4, false);
    private static final Caregiver CAREGIVER_TAN = new Caregiver(new Name("John Tan"), new Phone("90000001"),
            new Address("Blk 10 Jurong West St 65 #07-21"),
            new Note("Experienced with dementia care"), 1, false);
    private static final Caregiver CAREGIVER_MEIHUI = new Caregiver(new Name("Mei Hui"), new Phone("90000002"),
            new Address("Blk 620 Punggol Field Walk #08-23"),
            new Note("Bilingual (EN/MS)"), 2, false);

    /**
     * Sample seniors with REQUIRED risk tags (HR/MR/LR).
     */
    public static Senior[] getSampleSeniors() {
        SENIOR_ONG.setCaregiver(CAREGIVER_TAN);
        SENIOR_LIM.setCaregiver(CAREGIVER_MEIHUI);
        return new Senior[]{
            SENIOR_LIM, SENIOR_TAN, SENIOR_ONG, SENIOR_SITI
        };
    }

    /**
     * Sample caregivers with FIXED IDs so the allocator starts after these.
     */
    public static Caregiver[] getSampleCaregivers() {
        return new Caregiver[]{
            CAREGIVER_TAN, CAREGIVER_MEIHUI
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook ab = new AddressBook();

        // add caregivers, then seniors (order doesn’t matter; ensure unique (name, phone))
        for (Caregiver cg : getSampleCaregivers()) {
            ab.addCaregiver(cg);
        }
        for (Senior s : getSampleSeniors()) {
            ab.addSenior(s);
        }

        // IMPORTANT: set the next caregiver id AFTER the highest c<number> present
        ab.recomputeSeqFromData();

        return ab;
    }

    /**
     * Returns a Tag set from strings (used for risk tags: HR/MR/LR).
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
