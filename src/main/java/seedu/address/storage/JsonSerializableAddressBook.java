package seedu.address.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Monotonic sequence used to allocate caregiver IDs of the form {@code cN}.
     * This stores the last allocated number (e.g., if the highest ID is c9, caregiverSeq == 9).
     */
    private final Integer caregiverSeq;

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and sequence.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("caregiverSeq") Integer caregiverSeq) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        this.caregiverSeq = caregiverSeq; // may be null for legacy files
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        this.persons.addAll(source.getPersonList().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));

        // If we have a concrete AddressBook, read the stored sequence; otherwise, fall back to 0.
        if (source instanceof AddressBook) {
            this.caregiverSeq = ((AddressBook) source).getCaregiverSeq();
        } else {
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

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }

        // Restore (or initialize) the caregiver ID sequence.
        if (caregiverSeq == null) {
            // Legacy file with no sequence stored: recompute once from existing caregivers.
            addressBook.recomputeCaregiverSeqFromData();
        } else {
            // Trust the stored sequence, but ensure it's at least the current max in data.
            addressBook.setCaregiverSeq(caregiverSeq);
            addressBook.recomputeCaregiverSeqFromData(); // raises seq if data has higher IDs
        }

        return addressBook;
    }
}
