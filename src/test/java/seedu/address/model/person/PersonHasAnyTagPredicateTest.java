package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.CaregiverBuilder;
import seedu.address.testutil.SeniorBuilder;

public class PersonHasAnyTagPredicateTest {

    @Test
    public void test_singleMatch_true() {
        var p = new SeniorBuilder().withRiskTag("lr").build();
        assertTrue(new PersonHasAnyTagPredicate(List.of("lr")).test(p));
    }

    @Test
    public void test_personTagsCaseInsensitive_true() {
        var p = new SeniorBuilder().withRiskTag("HR").build();
        assertTrue(new PersonHasAnyTagPredicate(List.of("hr")).test(p));
    }

    @Test
    public void test_noMatch_false() {
        var p = new SeniorBuilder().withRiskTag("mr").build();
        assertFalse(new PersonHasAnyTagPredicate(List.of("lr")).test(p));
    }

    @Test
    public void test_emptyTargets_false() {
        var p = new SeniorBuilder().withRiskTag("mr").build();
        assertFalse(new PersonHasAnyTagPredicate(List.of()).test(p));
    }

    @Test
    void test_nonSenior_returnsFalse() {
        var caregiver = new CaregiverBuilder().withName("CG").withCaregiverId(7).build();
        assertFalse(new PersonHasAnyTagPredicate(List.of("lr")).test(caregiver));
    }

    @Test
    void emptyTargets_failClosed_false() {
        var senior = new SeniorBuilder().withRiskTag("mr").build();
        assertFalse(new PersonHasAnyTagPredicate(List.of()).test(senior));
    }

    @Test
    void equals_selfFastPath_true() {
        var p = new PersonHasAnyTagPredicate(List.of("lr"));
        assertTrue(p.equals(p)); // hits `other == this`
    }

    // (optional but good): ensure non-senior guard stays covered
    @Test
    void nonSenior_returnsFalse() {
        var cg = new CaregiverBuilder().withCaregiverId(5).build();
        assertFalse(new PersonHasAnyTagPredicate(List.of("lr")).test(cg));
    }

    @Test
    void equals_sameContent_true() {
        var a = new PersonHasAnyTagPredicate(List.of("lr", "hr"));
        var b = new PersonHasAnyTagPredicate(List.of("lr", "hr"));
        assertEquals(a, b); // instanceof true, Objects.equals true
    }

    @Test
    void equals_differentContent_false() {
        var a = new PersonHasAnyTagPredicate(List.of("lr"));
        var b = new PersonHasAnyTagPredicate(List.of("mr"));
        assertNotEquals(a, b); // instanceof true, Objects.equals false
    }

    @Test
    void equals_nullAndDifferentType_false() {
        var a = new PersonHasAnyTagPredicate(List.of("lr"));
        assertNotEquals(null, a); // null path
        assertNotEquals("notPred", a); // instanceof false path
    }
}
