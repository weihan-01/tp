package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Elderly;
import seedu.address.model.person.Person;

/**
 * Assigns a caregiver to a senior in the address book.
 */
public class AssignCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Assigns a caregiver to a senior identified by their index numbers "
            + "in the displayed person list.\n"
            + "Parameters: "
            + PREFIX_SENIOR + "SENIOR_INDEX "
            + PREFIX_CAREGIVER + "CAREGIVER_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SENIOR + "1 "
            + PREFIX_CAREGIVER + "3";

    public static final String MESSAGE_ASSIGN_SUCCESS = "Senior %1$s has been assigned to Caregiver %2$s";
    public static final String MESSAGE_INVALID_SENIOR_INDEX =
            "No such senior index exists. Please ensure the index matches a senior from the database.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX =
            "No such caregiver index exists. Please ensure the index matches a caregiver from the database.";
    public static final String MESSAGE_NOT_SENIOR = "Person at index %1$d is not a senior";
    public static final String MESSAGE_NOT_CAREGIVER = "Person at index %1$d is not a caregiver";
    public static final String MESSAGE_ALREADY_ASSIGNED =
            "This caregiver is already assigned to this senior";

    private final Index seniorIndex;
    private final Index caregiverIndex;

    /**
     * Creates an AssignCommand to assign the specified {@code Caregiver} to the specified {@code Elderly}
     *
     * @param seniorIndex Index of the senior in the filtered person list
     * @param caregiverIndex Index of the caregiver in the filtered person list
     */
    public AssignCommand(Index seniorIndex, Index caregiverIndex) {
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
        if (!(seniorPerson instanceof Elderly)) {
            throw new CommandException(String.format(MESSAGE_NOT_SENIOR,
                    seniorIndex.getOneBased()));
        }

        if (!(caregiverPerson instanceof Caregiver)) {
            throw new CommandException(String.format(MESSAGE_NOT_CAREGIVER,
                    caregiverIndex.getOneBased()));
        }

        Elderly senior = (Elderly) seniorPerson;
        Caregiver caregiver = (Caregiver) caregiverPerson;

        // Check if already assigned (uncomment when Elderly has getCaregiver method)
        // if (senior.getCaregiver() != null && senior.getCaregiver().isSamePerson(caregiver)) {
        //     throw new CommandException(MESSAGE_ALREADY_ASSIGNED);
        // }

        // Create updated senior with assigned caregiver
        Elderly updatedSenior = createSeniorWithCaregiver(senior, caregiver);

        model.setPerson(senior, updatedSenior);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_ASSIGN_SUCCESS,
                senior.getName(), caregiver.getName()));
    }

    /**
     * Creates and returns an {@code Elderly} with the details of {@code seniorToEdit}
     * and assigned with {@code caregiver}.
     */
    private Elderly createSeniorWithCaregiver(Elderly senior, Caregiver caregiver) {
        return new Elderly(
                senior.getName(),
                senior.getPhone(),
                senior.getEmail(),
                senior.getAddress(),
                senior.getRiskTags(),
                senior.getNote(),
                caregiver
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignCommand)) {
            return false;
        }

        AssignCommand otherAssignCommand = (AssignCommand) other;
        return seniorIndex.equals(otherAssignCommand.seniorIndex)
                && caregiverIndex.equals(otherAssignCommand.caregiverIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("seniorIndex", seniorIndex)
                .add("caregiverIndex", caregiverIndex)
                .toString();
    }
}