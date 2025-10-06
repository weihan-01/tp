package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;

/**
 * Adds a Caregiver to the address book.
 * Command word: {@code add-cgr}
 * Format: {@code add-cgr n/NAME p/PHONE [a/ADDRESS] [r/NOTES]}
 */
public class AddCaregiverCommand extends Command {

    public static final String COMMAND_WORD = "add-cgr";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a caregiver to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_NOTE + "NOTES]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Mei Hui "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_ADDRESS + "Blk 620 Punggol Field Rd #02-45 "
            + PREFIX_NOTE + "Has experience with dementia caregiving";

    public static final String MESSAGE_SUCCESS = "New caregiver added: %1$s";
    public static final String MESSAGE_DUPLICATE_CAREGIVER =
            "Caregiver already exists. Please amend your entry.";

    // Store parsed fields; build the Caregiver with ID in execute()
    private final Name name;
    private final Phone phone;
    private final Address address;
    private final Note note;

    /**
     * Creates an AddCaregiverCommand to add the specified {@code Caregiver}
     */
    public AddCaregiverCommand(Name name, Phone phone, Address address, Note note) {
        requireNonNull(name);
        requireNonNull(phone);
        requireNonNull(address);
        requireNonNull(note);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Always allocate a fresh caregiver id (e.g., "c10")
        final String caregiverId = model.allocateCaregiverId();

        // Build the final caregiver WITH id
        Caregiver created = new Caregiver(name, phone, address, note, caregiverId);

        // Duplicate rule: same name + same phone
        if (model.hasPerson(created)) {
            throw new CommandException(MESSAGE_DUPLICATE_CAREGIVER);
        }

        model.addPerson(created);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(created)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddCaregiverCommand)) {
            return false;
        }
        AddCaregiverCommand o = (AddCaregiverCommand) other;
        return name.equals(o.name)
                && phone.equals(o.phone)
                && address.equals(o.address)
                && note.equals(o.note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("note", note)
                .toString();
    }
}
