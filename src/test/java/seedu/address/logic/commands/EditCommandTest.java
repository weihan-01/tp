package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.ID_ONE_PERSON;
import static seedu.address.testutil.TypicalIndexes.ID_TWO_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;
import seedu.address.testutil.CaregiverBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.SeniorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Senior editedSenior = new SeniorBuilder()
                .withName("Amy Dee")
                .withRiskTag("HR")
                .withPhone("98510292")
                .withAddress("456, Jurong West Ave 10")
                .withNote("Changed her NRIC")
                .withPinned(true)
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedSenior).build();
        EditCommand editCommand = new EditCommand(ID_ONE_PERSON, descriptor, true);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                "Senior", Messages.formatSenior(editedSenior));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setSenior(model.getFilteredSeniorList().get(0), editedSenior);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Caregiver lastCaregiver = model.getCaregiverWithId(ID_ONE_PERSON);

        CaregiverBuilder caregiverInList = new CaregiverBuilder(lastCaregiver);
        Caregiver editedCaregiver = caregiverInList
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .build();
        EditCommand editCommand = new EditCommand(
                ID_ONE_PERSON, descriptor, false);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                "Caregiver", Messages.formatCaregiver(editedCaregiver));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setCaregiver(lastCaregiver, editedCaregiver);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        EditCommand editCommand = new EditCommand(
                ID_ONE_PERSON, new EditPersonDescriptor(), false);
        Caregiver editedCaregiver = model.getCaregiverWithId(ID_ONE_PERSON);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                "Caregiver", Messages.formatCaregiver(editedCaregiver));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        // edit person in list into a duplicate in address book
        Senior firstSenior = model.getAllSeniorList().get(ID_TWO_PERSON);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstSenior).build();
        EditCommand editCommand = new EditCommand(ID_ONE_PERSON, descriptor, true);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        // edit person in filtered list into a duplicate in address book
        Senior seniorInList = model.getFilteredSeniorList().get(ID_TWO_PERSON);
        EditCommand editCommand = new EditCommand(ID_ONE_PERSON,
                new EditPersonDescriptorBuilder(seniorInList).build(), true);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Integer outOfBoundIndex = model.getFilteredSeniorList().size() + 1;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor, true);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        Integer outOfBoundIndex = ID_TWO_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex < model.getAddressBook().getSeniorList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), true);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(ID_ONE_PERSON, DESC_AMY, true);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(ID_ONE_PERSON, copyDescriptor, true);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(ID_TWO_PERSON, DESC_AMY, true)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(ID_ONE_PERSON, DESC_BOB, true)));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(ID_ONE_PERSON, editPersonDescriptor, true);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + ID_ONE_PERSON + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
