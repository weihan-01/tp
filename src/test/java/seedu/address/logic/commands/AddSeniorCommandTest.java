package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.ID_THIRTEEN_PERSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;
import seedu.address.testutil.CaregiverBuilder;
import seedu.address.testutil.SeniorBuilder;

/**
 * Integration and unit tests for {@link AddSeniorCommand}.
 */
public class AddSeniorCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newSenior_success() {
        Senior newSenior = new SeniorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withRiskTag(VALID_TAG_AMY)
                .withNote("").build();

        AddSeniorCommand command = new AddSeniorCommand(newSenior, null);
        String expectedMessage = String.format(AddSeniorCommand.MESSAGE_SUCCESS, Messages.formatSenior(newSenior));

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addSenior(newSenior.withId(1));
        expectedModel.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_withCaregiver_success() {
        Caregiver caregiver = new CaregiverBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();
        model.addCaregiver(caregiver.withId(1));

        Senior senior = new SeniorBuilder().withName(VALID_NAME_CHARLES).withPhone(VALID_PHONE_CHARLES)
                .withAddress(VALID_ADDRESS_CHARLES).withRiskTag(VALID_TAG_CHARLES).build();
        AddSeniorCommand command = new AddSeniorCommand(senior, 1);

        String expectedMessage = String.format(AddSeniorCommand.MESSAGE_SUCCESS, Messages.formatSenior(senior));
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addCaregiver(caregiver.withId(1));
        expectedModel.addSenior(senior.withId(1).withCaregiver(caregiver.withId(1)));
        expectedModel.updateFilteredCaregiverList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredSeniorList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePhone_throwsCommandException() {
        Senior existing = new Senior(
                new Name(VALID_NAME_CHARLES),
                new Phone(VALID_PHONE_CHARLES),
                new Address(VALID_ADDRESS_CHARLES),
                new Tag(VALID_TAG_CHARLES), new Note(""),
                null, 1, false);
        model.addSenior(existing);

        Senior duplicatePhone = new SeniorBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_CHARLES)
                .withAddress(VALID_ADDRESS_BOB).withRiskTag(VALID_TAG_BOB).build();
        AddSeniorCommand command = new AddSeniorCommand(duplicatePhone, null);
        assertCommandFailure(command, model, AddSeniorCommand.MESSAGE_DUPLICATE_SENIOR);
    }

    @Test
    public void execute_caregiverDoesNotExist_throwsCommandException() {
        Senior senior = new SeniorBuilder().withName(VALID_NAME_CHARLES).withPhone(VALID_PHONE_CHARLES)
                .withAddress(VALID_ADDRESS_CHARLES).withRiskTag(VALID_TAG_CHARLES).build();
        AddSeniorCommand command = new AddSeniorCommand(senior, ID_THIRTEEN_PERSON);
        assertCommandFailure(command, model, String.format(
                AddSeniorCommand.MESSAGE_NO_SUCH_CAREGIVER,
                ID_THIRTEEN_PERSON));
    }

    @Test
    public void equals() {
        Senior seniorAmy = new SeniorBuilder()
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY).withRiskTag(VALID_TAG_AMY).build();
        Senior seniorBob = new SeniorBuilder()
                .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).withRiskTag(VALID_TAG_BOB).build();
        AddSeniorCommand addSeniorCommandAmy = new AddSeniorCommand(seniorAmy, null);
        AddSeniorCommand addSeniorCommandBob = new AddSeniorCommand(seniorBob, null);

        assertTrue(addSeniorCommandAmy.equals(addSeniorCommandAmy));
        assertTrue(addSeniorCommandAmy.equals(new AddSeniorCommand(seniorAmy, null)));
        assertFalse(addSeniorCommandAmy.equals(null));
        assertFalse(addSeniorCommandAmy.equals("str"));
        assertFalse(addSeniorCommandAmy.equals(addSeniorCommandBob));
    }
}
