package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Unassigns a caregiver from a senior in the address book.
 */
public class UnassignCommand extends Command {

    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unassigns a caregiver from a senior identified by their index numbers "
            + "in the displayed person list.\n"
            + "Parameters: "
            + PREFIX_SENIOR + "SENIOR_INDEX "
            + PREFIX_CAREGIVER + "CAREGIVER_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SENIOR + "1 "
            + PREFIX_CAREGIVER + "3";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "Senior %1$s has been unassigned from Caregiver %2$s";
    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_NOT_SENIOR = "Person at index %1$d is not a senior";
    public static final String MESSAGE_NOT_CAREGIVER = "Person at index %1$d is not a caregiver";
    public static final String MESSAGE_NOT_ASSIGNED = "This caregiver is not currently assigned to this senior";

    private final Index seniorIndex;
    private final Index caregiverIndex;

    /**
     * Creates an UnassignCommand to unassign the specified {@code Caregiver} from the specified {@code Senior}
     *
     * @param seniorIndex Index of the senior in the filtered person list
     * @param caregiverIndex Index of the caregiver in the filtered person list
     */
    public UnassignCommand(Index seniorIndex, Index caregiverIndex) {
        requireAllNonNull(seniorIndex, caregiverIndex);
        this.seniorIndex = seniorIndex;
        this.caregiverIndex = caregiverIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate senior index
        if (seniorIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_SENIOR_INDEX);
        }

        // Validate caregiver index
        if (caregiverIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_CAREGIVER_INDEX);
        }

        Person seniorPerson = lastShownList.get(seniorIndex.getZeroBased());
        Person caregiverPerson = lastShownList.get(caregiverIndex.getZeroBased());

        // Check if the persons are of correct types
        if (!(seniorPerson instanceof Senior)) {
            throw new CommandException(String.format(MESSAGE_NOT_SENIOR, seniorIndex.getOneBased()));
        }

        if (!(caregiverPerson instanceof Caregiver)) {
            throw new CommandException(String.format(MESSAGE_NOT_CAREGIVER, caregiverIndex.getOneBased()));
        }

        Senior senior = (Senior) seniorPerson;
        Caregiver caregiver = (Caregiver) caregiverPerson;

        // Check if assigned correctly
        if (!(senior.hasCaregiver() && senior.getCaregiver().isSamePerson(caregiver))) {
            throw new CommandException(MESSAGE_NOT_ASSIGNED);
        }

        // Build updated senior with caregiver removed
        Senior updatedSenior = createSeniorWithoutCaregiver(senior);

        model.setPerson(senior, updatedSenior);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNASSIGN_SUCCESS,
                senior.getName(), caregiver.getName()));
    }

    /**
     * Creates and returns a {@code Senior} with the details of {@code senior}
     * with caregiver as null.
     */
    private Senior createSeniorWithoutCaregiver(Senior senior) {
        return new Senior(
                senior.getName(),
                senior.getPhone(),
                senior.getAddress(),
                senior.getRiskTags(),
                senior.getNote(),
                null
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnassignCommand)) {
            return false;
        }

        UnassignCommand otherUnassignCommand = (UnassignCommand) other;
        return seniorIndex.equals(otherUnassignCommand.seniorIndex)
                && caregiverIndex.equals(otherUnassignCommand.caregiverIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("seniorIndex", seniorIndex)
                .add("caregiverIndex", caregiverIndex)
                .toString();
    }
}
