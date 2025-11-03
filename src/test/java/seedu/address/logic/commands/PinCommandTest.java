package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Tests for {@link PinCommand} using CommandTestUtil's assert helpers,
 * mirroring FilterCommandTest setup & style.
 */
public class PinCommandTest {

    private Model model;
    private Model expectedModel;

    private Senior firstSenior;
    private Caregiver firstCaregiver;

    @BeforeEach
    public void setUp() {
        AddressBook ab = getTypicalAddressBook();
        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());

        List<Senior> seniors = model.getFilteredSeniorList();
        if (!seniors.isEmpty()) {
            firstSenior = seniors.get(0);
        }
        List<Caregiver> caregivers = model.getFilteredCaregiverList();
        if (!caregivers.isEmpty()) {
            firstCaregiver = caregivers.get(0);
        }
    }

    // Test success paths

    @Test
    public void execute_pinSenior_success() throws Exception {
        Integer id = firstSenior.getId();
        PinCommand command = new PinCommand(id, null);

        String expectedMessage = String.format(PinCommand.MESSAGE_SUCCESS, Messages.formatSenior(firstSenior));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_pinCaregiver_success() throws Exception {
        Integer id = firstCaregiver.getId();
        PinCommand command = new PinCommand(null, id);

        String expectedMessage = String.format(PinCommand.MESSAGE_SUCCESS, Messages.formatCaregiver(firstCaregiver));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // Test seniors and caregiver that has already been pinned
    @Test
    public void execute_pinAlreadyPinnedSenior_usesAlreadyPinnedMessage() throws Exception {
        // Ensure both models are in pinned state, pin the first senior
        Integer id = firstSenior.getId();
        if (!firstSenior.getPinned()) {
            new PinCommand(id, null).execute(model);
        }
        // Mirror on expectedModel so both are equal pre-assert
        new PinCommand(id, null).execute(expectedModel);

        // Re-pin senior
        PinCommand command = new PinCommand(id, null);
        String expectedMessage = String.format(PinCommand.MESSAGE_ALREADY_PINNED,
                model.getFilteredSeniorList().stream()
                        .filter(s -> id.equals(s.getId()))
                        .findFirst().orElseThrow().getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_pinAlreadyPinnedCaregiver_usesAlreadyPinnedMessage() throws Exception {
        // Ensure both models are in pinned state, pin the first caregiver
        Integer id = firstCaregiver.getId();
        if (!firstCaregiver.getPinned()) {
            new PinCommand(null, id).execute(model);
        }
        new PinCommand(null, id).execute(expectedModel);

        PinCommand command = new PinCommand(null, id);
        String expectedMessage = String.format(PinCommand.MESSAGE_ALREADY_PINNED,
                model.getFilteredCaregiverList().stream()
                        .filter(c -> id.equals(c.getId()))
                        .findFirst().orElseThrow().getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // Test invalid indexes
    @Test
    public void execute_invalidSeniorIndex_throwsCommandException() {
        Integer invalidId = 999999;
        PinCommand command = new PinCommand(invalidId, null);

        assertCommandFailure(command, model, PinCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_invalidCaregiverIndex_throwsCommandException() {
        Integer invalidId = 999999;
        PinCommand command = new PinCommand(null, invalidId);

        assertCommandFailure(command, model, PinCommand.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void equals_sameIds_true() {
        PinCommand a = new PinCommand(1, null);
        PinCommand b = new PinCommand(1, null);
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentIds_false() {
        PinCommand pinSnr1 = new PinCommand(1, null);
        PinCommand pinSnr2 = new PinCommand(2, null);
        PinCommand pinCgr1 = new PinCommand(null, 1);
        PinCommand pinCgr2 = new PinCommand(null, 2);
        assertFalse(pinSnr1.equals(pinSnr2));
        assertFalse(pinCgr1.equals(pinCgr2));
        assertFalse(pinSnr1.equals(pinCgr1));
    }

    @Test
    public void toStringMethod() {
        PinCommand pinSenior = new PinCommand(5, null);
        PinCommand pinCaregiver = new PinCommand(null, 3);
        String expectedSenior = PinCommand.class.getCanonicalName() + "{seniorId=5}";
        String expectedCaregiver = PinCommand.class.getCanonicalName() + "{caregiverId=3}";
        assertEquals(expectedSenior, pinSenior.toString());
        assertEquals(expectedCaregiver, pinCaregiver.toString());
    }

}
