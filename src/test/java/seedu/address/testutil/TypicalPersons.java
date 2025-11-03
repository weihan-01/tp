package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CHARLES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {
    public static final Caregiver ALICE = new CaregiverBuilder()
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withNote("10 years experience with nursing")
            .withPinned(false)
            .build();
    public static final Caregiver BENSON = new CaregiverBuilder()
            .withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPhone("98765432")
            .withNote("Experience with dementia care")
            .withPinned(false)
            .withCaregiverId(1)
            .build();
    public static final Caregiver CARL = new CaregiverBuilder()
            .withName("Carl Kurz")
            .withPhone("95352563")
            .withAddress("Wall Street")
            .withNote("Speaks Chinese and English")
            .withPinned(false)
            .build();
    public static final Senior DANIEL = new SeniorBuilder()
            .withName("Daniel Meier")
            .withPhone("87652533")
            .withAddress("10th Street")
            .withRiskTag("HR")
            .withNote("Needs diabetes medicine daily")
            .withPinned(false)
            .build();
    public static final Senior ELLE = new SeniorBuilder()
            .withName("Elle Meyer")
            .withPhone("9482224")
            .withAddress("Michegan Ave")
            .withRiskTag("MR")
            .withPinned(false)
            .build();
    public static final Senior FIONA = new SeniorBuilder()
            .withName("Fiona Kunz")
            .withPhone("9482427")
            .withAddress("Little Tokyo")
            .withRiskTag("LR")
            .withNote("Stays alone")
            .withPinned(false)
            .build();
    public static final Senior GEORGE = new SeniorBuilder()
            .withName("George Best")
            .withPhone("9482442")
            .withAddress("4th Street")
            .withRiskTag("HR")
            .withNote("Has Dementia")
            .withPinned(false)
            .build();

    // Manually added
    public static final Senior HOON = new SeniorBuilder()
            .withName("Hoon Meier")
            .withPhone("8482424")
            .withAddress("Little India")
            .withRiskTag("MR")
            .withNote("Prone to falling")
            .withPinned(false)
            .build();
    public static final Caregiver IDA = new CaregiverBuilder()
            .withName("Ida Mueller")
            .withPhone("8482131")
            .withAddress("Chicago Ave")
            .withNote("Weekly meeting on wednesdays")
            .withPinned(false)
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}

    public static final Senior AMY = new SeniorBuilder()
            .withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withRiskTag(VALID_TAG_AMY)
            .withNote(VALID_NOTE_AMY).build();
    public static final Caregiver BOB = new CaregiverBuilder()
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withNote(VALID_NOTE_BOB)
            .build();
    public static final Senior CHARLES = new SeniorBuilder()
            .withName(VALID_NAME_CHARLES)
            .withPhone(VALID_PHONE_CHARLES)
            .withAddress(VALID_ADDRESS_CHARLES)
            .withRiskTag(VALID_TAG_CHARLES)
            .withNote(VALID_NOTE_CHARLES).build();


    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();

        for (Senior senior : getTypicalSeniors()) {
            ab.addSenior(senior);
        }
        for (Caregiver caregiver : getTypicalCaregivers()) {
            ab.addCaregiver(caregiver);
        }
        return ab;
    }

    public static List<Senior> getTypicalSeniors() {
        return new ArrayList<>(Arrays.asList(DANIEL, ELLE, FIONA, GEORGE));
    }

    public static List<Caregiver> getTypicalCaregivers() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL));
    }
}
