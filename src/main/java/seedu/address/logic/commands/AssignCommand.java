package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

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
    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_ALREADY_ASSIGNED = "This caregiver is already assigned to this senior";

    private final Integer seniorIndex;
    private final Integer caregiverIndex;

    /**
     * Creates an AssignCommand to assign the specified {@code Caregiver} to the specified {@code Senior}
     *
     * @param seniorIndex    Index of the senior in the filtered person list
     * @param caregiverIndex Index of the caregiver in the filtered person list
     */
    public AssignCommand(Integer seniorIndex, Integer caregiverIndex) {
        requireAllNonNull(seniorIndex, caregiverIndex);
        this.seniorIndex = seniorIndex;
        this.caregiverIndex = caregiverIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        // Validate caregiver and senior index
        CommandUtil.validateIndex(seniorIndex, MESSAGE_INVALID_SENIOR_INDEX);
        CommandUtil.validateIndex(caregiverIndex, MESSAGE_INVALID_CAREGIVER_INDEX);


        // Find senior by seniorIndex
        Senior senior = CommandUtil.findSeniorById(
                fullSeniorList, seniorIndex, MESSAGE_INVALID_SENIOR_INDEX);


        // Find caregiver by caregiverIndex
        Caregiver caregiver = CommandUtil.findCaregiverById(
                fullCaregiverList, caregiverIndex, MESSAGE_INVALID_CAREGIVER_INDEX);


        // Check if already assigned
        if (senior.hasCaregiver() && senior.getCaregiver().isSamePerson(caregiver)) {
            throw new CommandException(MESSAGE_ALREADY_ASSIGNED);
        }

        // Create updated senior with assigned caregiver
        Senior updatedSenior = createSeniorWithCaregiver(senior, caregiver);

        model.setSenior(senior, updatedSenior);
        model.updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_ASSIGN_SUCCESS,
                senior.getName(), caregiver.getName()));
    }

    /**
     * Creates and returns a {@code Senior} with the details of {@code senior}
     * and assigned with {@code caregiver}.
     */
    private Senior createSeniorWithCaregiver(Senior senior, Caregiver caregiver) {
        return new Senior(
                senior.getName(),
                senior.getPhone(),
                senior.getAddress(),
                senior.getRiskTag(),
                senior.getNote(),
                caregiver,
                senior.getId(),
                senior.getPinned()
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
