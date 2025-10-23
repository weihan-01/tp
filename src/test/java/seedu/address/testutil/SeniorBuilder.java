package seedu.address.testutil;

import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;

/**
 * A utility class to help with building Person objects.
 */
public class SeniorBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_NOTE = "";

    private Name name;
    private Phone phone;
    private Address address;
    private Note note;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public SeniorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        note = new Note(DEFAULT_NOTE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public SeniorBuilder(Senior seniorToCopy) {
        name = seniorToCopy.getName();
        phone = seniorToCopy.getPhone();
        address = seniorToCopy.getAddress();
        note = seniorToCopy.getNote();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public SeniorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public SeniorBuilder withTags(String ... tags) {
        // this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public SeniorBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Note} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withNote(String note) {
        this.note = new Note(note);
        return this;
    }

    public Senior build() {
        return new Senior(name, phone, address, null, note, null, null);
    }

}
