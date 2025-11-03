package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.ID_NEGATIVE_INVALID_PERSON;
import static seedu.address.testutil.TypicalIndexes.ID_ONE_PERSON;
import static seedu.address.testutil.TypicalIndexes.ID_THIRTEEN_PERSON;
import static seedu.address.testutil.TypicalIndexes.ID_TWO_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;
import seedu.address.testutil.TestUtil;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Caregiver caregiverToDelete = model.getCaregiverWithId(ID_ONE_PERSON);
        DeleteCommand deleteCommand = new DeleteCommand(null, ID_ONE_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatCaregiver(caregiverToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteCaregiver(caregiverToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Integer outOfBoundIndex = model.getAllSeniorList().size() + 1;
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, null);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_validSeniorIndex_success() {
        // Grab a real senior by id 1
        Senior seniorToDelete = TestUtil.getSenior(model, ID_ONE_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(ID_ONE_PERSON, null);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatSenior(seniorToDelete));

        // Build expected model and message
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteSenior(seniorToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNegativeSeniorIndex_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(ID_NEGATIVE_INVALID_PERSON, null);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_invalidPositiveSeniorIndex_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(ID_THIRTEEN_PERSON, null);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_validCaregiverIndex_success() {
        // Grab a real caregiver by id 1
        Caregiver caregiverToDelete = TestUtil.getCaregiver(model, ID_ONE_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(null, ID_ONE_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatCaregiver(caregiverToDelete));

        // Build expected model and message
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteCaregiver(caregiverToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNegativeCaregiverIndex_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(null, ID_NEGATIVE_INVALID_PERSON);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void execute_invalidPositiveCaregiverIndex_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(null, ID_THIRTEEN_PERSON);
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        Caregiver caregiverToDelete = model.getCaregiverWithId(ID_ONE_PERSON);
        DeleteCommand deleteCommand = new DeleteCommand(null, ID_ONE_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatCaregiver(caregiverToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteCaregiver(caregiverToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        int outOfBoundIndex = ID_TWO_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex < model.getAddressBook().getSeniorList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, null);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(ID_ONE_PERSON, null);
        DeleteCommand deleteSecondCommand = new DeleteCommand(ID_TWO_PERSON, null);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(ID_ONE_PERSON, null);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Integer seniorTargetIndex = ID_ONE_PERSON;
        Integer caregiverTargetIndex = null;
        DeleteCommand deleteCommand = new DeleteCommand(seniorTargetIndex, caregiverTargetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{seniorIndex=" + seniorTargetIndex
                + ", caregiverIndex=" + caregiverTargetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredSeniorList(p -> false);
        assertTrue(model.getFilteredSeniorList().isEmpty());
    }
}
