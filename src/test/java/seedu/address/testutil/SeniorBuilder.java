package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Senior objects.
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
    private Set<Tag> tags;

    /**
     * Creates a {@code SeniorBuilder} with the default details.
     */
    public SeniorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        note = new Note(DEFAULT_NOTE);
        tags = new HashSet<>(); // seniors can have risk tags
    }

    /**
     * Initializes the SeniorBuilder with the data of {@code seniorToCopy}.
     */
    public SeniorBuilder(Senior seniorToCopy) {
        name = seniorToCopy.getName();
        phone = seniorToCopy.getPhone();
        address = seniorToCopy.getAddress();
        note = seniorToCopy.getNote();
        tags = new HashSet<>(seniorToCopy.getRiskTags());
    }

    /**
     * Sets the {@code Name} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code Phone} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Parses the {@code Address} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Parses the {@code Note} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withNote(String note) {
        this.note = new Note(note);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets them.
     * Example: withTags("lr", "hr")
     */
    public SeniorBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Senior build() {
        return new Senior(name, phone, address, null, note, null, null);
    }
}
