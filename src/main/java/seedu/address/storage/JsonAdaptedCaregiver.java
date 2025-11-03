package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Jackson-friendly version of {@link Person} and its subclasses.
 */
class JsonAdaptedCaregiver {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String address;
    private final String note;
    private final Boolean pinned;

    /** Only used when role is Caregiver */
    private final Integer caregiverId;

    /** Constructs from JSON. */
    @JsonCreator
    public JsonAdaptedCaregiver(@JsonProperty("name") String name,
                                @JsonProperty("phone") String phone,
                                @JsonProperty("address") String address,
                                @JsonProperty("note") String note,
                                @JsonProperty("caregiverId") Integer caregiverId,
                                @JsonProperty("pinned") Boolean pinned) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.caregiverId = caregiverId;
        this.pinned = pinned;
    }

    /** Constructs from model. */
    public JsonAdaptedCaregiver(Person source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.address = source.getAddress().value;
        this.note = source.getNote().value;
        this.caregiverId = ((Caregiver) source).getId();
        this.pinned = source.getPinned();
    }

    /** Converts this JSON-friendly object back into the model's {@code Person} subtype. */
    public Caregiver toModelType() throws IllegalValueException {
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

        if (caregiverId == null) {
            // If you keep old data around, either delete data/addressbook.json or backfill IDs before loading.
            throw new IllegalValueException("Caregiver is missing caregiverId.");
        }
        return new Caregiver(modelName, modelPhone, modelAddress, modelNote, caregiverId, pinned);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("note", note)
                .add("caregiverId", caregiverId)
                .add("pinned", pinned)
                .toString();
    }
}
