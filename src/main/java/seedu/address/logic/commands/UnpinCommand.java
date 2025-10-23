package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.PinCommand.cloneWithNote;
import static seedu.address.logic.commands.PinCommand.isPinned;
import static seedu.address.logic.commands.PinCommand.stripPinned;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Unpins whoever is currently pinned. Takes no arguments.
 *
 * Usage: {@code unpin}
 */
public class UnpinCommand extends Command {

    public static final String COMMAND_WORD = "unpin";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unpins the pinned person.\n"
            + "No arguments allowed.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS_ANY = "Unpinned.";
    public static final String MESSAGE_NOTHING_PINNED = "No one is pinned.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        int count = 0;
        for (Senior s : fullSeniorList) {
            if (isPinned(s)) {
                Person unpinned = cloneWithNote(s, stripPinned(s.getNote()));
                model.setSenior(s, (Senior) unpinned);
                count++;
            }
        }

        for (Caregiver c : fullCaregiverList) {
            if (isPinned(c)) {
                Person unpinned = cloneWithNote(c, stripPinned(c.getNote()));
                model.setCaregiver(c, (Caregiver) unpinned);
                count++;
            }
        }

        if (count == 0) {
            throw new CommandException(MESSAGE_NOTHING_PINNED);
        }

        model.updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS_ANY);
    }
}
