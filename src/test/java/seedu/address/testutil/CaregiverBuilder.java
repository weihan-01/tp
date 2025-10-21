package seedu.address.testutil;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;

/**
 * A utility class to help with building {@code Caregiver} objects for tests.
 * Mirrors PersonBuilder defaults, plus caregiverId.
 */
public class CaregiverBuilder {

    public static final String DEFAULT_NAME = PersonBuilder.DEFAULT_NAME;
    public static final String DEFAULT_PHONE = PersonBuilder.DEFAULT_PHONE;
    public static final String DEFAULT_ADDRESS = PersonBuilder.DEFAULT_ADDRESS;
    public static final String DEFAULT_NOTE = PersonBuilder.DEFAULT_NOTE;
    public static final String DEFAULT_CAREGIVER_ID = "c1";

    private Name name;
    private Phone phone;
    private Address address;
    private Note note;
    private String caregiverId;

    /** Creates a {@code CaregiverBuilder} with the default details. */
    public CaregiverBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        note = new Note(DEFAULT_NOTE);
        caregiverId = DEFAULT_CAREGIVER_ID;
    }

    /** Initializes the builder with data from {@code caregiverToCopy}. */
    public CaregiverBuilder(Caregiver caregiverToCopy) {
        name = caregiverToCopy.getName();
        phone = caregiverToCopy.getPhone();
        address = caregiverToCopy.getAddress();
        note = caregiverToCopy.getNote();
        caregiverId = caregiverToCopy.getCaregiverId();
    }

    public CaregiverBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    public CaregiverBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    public CaregiverBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    public CaregiverBuilder withNote(String note) {
        this.note = new Note(note);
        return this;
    }

    /** Sets the caregiver ID, e.g. "c7". Must match c\\d+ (same as model validation). */
    public CaregiverBuilder withCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
        return this;
    }

    public Caregiver build() {
        return new Caregiver(name, phone, address, note, caregiverId);
    }
}
