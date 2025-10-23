package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person} and its subclasses.
 */
class JsonAdaptedSenior {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    /** Discriminator: "SENIOR", "CAREGIVER", or "PERSON". */
    private final String role;

    private final String name;
    private final String phone;
    private final String address;
    private final String note;

    /**
     * Only used when role == "SENIOR": we store the single risk tag
     * (e.g., HR/MR/LR) as a one-element array for consistency.
     */
    private final List<JsonAdaptedTag> riskTags = new ArrayList<>();

    private final Integer seniorId;
    private final Integer caregiverId;

    /** Constructs from JSON. */
    @JsonCreator
    public JsonAdaptedSenior(@JsonProperty("role") String role,
                             @JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("address") String address,
                             @JsonProperty("note") String note,
                             @JsonProperty("risk") List<JsonAdaptedTag> risk,
                             @JsonProperty("seniorId") Integer seniorId,
                             @JsonProperty("caregiverId") Integer caregiverId) {
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        if (risk != null) {
            this.riskTags.addAll(risk);
        }
        this.seniorId = seniorId;
        this.caregiverId = caregiverId;
    }

    /** Constructs from model. */
    public JsonAdaptedSenior(Senior source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.address = source.getAddress().value;
        this.note = source.getNote().value;

        this.role = "SENIOR";
        this.riskTags.addAll(source.getRiskTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        this.seniorId = source.getSeniorId();
        this.caregiverId = source.getCaregiverId();
    }

    /** Returns the caregiver ID provided, */
    public Integer getCaregiverId() {
        return caregiverId;
    }

    /** Converts this JSON-friendly object back into the model's {@code Person} subtype. */
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

        final Note modelNote = (note == null) ? new Note("") : new Note(note);

        final Set<Tag> riskSet = new HashSet<>();
        for (JsonAdaptedTag t : riskTags) {
            riskSet.add(t.toModelType());
        }

        return new Senior(modelName, modelPhone, modelAddress, riskSet, modelNote, null, seniorId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("role", role)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("note", note)
                .add("riskTags", riskTags)
                .add("seniorId", seniorId)
                .add("caregiverId", caregiverId)
                .toString();
    }
}
