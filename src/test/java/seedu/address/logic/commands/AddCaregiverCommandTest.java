package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.testutil.CaregiverBuilder;
import seedu.address.testutil.TypicalPersons;

/**
 * Integration and unit tests for {@link AddCaregiverCommand}.
 */
public class AddCaregiverCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newCaregiver_success() {
        Caregiver newCaregiver = new CaregiverBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withNote("").build();

        AddCaregiverCommand command = new AddCaregiverCommand(newCaregiver);

        String expectedMessage = String.format(
                AddCaregiverCommand.MESSAGE_SUCCESS,
                Messages.formatCaregiver(newCaregiver));

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addCaregiver(newCaregiver.withId(1));
        expectedModel.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePhone_throwsCommandException() {
        model.addCaregiver(TypicalPersons.BENSON.withId(1));

        Caregiver duplicate = new Caregiver(new Name(TypicalPersons.BENSON.getName().fullName),
                new Phone(TypicalPersons.BENSON.getPhone().value),
                TypicalPersons.BENSON.getAddress(), new Note(""), null, false);
        AddCaregiverCommand command = new AddCaregiverCommand(duplicate);
        assertCommandFailure(command, model, AddCaregiverCommand.MESSAGE_DUPLICATE_CAREGIVER);
    }

    @Test
    public void equals() {
        Caregiver caregiverAmy = new CaregiverBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).build();
        Caregiver caregiverBob = new CaregiverBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();
        AddCaregiverCommand caregiverCommandAmy = new AddCaregiverCommand(caregiverAmy);
        AddCaregiverCommand caregiverCommandBob = new AddCaregiverCommand(caregiverBob);

        // same object -> true
        assertTrue(caregiverCommandAmy.equals(caregiverCommandAmy));
        // same values -> true
        assertTrue(caregiverCommandAmy.equals(new AddCaregiverCommand(caregiverAmy)));
        // null -> false
        assertFalse(caregiverCommandAmy.equals(null));
        // different type -> false
        assertFalse(caregiverCommandAmy.equals("str"));
        // different -> false
        assertFalse(caregiverCommandAmy.equals(caregiverCommandBob));
    }
}
