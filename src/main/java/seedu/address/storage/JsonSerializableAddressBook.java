package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.exceptions.ParseException;
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

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

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
                .toList());

        this.caregivers.addAll(source.getCaregiverList().stream()
                .map(JsonAdaptedCaregiver::new)
                .toList());

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

        // Record Senior -> Caregiver assignments to be added after all
        // seniors and caregivers added to AddressBook
        List<Map.Entry<Senior, Integer>> links = new ArrayList<>();

        for (JsonAdaptedSenior jsonAdaptedSenior : seniors) {
            Senior senior = jsonAdaptedSenior.toModelType();
            if (addressBook.hasPerson(senior)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addSenior(senior);

            Integer caregiverId = jsonAdaptedSenior.getCaregiverId();
            if (caregiverId != null) {
                links.add(new java.util.AbstractMap.SimpleEntry<>(senior, caregiverId));
            }
        }

        logger.log(Level.INFO, "All seniors from JSON records have been added");

        for (JsonAdaptedCaregiver jsonAdaptedCaregiver : caregivers) {
            Caregiver caregiver = jsonAdaptedCaregiver.toModelType();
            if (addressBook.hasPerson(caregiver)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addCaregiver(caregiver);
        }

        logger.log(Level.INFO, "All caregivers from JSON records have been added");

        // Create map of all caregivers to reference by ID
        java.util.Map<Integer, Caregiver> byKey = addressBook.getCaregiverList().stream()
            .collect(java.util.stream.Collectors.toMap(
                    cg -> cg.getCaregiverId(),
                    cg -> cg, (a, b) -> a // if duplicate keys, keep first
            ));

        // Add assignments of seniors and caregivers
        for (Map.Entry<Senior, Integer> entry : links) {
            Integer caregiverId = entry.getValue();
            if (caregiverId == null) {
                continue;
            }
            Caregiver caregiver = byKey.get(caregiverId);
            if (caregiver != null) {
                entry.getKey().setCaregiver(caregiver);
            } else {
                throw new ParseException(String.format(MESSAGE_DUPLICATE_PERSON, caregiverId));
            }
        }

        logger.log(Level.INFO, "All existing assignments between seniors and caregivers from JSON records have been added");

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
}


