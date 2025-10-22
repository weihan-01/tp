package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person} and its subclasses.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    /** Discriminator: "SENIOR", "CAREGIVER", or "PERSON". */
    private final String role;

    private final String name;
    private final String phone;
    private final String address;
    private final String note;
    private final String assignedCaregiverName;
    private final String assignedCaregiverPhone;

    /**
     * Only used when role == "SENIOR": we store the single risk tag
     * (e.g., HR/MR/LR) as a one-element array for consistency.
     */
    private final List<JsonAdaptedTag> risk = new ArrayList<>();

    /** Only used when role is Caregiver */
    private final Integer caregiverId;

    /** Constructs from JSON. */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("role") String role,
                             @JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("address") String address,
                             @JsonProperty("note") String note,
                             @JsonProperty("risk") List<JsonAdaptedTag> risk,
                             @JsonProperty("caregiverId") Integer caregiverId,
                             @JsonProperty("assignedCaregiverName") String assignedCaregiverName,
                             @JsonProperty("assignedCaregiverPhone") String assignedCaregiverPhone) {
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        if (risk != null) {
            this.risk.addAll(risk);
        }
        this.caregiverId = caregiverId;
        this.assignedCaregiverName = assignedCaregiverName;
        this.assignedCaregiverPhone = assignedCaregiverPhone;
    }

    /** Constructs from model. */
    public JsonAdaptedPerson(Person source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.address = source.getAddress().value;
        this.note = source.getNote().value;

        if (source instanceof Senior) {
            this.role = "SENIOR";
            Senior s = (Senior) source;
            this.risk.addAll(s.getRiskTags().stream()
                    .map(JsonAdaptedTag::new)
                    .collect(Collectors.toList()));
            this.caregiverId = null;
            Caregiver cg = s.getCaregiver();
            this.assignedCaregiverName = (cg == null) ? null : cg.getName().fullName;
            this.assignedCaregiverPhone = (cg == null) ? null : cg.getPhone().value;

        } else if (source instanceof Caregiver) {
            this.role = "CAREGIVER";
            this.caregiverId = ((Caregiver) source).getCaregiverId();
            this.assignedCaregiverName = null;
            this.assignedCaregiverPhone = null;
        } else {
            this.role = "PERSON";
            this.caregiverId = null;
            this.assignedCaregiverName = null;
            this.assignedCaregiverPhone = null;
        }
    }

    /** Converts this JSON-friendly object back into the model's {@code Person} subtype. */
    public Person toModelType() throws IllegalValueException {
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

        // Decide subtype (legacy-friendly: if role missing but risk present → SENIOR)
        final String kind = role == null ? "" : role.trim().toUpperCase();
        final boolean looksLikeSenior = !risk.isEmpty();

        if ("SENIOR".equals(kind) || (kind.isEmpty() && looksLikeSenior)) {
            final Set<Tag> riskSet = new HashSet<>();
            for (JsonAdaptedTag t : risk) {
                riskSet.add(t.toModelType());
            }
            return new Senior(modelName, modelPhone, modelAddress, riskSet, modelNote, null, null);
        }

        if ("CAREGIVER".equals(kind)) {
            if (caregiverId == null) {
                // If you keep old data around, either delete data/addressbook.json or backfill IDs before loading.
                throw new IllegalValueException("Caregiver is missing caregiverId.");
            }
            return new Caregiver(modelName, modelPhone, modelAddress, modelNote, caregiverId);
        }

        // Default/fallback: plain Person
        return new Caregiver(modelName, modelPhone, modelAddress, modelNote, null);
    }

    String getAssignedCaregiverName() {
        return assignedCaregiverName;
    }

    String getAssignedCaregiverPhone() {
        return assignedCaregiverPhone;
    }
}
