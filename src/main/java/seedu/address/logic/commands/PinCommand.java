package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.CommandUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Parses input arguments and creates a new {@link PinCommand}.
 * Expected format: {@code pin n/NAME}
 */
public class PinCommand extends Command {

    public static final String COMMAND_WORD = "pin";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Pins exactly one person.\n"
            + "Parameters: s/SENIOR_INDEX  |  c/CAREGIVER_INDEX\n"
            + "Examples: " + COMMAND_WORD + " s/5    OR    " + COMMAND_WORD + " c/3";

    public static final String MESSAGE_SUCCESS = "Pinned: %s";
    public static final String MESSAGE_ALREADY_PINNED = "%1$s is already pinned.";

    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_NO_PERSONS = "No such senior and caregiver exist.";

    //private static final String PIN_SENTINEL = "PINNED";

    private final Integer seniorIndex;
    private final Integer caregiverIndex;

    /**
     * Creates a {@code PinCommand} targeting a specific person by name.
     * <p>
     * The command will pin exactly the person whose name matches {@code seniorIndex}
     * (comparison is performed case-insensitively during execution).
     *
     * @param seniorIndex the id of the senior to pin;
     * @param caregiverIndex the id of the caregiver to pin;
     */
    public PinCommand(Integer seniorIndex, Integer caregiverIndex) {
        this.seniorIndex = seniorIndex;
        this.caregiverIndex = caregiverIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        // Find senior by seniorIndex
        Senior senior = CommandUtil.validateOptionalSeniorId(
                fullSeniorList, seniorIndex, MESSAGE_INVALID_SENIOR_INDEX);

        // Find caregiver by caregiverIndex
        Caregiver caregiver = CommandUtil.validateOptionalCaregiverId(
                fullCaregiverList, caregiverIndex, MESSAGE_INVALID_CAREGIVER_INDEX);

        // Already pinned? (use boolean flag)
        if (senior != null && senior.isPinned()) {
            return new CommandResult(String.format(MESSAGE_ALREADY_PINNED, senior.getName()));
        }
        if (caregiver != null && caregiver.isPinned()) {
            return new CommandResult(String.format(MESSAGE_ALREADY_PINNED, caregiver.getName()));
        }

        if (senior != null) {
            // unpin any existing senior
            for (Senior s : fullSeniorList) {
                if (s.isPinned()) {
                    model.setSenior(s, s.withPinned(false));
                }
            }
            model.setSenior(senior, senior.withPinned(true));
        }

        if (caregiver != null) {
            // unpin any existing caregiver
            for (Caregiver c : fullCaregiverList) {
                if (c.isPinned()) {
                    model.setCaregiver(c, c.withPinned(false));
                }
            }
            model.setCaregiver(caregiver, caregiver.withPinned(true));
        }

        model.updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);

        if (senior != null && caregiver == null) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatSenior(senior)));
        } else if (senior == null && caregiver != null) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatCaregiver(caregiver)));
        } else if (senior != null && caregiver != null) { // && (bugfix from &)
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS, Messages.formatSenior(senior)) + " and "
                            + String.format(MESSAGE_SUCCESS, Messages.formatCaregiver(caregiver)));
        } else {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PinCommand)) {
            return false;
        }
        PinCommand o = (PinCommand) other;
        return Objects.equals(this.seniorIndex, o.seniorIndex)
                && Objects.equals(this.caregiverIndex, o.caregiverIndex);
    }

    @Override
    public String toString() {
        String which = (seniorIndex != null)
                ? ("seniorId=" + seniorIndex)
                : ("caregiverId=" + caregiverIndex);
        return getClass().getCanonicalName() + "{" + which + "}";
    }
}
