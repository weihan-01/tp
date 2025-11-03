package seedu.address.testutil;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

/**
 * A utility class to help with building Senior objects.
 */
public class SeniorBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_NOTE = "";
    public static final String DEFAULT_TAG = "MR";
    public static final Caregiver DEFAULT_CAREGIVER = new CaregiverBuilder()
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withNote("10 years experience with nursing")
            .withPinned(false)
            .build();
    public static final Integer DEFAULT_SENIOR_ID = 1;
    public static final Boolean DEFAULT_PINNED = false;

    private Name name;
    private Phone phone;
    private Address address;
    private Note note;
    private Tag riskTag;
    private Caregiver caregiver;
    private Integer seniorId;
    private Boolean isPinned;

    /**
     * Creates a {@code SeniorBuilder} with the default details.
     */
    public SeniorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        //riskTags = new HashSet<>(); // seniors can have risk tags
        riskTag = new Tag(DEFAULT_TAG);
        note = new Note(DEFAULT_NOTE);
        caregiver = DEFAULT_CAREGIVER;
        seniorId = DEFAULT_SENIOR_ID;
        isPinned = DEFAULT_PINNED;
    }

    /**
     * Initializes the SeniorBuilder with the data of {@code seniorToCopy}.
     */
    public SeniorBuilder(Senior seniorToCopy) {
        name = seniorToCopy.getName();
        phone = seniorToCopy.getPhone();
        address = seniorToCopy.getAddress();
        //riskTags = new HashSet<>(seniorToCopy.getRiskTags());
        riskTag = seniorToCopy.getRiskTag();
        note = seniorToCopy.getNote();
        caregiver = seniorToCopy.getCaregiver();
        seniorId = seniorToCopy.getId();
        isPinned = seniorToCopy.getPinned();
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
    public SeniorBuilder withRiskTag(String riskTag) {
        this.riskTag = new Tag(riskTag);
        return this;
    }

    /**
     * Parses the {@code isPinned} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withPinned(Boolean isPinned) {
        this.isPinned = isPinned;
        return this;
    }

    /**
     * Parses the {@code caregiver} of the {@code Senior} that we are building.
     */
    public SeniorBuilder withCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
        return this;
    }


    public Senior build() {
        return new Senior(name, phone, address, riskTag, note, caregiver, seniorId, isPinned);
    }
}
