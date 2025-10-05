package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;

/**
 * Adds a Caregiver to the address book.
 * Command word: {@code add-cgr}
 * Format: {@code add-cgr n/NAME p/PHONE e/EMAIL [a/ADDRESS] [n/NOTES]}
 */
public class AddCaregiverCommand extends Command {

    public static final String COMMAND_WORD = "add-cgr";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a caregiver to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_NOTE + "NOTES]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Mei Hui "
            + PREFIX_PHONE + "98765432  "
            + PREFIX_EMAIL + "meihui@example.com "
            + PREFIX_ADDRESS + "Blk 620 Punggol Field Rd #02-45 "
            + PREFIX_NOTE + "Has experience with dementia caregiving";

    public static final String MESSAGE_SUCCESS = "New caregiver added: %1$s";
    public static final String MESSAGE_DUPLICATE_CAREGIVER = "Caregiver already exists. Please amend your entry.";

    private final Caregiver toAdd;

    /**
     * Creates an AddCaregiverCommand to add the specified {@code Caregiver}.
     */
    public AddCaregiverCommand(Caregiver caregiver) {
        requireNonNull(caregiver);
        toAdd = caregiver;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CAREGIVER);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCaregiverCommand)) {
            return false;
        }

        AddCaregiverCommand otherAddCaregiverCommand = (AddCaregiverCommand) other;
        return toAdd.equals(otherAddCaregiverCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

}
