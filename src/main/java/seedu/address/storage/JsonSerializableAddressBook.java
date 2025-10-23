package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedSenior> seniors = new ArrayList<>();
    private final List<JsonAdaptedCaregiver> caregivers = new ArrayList<>();

    /**
     * Monotonic sequence used to allocate senior IDs of the form {@code N}.
     * This stores the last allocated number (e.g., if the highest ID is 9, seniorSeq == 9).
     */
    private final Integer seniorSeq;

    /**
     * Monotonic sequence used to allocate caregiver IDs of the form {@code N}.
     * This stores the last allocated number (e.g., if the highest ID is 9, caregiverSeq == 9).
     */
    private final Integer caregiverSeq;

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and sequence.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("seniors") List<JsonAdaptedSenior> seniors,
                                       @JsonProperty("caregivers") List<JsonAdaptedCaregiver> caregivers,
                                       @JsonProperty("seniorSeq") Integer seniorSeq,
                                       @JsonProperty("caregiverSeq") Integer caregiverSeq) {
        if (seniors != null) {
            this.seniors.addAll(seniors);
        }
        if (caregivers != null) {
            this.caregivers.addAll(caregivers);
        }
        this.seniorSeq = seniorSeq; // may be null for legacy files
        this.caregiverSeq = caregiverSeq; // may be null for legacy files
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        this.seniors.addAll(source.getSeniorList().stream()
                .map(JsonAdaptedSenior::new)
                .collect(Collectors.toList()));

        this.caregivers.addAll(source.getCaregiverList().stream()
                .map(JsonAdaptedCaregiver::new)
                .collect(Collectors.toList()));

        // If we have a concrete AddressBook, read the stored sequence; otherwise, fall back to 0.
        if (source instanceof AddressBook) {
            this.seniorSeq = ((AddressBook) source).getSeniorSeq();
            this.caregiverSeq = ((AddressBook) source).getCaregiverSeq();
        } else {
            this.seniorSeq = 0;
            this.caregiverSeq = 0;
        }
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        java.util.List<java.util.AbstractMap.SimpleEntry<Senior, String>> links = new java.util.ArrayList<>();

        for (JsonAdaptedSenior jsonAdaptedSenior : seniors) {
            Senior senior = jsonAdaptedSenior.toModelType();
            if (addressBook.hasPerson(senior)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addSenior(senior);

            String key = compositeKey(
                    jsonAdaptedSenior.getAssignedCaregiverName(),
                    jsonAdaptedSenior.getAssignedCaregiverPhone());
            links.add(new java.util.AbstractMap.SimpleEntry<>(senior, key));
        }

        // index caregivers by (name|phone)
        java.util.Map<String, Caregiver> byKey = addressBook.getCaregiverList().stream()
                .map(x -> (Caregiver) x)
                .collect(java.util.stream.Collectors.toMap(
                         cg -> compositeKey(cg.getName().fullName, cg.getPhone().value),
                        cg -> cg, (a, b) -> a));

        // resolve Senior → Caregiver
        for (var e : links) {
            String key = e.getValue();
            if (key == null) {
                continue;
            }
            Caregiver cg = byKey.get(key);
            if (cg != null) {
                e.getKey().setCaregiver(cg);
            }
        }

        // Restore (or initialize) the caregiver ID sequence.
        if (seniorSeq == null || caregiverSeq == null) {
            // Legacy file with no sequence stored: recompute once from existing caregivers.
            addressBook.recomputeSeqFromData();
        } else {
            // Trust the stored sequence, but ensure it's at least the current max in data.
            addressBook.setSeniorSeq(seniorSeq);
            addressBook.setCaregiverSeq(caregiverSeq);
            addressBook.recomputeSeqFromData(); // raises seq if data has higher IDs
        }

        return addressBook;
    }

    private static String compositeKey(String name, String phone) {
        if (name == null || phone == null || name.isBlank() || phone.isBlank()) {
            return null;
        }
        // use a delimiter that cannot appear in phone; name can contain spaces/apostrophes—fine.
        return name + "|" + phone;
    }
}


