package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

/**
 * Jackson-friendly version of {@link Person} and its subclasses.
 */
class JsonAdaptedSenior {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String address;
    private final String note;
    private final String riskTag;
    private final Boolean pinned;

    private final Integer seniorId;
    private final Integer caregiverId;

    /**
     * Constructs from JSON.
     */
    @JsonCreator
    public JsonAdaptedSenior(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("address") String address,
                             @JsonProperty("note") String note,
                             @JsonProperty("risk") String riskTag,
                             @JsonProperty("seniorId") Integer seniorId,
                             @JsonProperty("caregiverId") Integer caregiverId,
                             @JsonProperty("pinned") Boolean pinned) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.riskTag = riskTag;
        this.seniorId = seniorId;
        this.caregiverId = caregiverId;
        this.pinned = pinned;
    }

    /**
     * Constructs from model.
     */
    public JsonAdaptedSenior(Senior source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.address = source.getAddress().value;
        this.note = source.getNote().value;
        this.riskTag = source.getRiskTag().tagName;
        this.seniorId = source.getId();
        this.caregiverId = source.getCaregiverId();
        this.pinned = source.getPinned();
    }

    /**
     * Returns the caregiver ID provided,
     */
    public Integer getCaregiverId() {
        return caregiverId;
    }

    /**
     * Converts this JSON-friendly object back into the model's {@code Person} subtype.
     */
    public Senior toModelType() throws IllegalValueException {
        // Validate common fields
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (riskTag == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "riskTag"));
        }
        if (!Tag.isValidTagName(riskTag)) {
            throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
        }

        final Tag modelRiskTag = new Tag(riskTag);

        final Note modelNote = (note == null) ? new Note("") : new Note(note);

        if (pinned == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "pinned"));
        }

        return new Senior(modelName, modelPhone, modelAddress, modelRiskTag, modelNote, null, seniorId, pinned);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("note", note)
                .add("riskTags", riskTag)
                .add("seniorId", seniorId)
                .add("caregiverId", caregiverId)
                .add("pinned", pinned)
                .toString();
    }
}
