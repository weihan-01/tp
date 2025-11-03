package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_NO_SENIORS_PROMPT = "No seniors in AddressBook. Use add-snr to add a senior!";
    public static final String MESSAGE_NO_CAREGIVERS_PROMPT =
            "No caregivers in AddressBook. Use add-cgr command to add a caregiver!";
    public static final String MESSAGE_EMPTY_ADDRESS =
            "Address is required for seniors and cannot be blank.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        // person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code senior} for display to the user.
     */
    public static String formatSenior(Senior senior) {
        final StringBuilder builder = new StringBuilder();
        builder.append(senior.getName())
                .append("; Risk: ")
                .append(senior.getRiskTag())
                .append("; Phone: ")
                .append(senior.getPhone())
                .append("; Address: ")
                .append(senior.getAddress());

        // Notes only if present
        if (senior.getNote() != null && !senior.getNote().toString().trim().isEmpty()) {
            builder.append("; Notes: ").append(senior.getNote());
        }

        // person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code senior} for display to the user.
     */
    public static String formatCaregiver(Caregiver caregiver) {
        final StringBuilder builder = new StringBuilder();
        builder.append(caregiver.getName())
                .append("; Phone: ")
                .append(caregiver.getPhone());

        // Address only if present
        if (caregiver.getAddress() != null && !caregiver.getAddress().toString().trim().isEmpty()) {
            builder.append("; Address: ").append(caregiver.getAddress());
        }

        // Notes only if present
        if (caregiver.getNote() != null && !caregiver.getNote().toString().trim().isEmpty()) {
            builder.append("; Notes: ").append(caregiver.getNote());
        }
        return builder.toString();
    }

}
