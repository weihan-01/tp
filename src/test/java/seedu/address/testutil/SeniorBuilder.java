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
 * A utility class to help with building {@code Senior} objects for tests.
 * Mirrors PersonBuilder defaults, with extra support for risk tags.
 */
public class SeniorBuilder {

    public static final String DEFAULT_NAME = PersonBuilder.DEFAULT_NAME;
    public static final String DEFAULT_PHONE = PersonBuilder.DEFAULT_PHONE;
    public static final String DEFAULT_ADDRESS = PersonBuilder.DEFAULT_ADDRESS;
    public static final String DEFAULT_NOTE = PersonBuilder.DEFAULT_NOTE;

    private Name name;
    private Phone phone;
    private Address address;
    private Note note;
    private Set<Tag> tags;

    /** Creates a {@code SeniorBuilder} with the default details. */
    public SeniorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        note = new Note(DEFAULT_NOTE);
        tags = new HashSet<>(); // seniors can have risk tags
    }

    /** Initializes the builder with data from {@code seniorToCopy}. */
    public SeniorBuilder(Senior seniorToCopy) {
        name = seniorToCopy.getName();
        phone = seniorToCopy.getPhone();
        address = seniorToCopy.getAddress();
        note = seniorToCopy.getNote();
        tags = new HashSet<>(seniorToCopy.getRiskTags());
    }

    public SeniorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    public SeniorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    public SeniorBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

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
        return new Senior(name, phone, address, tags, note);
    }
}
