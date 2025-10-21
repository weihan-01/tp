package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.PersonHasAnyTagPredicate; // if your class is in model.person, change import accordingly
import seedu.address.testutil.SeniorBuilder;

public class PersonHasAnyTagPredicateTest {

    @Test
    public void test_singleMatch_true() {
        var p = new SeniorBuilder().withTags("lr").build();
        assertTrue(new PersonHasAnyTagPredicate(List.of("lr")).test(p));
    }

    @Test
    public void test_anyOfMultiple_true() {
        var p = new SeniorBuilder().withTags("hr", "mr").build();
        assertTrue(new PersonHasAnyTagPredicate(List.of("lr", "mr")).test(p));
    }

    @Test
    public void test_personTagsCaseInsensitive_true() {
        var p = new SeniorBuilder().withTags("HR").build();
        assertTrue(new PersonHasAnyTagPredicate(List.of("hr")).test(p));
    }

    @Test
    public void test_noMatch_false() {
        var p = new SeniorBuilder().withTags("mr").build();
        assertFalse(new PersonHasAnyTagPredicate(List.of("lr")).test(p));
    }

    @Test
    public void test_emptyTargets_false() {
        var p = new SeniorBuilder().withTags("mr").build();
        assertFalse(new PersonHasAnyTagPredicate(List.of()).test(p));
    }
}
