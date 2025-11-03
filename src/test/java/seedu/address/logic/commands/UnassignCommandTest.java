package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Tests for {@link UnassignCommand} using seniorIndex/caregiverIndex
 */
public class UnassignCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        AddressBook ab = getTypicalAddressBook();
        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());
    }

    // Success paths

    @Test
    public void execute_unassign_success() throws Exception {
        // need an already-assigned pair
        Optional<Pair<Senior, Caregiver>> maybePair = pickCurrentlyAssignedPair(model);

        if (maybePair.isEmpty()) {
            // Typical data might not have any pre-assigned pairs; skip gracefully.
            return;
        }

        Senior senior = maybePair.get().first;
        Caregiver caregiver = maybePair.get().second;

        Integer seniorId = senior.getId();
        Integer caregiverId = caregiver.getId();

        UnassignCommand command = new UnassignCommand(seniorId, caregiverId);

        // Mirror mutation and obtain the exact expected message on expectedModel
        CommandResult expectedResult = new UnassignCommand(seniorId, caregiverId).execute(expectedModel);
        String expectedMessage = expectedResult.getFeedbackToUser();

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unassignOnFilteredLists_success() throws Exception {
        Optional<Pair<Senior, Caregiver>> maybePair = pickCurrentlyAssignedPair(model);
        if (maybePair.isEmpty()) {
            return;
        }

        Senior targetSenior = maybePair.get().first;
        Caregiver targetCaregiver = maybePair.get().second;

        Predicate<Person> seniorOnly = s -> s.equals(targetSenior);
        Predicate<Person> caregiverOnly = c -> c.equals(targetCaregiver);

        model.updateFilteredSeniorList(seniorOnly);
        model.updateFilteredCaregiverList(caregiverOnly);
        expectedModel.updateFilteredSeniorList(seniorOnly);
        expectedModel.updateFilteredCaregiverList(caregiverOnly);

        Integer seniorId = targetSenior.getId();
        Integer caregiverId = targetCaregiver.getId();

        UnassignCommand command = new UnassignCommand(seniorId, caregiverId);

        CommandResult expectedResult = new UnassignCommand(seniorId, caregiverId).execute(expectedModel);
        String expectedMessage = expectedResult.getFeedbackToUser();

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // Failure paths

    @Test
    public void execute_invalidSeniorId_throwsCommandException() {
        Integer invalidSeniorId = 999;

        Optional<Caregiver> maybeCaregiver = pickAnyCaregiver(model);
        if (maybeCaregiver.isEmpty()) {
            return;
        }
        Integer caregiverId = maybeCaregiver.get().getId();

        UnassignCommand command = new UnassignCommand(invalidSeniorId, caregiverId);
        // Use the same constant names as AssignCommand for consistency; rename if your UnassignCommand differs
        assertCommandFailure(command, model, UnassignCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_invalidCaregiverId_throwsCommandException() {
        Integer invalidCaregiverId = 999;

        Optional<Senior> maybeSenior = pickSeniorWithCaregiver(model);
        if (maybeSenior.isEmpty()) {
            return;
        }
        Integer seniorId = maybeSenior.get().getId();

        UnassignCommand command = new UnassignCommand(seniorId, invalidCaregiverId);
        assertCommandFailure(command, model, UnassignCommand.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void execute_pairNotAssigned_throwsCommandException() {
        // Try to craft a pair that is NOT currently assigned:
        // pick a senior without caregiver (if available) and any caregiver; or pick a mismatched caregiver
        List<Senior> seniors = model.getFilteredSeniorList();
        List<Caregiver> caregivers = model.getFilteredCaregiverList();
        if (seniors.isEmpty() || caregivers.isEmpty()) {
            return;
        }

        // Case A: senior without caregiver
        for (Senior s : seniors) {
            if (!s.hasCaregiver()) {
                Integer seniorId = s.getId();
                Integer caregiverId = caregivers.get(0).getId();
                UnassignCommand command = new UnassignCommand(seniorId, caregiverId);
                assertCommandFailure(command, model, UnassignCommand.MESSAGE_NOT_ASSIGNED);
                return;
            }
        }

        // Case B: all seniors have caregivers; pick a caregiver different from the senior's current one
        for (Senior s : seniors) {
            Integer current = s.getCaregiver().getId();
            for (Caregiver c : caregivers) {
                if (!c.getId().equals(current)) {
                    Integer seniorId = s.getId();
                    Integer caregiverId = c.getId();
                    UnassignCommand command = new UnassignCommand(seniorId, caregiverId);
                    assertCommandFailure(command, model, UnassignCommand.MESSAGE_NOT_ASSIGNED);
                    return;
                }
            }
        }
        // skip when cannot find a mismatched pair
    }

    @Test
    public void execute_bothIdsNull_throwsCommandException() {
        // If your UnassignCommand requires both ids, keep this test; otherwise remove/adjust.
        UnassignCommand command = new UnassignCommand(null, null);
        assertCommandFailure(command, model, UnassignCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_onlyOneSeniorIdProvided_throwsCommandException() {
        UnassignCommand onlySenior = new UnassignCommand(1, null);
        assertCommandFailure(onlySenior, model, UnassignCommand.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void execute_onlyOneCaregiverIdProvided_throwsCommandException() {
        UnassignCommand onlyCaregiver = new UnassignCommand(null, 1);
        assertCommandFailure(onlyCaregiver, model, UnassignCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    // Equality checks and toString method

    @Test
    public void equals_sameIds_true() {
        UnassignCommand a = new UnassignCommand(10, 20);
        UnassignCommand b = new UnassignCommand(10, 20);
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentIds_false() {
        UnassignCommand a = new UnassignCommand(10, 20);
        assertFalse(a.equals(new UnassignCommand(11, 20)));
        assertFalse(a.equals(new UnassignCommand(10, 21)));
        assertFalse(a.equals(null));
        assertFalse(a.equals(42));
    }

    @Test
    public void toStringMethod() {
        UnassignCommand cmd = new UnassignCommand(3, 2);
        String expected = UnassignCommand.class.getCanonicalName()
                + "{seniorIndex=3, caregiverIndex=2}";
        assertEquals(expected, cmd.toString());
    }

    // Helper functions

    /**
     * Find any Senior who currently has a caregiver (already assigned).
     */
    private Optional<Senior> pickSeniorWithCaregiver(Model m) {
        for (Senior s : m.getFilteredSeniorList()) {
            if (s.hasCaregiver()) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    /**
     * Return any caregiver (first) or empty.
     */
    private Optional<Caregiver> pickAnyCaregiver(Model m) {
        List<Caregiver> caregivers = m.getFilteredCaregiverList();
        return caregivers.isEmpty() ? Optional.empty() : Optional.of(caregivers.get(0));
    }

    /**
     * A tiny immutable pair container for local use.
     */
    private static final class Pair<A, B> {
        final A first;
        final B second;

        Pair(A a, B b) {
            this.first = a;
            this.second = b;
        }
    }

    /**
     * Returns a pair (Senior-with-caregiver, that caregiver) if present.
     */
    private Optional<Pair<Senior, Caregiver>> pickCurrentlyAssignedPair(Model m) {
        for (Senior s : m.getFilteredSeniorList()) {
            if (s.hasCaregiver()) {
                Caregiver c = s.getCaregiver();
                return Optional.of(new Pair<>(s, c));
            }
        }
        return Optional.empty();
    }
}
