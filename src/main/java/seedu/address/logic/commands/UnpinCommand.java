package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.PinCommand.cloneWithNote;
import static seedu.address.logic.commands.PinCommand.isPinned;
import static seedu.address.logic.commands.PinCommand.stripPinned;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

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
        List<Person> all = getAllPersons(model);

        int count = 0;
        for (Person p : all) {
            if (isPinned(p)) {
                Person unpinned = cloneWithNote(p, stripPinned(p.getNote()));
                model.setPerson(p, unpinned);
                count++;
            }
        }

        if (count == 0) {
            throw new CommandException(MESSAGE_NOTHING_PINNED);
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS_ANY);
    }

    private static List<Person> getAllPersons(Model model) {
        try {
            return new ArrayList<>(model.getAddressBook().getPersonList());
        } catch (Throwable t) {
            return new ArrayList<>(model.getFilteredPersonList());
        }
    }

}
