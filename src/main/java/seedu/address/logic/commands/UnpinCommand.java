package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Unpins whoever is currently pinned. Takes no arguments.
 * <p>
 * Usage: {@code unpin}
 */
public class UnpinCommand extends Command {

    public static final String COMMAND_WORD = "unpin";

    /**
     * scope selector
     */
    public enum Scope { SENIOR, CAREGIVER, BOTH }

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unpins pinned person(s).\n"
            + "Parameters: [s|senior | c|caregiver | all]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + "\n"
            + "  " + COMMAND_WORD + " s\n"
            + "  " + COMMAND_WORD + " c";

    public static final String MESSAGE_SUCCESS_ANY = "Unpinned pinned person(s).";
    public static final String MESSAGE_SUCCESS_SENIOR = "Unpinned pinned senior.";
    public static final String MESSAGE_SUCCESS_CAREGIVER = "Unpinned pinned caregiver.";
    public static final String MESSAGE_NOTHING_PINNED = "No one is pinned.";
    public static final String MESSAGE_NOTHING_PINNED_SENIOR = "No pinned senior.";
    public static final String MESSAGE_NOTHING_PINNED_CAREGIVER = "No pinned caregiver.";

    private final Scope scope;

    // keep a no-arg ctor if something else calls it; default to BOTH
    public UnpinCommand() {
        this(Scope.BOTH);
    }

    public UnpinCommand(Scope scope) {
        this.scope = requireNonNull(scope);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        int count = 0;

        // Unpin seniors if scope is BOTH or SENIOR
        if (scope != Scope.CAREGIVER) {
            for (Senior s : fullSeniorList) {
                if (s.getPinned()) {
                    model.setSenior(s, s.withPinned(false));
                    count++;
                }
            }
        }

        // Unpin caregivers if scope is BOTH or CAREGIVER
        if (scope != Scope.SENIOR) {
            for (Caregiver c : fullCaregiverList) {
                if (c.getPinned()) {
                    model.setCaregiver(c, c.withPinned(false));
                    count++;
                }
            }
        }

        if (count == 0) {
            switch (scope) {
            case SENIOR:
                throw new CommandException(MESSAGE_NOTHING_PINNED_SENIOR);
            case CAREGIVER:
                throw new CommandException(MESSAGE_NOTHING_PINNED_CAREGIVER);
            default:
                throw new CommandException(MESSAGE_NOTHING_PINNED);
            }
        }

        model.updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);

        switch (scope) {
        case SENIOR:
            return new CommandResult(MESSAGE_SUCCESS_SENIOR);
        case CAREGIVER:
            return new CommandResult(MESSAGE_SUCCESS_CAREGIVER);
        default:
            return new CommandResult(MESSAGE_SUCCESS_ANY);
        }
    }


    @Override
    public boolean equals(Object o) {
        return (o instanceof UnpinCommand) && ((UnpinCommand) o).scope == this.scope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope);
    }
}
