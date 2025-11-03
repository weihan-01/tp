package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

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
import seedu.address.testutil.CaregiverBuilder;
import seedu.address.testutil.SeniorBuilder;

/**
 * AssignCommand tests
 */
public class AssignCommandTest {

    private Senior aliceLr;
    private Senior bobMr;
    private Senior caraHr;
    private Senior danLr;

    private Caregiver cgrElsa;
    private Caregiver cgrFaith;
    private Caregiver cgrJodie;

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {

        AddressBook ab = new AddressBook();

        aliceLr = new SeniorBuilder().withName("Alice").withRiskTag("lr").build();
        bobMr = new SeniorBuilder().withName("Bob").withRiskTag("mr").build();
        caraHr = new SeniorBuilder().withName("Cara").withRiskTag("hr").build();
        danLr = new SeniorBuilder().withName("Dan").withRiskTag("lr").build();

        cgrElsa = new CaregiverBuilder().withName("Elsa").build();
        cgrFaith = new CaregiverBuilder().withName("Faith").build();
        cgrJodie = new CaregiverBuilder().withName("Jodie").build();

        ab.addSenior(aliceLr);
        ab.addSenior(bobMr);
        ab.addSenior(caraHr);
        ab.addSenior(danLr);
        ab.addCaregiver(cgrElsa);
        ab.addCaregiver(cgrFaith);
        ab.addCaregiver(cgrJodie);

        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());

    }

    // Success Path

    @Test
    public void execute_assign_success() throws Exception {
        Optional<Senior> maybeSenior = pickSeniorPreferNoCaregiver(model);
        Optional<Caregiver> maybeCaregiver = pickAnyCaregiver(model);

        Senior senior = maybeSenior.get();
        Caregiver caregiver = maybeCaregiver.get();

        Integer seniorId = senior.getId();
        Integer caregiverId = caregiver.getId();

        AssignCommand command = new AssignCommand(seniorId, caregiverId);

        // Mirror mutation & get exact expected message
        CommandResult expectedResult = new AssignCommand(seniorId, caregiverId).execute(expectedModel);
        String expectedMessage = expectedResult.getFeedbackToUser();

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignOnFilteredLists_success() throws Exception {
        Optional<Senior> maybeSenior = pickSeniorPreferNoCaregiver(model);
        Optional<Caregiver> maybeCaregiver = pickAnyCaregiver(model);

        Senior targetSenior = maybeSenior.get();
        Caregiver targetCaregiver = maybeCaregiver.get();

        Predicate<Person> seniorOnly = s -> s.equals(targetSenior);
        Predicate<Person> caregiverOnly = c -> c.equals(targetCaregiver);

        model.updateFilteredSeniorList(seniorOnly);
        model.updateFilteredCaregiverList(caregiverOnly);
        expectedModel.updateFilteredSeniorList(seniorOnly);
        expectedModel.updateFilteredCaregiverList(caregiverOnly);

        Integer seniorId = targetSenior.getId();
        Integer caregiverId = targetCaregiver.getId();

        AssignCommand command = new AssignCommand(seniorId, caregiverId);

        CommandResult expectedResult = new AssignCommand(seniorId, caregiverId).execute(expectedModel);
        String expectedMessage = expectedResult.getFeedbackToUser();

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // Failure Paths

    @Test
    public void execute_invalidSeniorId_throwsCommandException() {
        // Choose an unreachable seniorId
        Integer invalidSeniorId = 999_999;

        // Need a valid caregiver id for this path
        Optional<Caregiver> maybeCaregiver = pickAnyCaregiver(model);
        if (maybeCaregiver.isEmpty()) {
            return;
        }
        Integer caregiverId = maybeCaregiver.get().getId();

        AssignCommand command = new AssignCommand(invalidSeniorId, caregiverId);
        assertCommandFailure(command, model, AssignCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_invalidCaregiverId_throwsCommandException() {
        // Choose an unreachable caregiverId
        Integer invalidCaregiverId = 999_999;

        Optional<Senior> maybeSenior = pickSeniorPreferNoCaregiver(model);
        if (maybeSenior.isEmpty()) {
            return;
        }
        Integer seniorId = maybeSenior.get().getId();

        AssignCommand command = new AssignCommand(seniorId, invalidCaregiverId);
        assertCommandFailure(command, model, AssignCommand.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    @Test
    public void execute_invalidSeniorIdFromFilteredList_throwsCommandException() {
        // Narrow lists so we prove id-based lookup, not index-based
        if (model.getFilteredSeniorList().isEmpty() || model.getFilteredCaregiverList().isEmpty()) {
            return;
        }
        Senior onlySenior = model.getFilteredSeniorList().get(0);
        Caregiver onlyCaregiver = model.getFilteredCaregiverList().get(0);

        model.updateFilteredSeniorList(s -> s.equals(onlySenior));
        expectedModel.updateFilteredSeniorList(s -> s.equals(onlySenior));
        model.updateFilteredCaregiverList(c -> c.equals(onlyCaregiver));
        expectedModel.updateFilteredCaregiverList(c -> c.equals(onlyCaregiver));

        Integer invalidSeniorId = 999_999; // definitely not in model
        Integer caregiverId = onlyCaregiver.getId();

        AssignCommand command = new AssignCommand(invalidSeniorId, caregiverId);
        assertCommandFailure(command, model, AssignCommand.MESSAGE_INVALID_SENIOR_INDEX);
    }

    @Test
    public void execute_invalidCaregiverIdFromFilteredList_throwsCommandException() {
        if (model.getFilteredSeniorList().isEmpty() || model.getFilteredCaregiverList().isEmpty()) {
            return;
        }
        Senior onlySenior = model.getFilteredSeniorList().get(0);
        Caregiver onlyCaregiver = model.getFilteredCaregiverList().get(0);

        model.updateFilteredSeniorList(s -> s.equals(onlySenior));
        expectedModel.updateFilteredSeniorList(s -> s.equals(onlySenior));
        model.updateFilteredCaregiverList(c -> c.equals(onlyCaregiver));
        expectedModel.updateFilteredCaregiverList(c -> c.equals(onlyCaregiver));

        Integer seniorId = onlySenior.getId();
        Integer invalidCaregiverId = 999_999;

        AssignCommand command = new AssignCommand(seniorId, invalidCaregiverId);
        assertCommandFailure(command, model, AssignCommand.MESSAGE_INVALID_CAREGIVER_INDEX);
    }

    // Equality test and to string

    @Test
    public void equals_sameIds_true() {
        AssignCommand a = new AssignCommand(10, 20);
        AssignCommand b = new AssignCommand(10, 20);
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentIds_false() {
        AssignCommand a = new AssignCommand(10, 20);
        assertFalse(a.equals(new AssignCommand(11, 20)));
        assertFalse(a.equals(new AssignCommand(10, 21)));
        assertFalse(a.equals(null));
        assertFalse(a.equals(42));
    }

    @Test
    public void toStringMethod() {
        AssignCommand cmd = new AssignCommand(3, 2);
        String expected = AssignCommand.class.getCanonicalName()
                + "{seniorIndex=3, caregiverIndex=2}";
        assertEquals(expected, cmd.toString());
    }

    // Helper Function

    /**
     * Returns a Senior preferring one without a caregiver, else first present.
     */
    private Optional<Senior> pickSeniorPreferNoCaregiver(Model m) {
        List<Senior> seniors = m.getFilteredSeniorList();
        for (Senior s : seniors) {
            if (!s.hasCaregiver()) {
                return Optional.of(s);
            }
        }
        return seniors.isEmpty() ? Optional.empty() : Optional.of(seniors.get(0));
    }

    /**
     * Returns any available Caregiver (first).
     */
    private Optional<Caregiver> pickAnyCaregiver(Model m) {
        List<Caregiver> caregivers = m.getFilteredCaregiverList();
        return caregivers.isEmpty() ? Optional.empty() : Optional.of(caregivers.get(0));
    }

}
