package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_INVALID_CAREGIVER_INDEX;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_INVALID_SENIOR_INDEX;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;

/**
 * Edits the details of an existing senior or caregiver in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of a senior or caregiver using their unique index.\n"
            + "Specify exactly one person type (senior or caregiver).\n"
            + "Format: edit s/<SENIOR_INDEX> [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG] [nt/NOTE]...\n"
            + "        edit c/<CAREGIVER_INDEX> [n/NAME] [p/PHONE] [a/ADDRESS] [nt/NOTE]...\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " s/1 n/John Tan p/91234567\n"
            + "  " + COMMAND_WORD + " c/2 n/Jane Lim";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited %1$s: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This phone number "
            + "is already used by another person. Please amend your entry.";
    private static final Logger log = LogsCenter.getLogger(EditCommand.class);

    private final int index;
    private final EditPersonDescriptor editPersonDescriptor;
    private final boolean isSenior; // true if editing a Senior, false if editing a Caregiver

    /**
     * Creates an EditCommand to edit the person at the specified index.
     *
     * @param index                index of the person in the filtered list to edit
     * @param editPersonDescriptor details to edit the person with
     * @param isSenior             whether the person is a Senior (true) or a Caregiver (false)
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

        assert model.getFilteredSeniorList() != null
                : "Filtered senior list must not be null";
        assert model.getFilteredCaregiverList() != null
                : "Filtered caregiver list must not be null";
        assert model.getAllSeniorList() != null
                : "All senior list must not be null";
        assert model.getAllCaregiverList() != null
                : "All caregiver list must not be null";

        if (isSenior) {
            List<Senior> fullSeniorList = model.getAllSeniorList();

            CommandUtil.validateIndex(index, MESSAGE_INVALID_SENIOR_INDEX);

            // Find senior by seniorIndex
            Senior seniorToEdit = CommandUtil.findSeniorById(
                    fullSeniorList, index, MESSAGE_INVALID_SENIOR_INDEX);

            // Resolve caregiver ID to object if present
            if (editPersonDescriptor.getCaregiverId().isPresent()) {
                Integer caregiverId = editPersonDescriptor.getCaregiverId().get();
                Caregiver caregiver = model.getAllCaregiverList().stream()
                        .filter(c -> caregiverId.equals(c.getId()))
                        .findFirst()
                        .orElseThrow(() -> new CommandException("No caregiver found with ID " + caregiverId));
                editPersonDescriptor.setCaregiver(caregiver);
            }

            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED);
            }

            Phone maybeUpdatedPhone = editPersonDescriptor.getPhone().orElse(null);
            if (maybeUpdatedPhone != null
                    && (isPhoneInUseByOtherSenior(model, maybeUpdatedPhone, seniorToEdit.getId())
                    || isPhoneInUseByOtherCaregiver(model, maybeUpdatedPhone, null))) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            Senior editedSenior = seniorToEdit.edit(editPersonDescriptor);

            assert editedSenior != null
                    : "Edited senior must not be null";
            assert editedSenior.getId() != null
                    : "Edited senior must retain a non-null ID";
            assert editedSenior.getId().equals(seniorToEdit.getId())
                    : "Edit must not change the senior's ID";

            if (!seniorToEdit.getPhone().equals(editedSenior.getPhone())
                    && model.hasPhone(editedSenior.getPhone())) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }
            if (!seniorToEdit.isSamePerson(editedSenior) && model.hasPerson(editedSenior)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setSenior(seniorToEdit, editedSenior);
            model.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);
            model.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

            return new CommandResult(
                    String.format(MESSAGE_EDIT_PERSON_SUCCESS, "Senior", Messages.formatSenior(editedSenior)));

        } else {
            List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

            CommandUtil.validateIndex(index, MESSAGE_INVALID_CAREGIVER_INDEX);

            // Find senior by seniorIndex
            Caregiver caregiverToEdit = CommandUtil.findCaregiverById(
                    fullCaregiverList, index, MESSAGE_INVALID_CAREGIVER_INDEX);

            if (!editPersonDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED);
            }

            Phone maybeUpdatedPhone = editPersonDescriptor.getPhone().orElse(null);
            if (maybeUpdatedPhone != null
                    && (isPhoneInUseByOtherSenior(model, maybeUpdatedPhone, null)
                    || isPhoneInUseByOtherCaregiver(model, maybeUpdatedPhone, caregiverToEdit.getId()))) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            Caregiver editedCaregiver = caregiverToEdit.edit(editPersonDescriptor);

            assert editedCaregiver != null
                    : "Edited caregiver must not be null";
            assert editedCaregiver.getId() != null
                    : "Edited caregiver must retain a non-null ID";
            assert editedCaregiver.getId().equals(caregiverToEdit.getId())
                    : "Edit must not change the caregiver's ID";

            if (!caregiverToEdit.isSamePerson(editedCaregiver) && model.hasPerson(editedCaregiver)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            log.fine(() -> String.format("Edit caregiver: userIndex=%d (1-based)", index));
            log.fine(() -> "Before edit: id=" + caregiverToEdit.getId()
                    + ", name=" + caregiverToEdit.getName());

            model.setCaregiver(caregiverToEdit, editedCaregiver);

            log.info(() -> "Updated caregiver in model: id=" + editedCaregiver.getId());

            int targetId = caregiverToEdit.getId();
            int rebound = 0;
            for (Senior s : model.getAllSeniorList()) {
                if (s.getCaregiverId() != null && s.getCaregiverId().equals(targetId)) {
                    Senior updated = s.withCaregiver(editedCaregiver);
                    model.setSenior(s, updated);
                    rebound++;
                }
            }

            log.info("Rebound " + rebound + " seniors to caregiverId=" + targetId);

            model.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);
            model.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

            return new CommandResult(
                    String.format(MESSAGE_EDIT_PERSON_SUCCESS, "Caregiver", Messages.formatCaregiver(editedCaregiver)));
        }
    }

    private static boolean isPhoneInUseByOtherSenior(Model model, Phone phone, Integer ignoreSeniorId) {
        return model.getAllSeniorList().stream()
                .anyMatch(senior -> !Objects.equals(senior.getId(), ignoreSeniorId)
                        && senior.getPhone().equals(phone));
    }

    private static boolean isPhoneInUseByOtherCaregiver(Model model, Phone phone, Integer ignoreCaregiverId) {
        return model.getAllCaregiverList().stream()
                .anyMatch(caregiver -> !Objects.equals(caregiver.getId(), ignoreCaregiverId)
                        && caregiver.getPhone().equals(phone));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EditCommand)
                && index == ((EditCommand) other).index
                && editPersonDescriptor.equals(((EditCommand) other).editPersonDescriptor)
                && isSenior == ((EditCommand) other).isSenior;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }
}
