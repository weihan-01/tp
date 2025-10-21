package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;


/**
 * Finds and lists all seniors in address book whose risk tag is HR / MR / LR (case-insensitive)
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds and lists all seniors in address book "
            + "whose risk tag is HR / MR / LR (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Valid risk tags: hr (High Risk), mr (Medium Risk), lr (Low Risk)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "hr";

    public static final String MESSAGE_NO_TAGS = "Please provide at least one tag.\n" + MESSAGE_USAGE;

    public static final String MESSAGE_SUCCESS = "Filtered list: %d person(s) shown.";

    private final Predicate<Person> predicate;

    public FilterCommand(Predicate<Person> predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FilterCommand && predicate.equals(((FilterCommand) other).predicate));
    }
}
