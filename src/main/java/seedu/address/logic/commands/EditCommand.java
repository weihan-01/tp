package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Edits the details of an existing senior or caregiver in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the person identified by the senior or caregiver index.\n"
            + "Specify one type of person only.\n"
            + "Example: edit s/1 n/John Tan p/91234567\n"
            + "Example: edit c/2 n/Jane Lim";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited %1$s: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid.";

    private final int index;
    private final EditPersonDescriptor editPersonDescriptor;
    private final boolean isSenior; // true if editing a Senior, false if editing a Caregiver

    /**
     * Creates an EditCommand to edit the person at the specified index.
     *
     * @param index index of the person in the filtered list to edit
     * @param editPersonDescriptor details to edit the person with
     * @param isSenior whether the person is a Senior (true) or a Caregiver (false)
     */
    public EditCommand(int index, EditPersonDescriptor editPersonDescriptor, boolean isSenior) {
        requireNonNull(editPersonDescriptor);
        this.index = index;
        this.editPersonDescriptor = editPersonDescriptor;
        this.isSenior = isSenior;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isSenior) {
            List<Senior> lastShownList = model.getFilteredSeniorList();

            if (index < 0 || index >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_INDEX);
            }

            Senior seniorToEdit = lastShownList.get(index);

            // Resolve caregiver ID to object if present
            if (editPersonDescriptor.getCaregiverId().isPresent()) {
                Integer caregiverId = editPersonDescriptor.getCaregiverId().get();
                Caregiver caregiver = model.getAllCaregiverList().stream()
                        .filter(c -> caregiverId.equals(c.getCaregiverId()))
                        .findFirst()
                        .orElseThrow(() -> new CommandException("No caregiver found with ID " + caregiverId));
                editPersonDescriptor.setCaregiver(caregiver);
            }

            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED);
            }

            Senior editedSenior = seniorToEdit.edit(editPersonDescriptor);

            if (!seniorToEdit.isSamePerson(editedSenior) && model.hasPerson(editedSenior)) {
                throw new CommandException("This senior already exists in the address book.");
            }

            model.setSenior(seniorToEdit, editedSenior);
            model.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

            return new CommandResult(
                    String.format(MESSAGE_EDIT_PERSON_SUCCESS, "Senior", Messages.format(editedSenior)));

        } else {
            List<Caregiver> lastShownList = model.getFilteredCaregiverList();
            if (index < 0 || index >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_INDEX);
            }

            Caregiver caregiverToEdit = lastShownList.get(index);

            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED);
            }

            Caregiver editedCaregiver = caregiverToEdit.edit(editPersonDescriptor);

            if (!caregiverToEdit.isSamePerson(editedCaregiver) && model.hasPerson(editedCaregiver)) {
                throw new CommandException("This caregiver already exists in the address book.");
            }

            model.setCaregiver(caregiverToEdit, editedCaregiver);
            model.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);

            return new CommandResult(
                    String.format(MESSAGE_EDIT_PERSON_SUCCESS, "Caregiver", Messages.format(editedCaregiver)));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EditCommand)
                && index == ((EditCommand) other).index
                && editPersonDescriptor.equals(((EditCommand) other).editPersonDescriptor)
                && isSenior == ((EditCommand) other).isSenior;
    }
}
