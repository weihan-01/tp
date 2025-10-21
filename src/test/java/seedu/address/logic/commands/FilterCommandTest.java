package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonHasAnyTagPredicate; // adjust if needed
import seedu.address.testutil.SeniorBuilder;

public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    private Person aliceLr;
    private Person bobMr;
    private Person caraHr;
    private Person danFriend;

    @BeforeEach
    public void setUp() {
        AddressBook ab = new AddressBook();

        aliceLr   = new SeniorBuilder().withName("Alice").withTags("lr").build();
        bobMr     = new SeniorBuilder().withName("Bob").withTags("mr").build();
        caraHr    = new SeniorBuilder().withName("Cara").withTags("hr").build();
        danFriend = new SeniorBuilder().withName("Dan").withTags("friend").build(); // non-risk tag

        ab.addPerson(aliceLr);
        ab.addPerson(bobMr);
        ab.addPerson(caraHr);
        ab.addPerson(danFriend);

        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());
    }

    @Test
    public void equals() {
        var p1 = new PersonHasAnyTagPredicate(List.of("lr"));
        var p2 = new PersonHasAnyTagPredicate(List.of("mr"));

        FilterCommand c1 = new FilterCommand(p1);
        FilterCommand c2 = new FilterCommand(p2);

        assertTrue(c1.equals(c1));                       // same object
        assertTrue(c1.equals(new FilterCommand(p1)));    // same values
        assertFalse(c1.equals(1));                       // different type
        assertFalse(c1.equals(null));                    // null
        assertFalse(c1.equals(c2));                      // different predicate
    }

    @Test
    public void execute_zeroMatches_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        var predicate = new PersonHasAnyTagPredicate(List.of("unknown")); // tag that nobody has
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTag_filtersOne() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        var predicate = new PersonHasAnyTagPredicate(List.of("lr"));
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals("Alice", model.getFilteredPersonList().get(0).getName().fullName);
    }

    @Test
    public void execute_multipleTags_orAcrossTags() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        var predicate = new PersonHasAnyTagPredicate(Arrays.asList("lr", "hr"));
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Should contain Alice(lr) and Cara(hr) in insertion order
        assertEquals(2, model.getFilteredPersonList().size());
        assertEquals("Alice", model.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Cara",  model.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void toStringMethod() {
        var predicate = new PersonHasAnyTagPredicate(List.of("lr"));
        var cmd = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, cmd.toString());
    }
}
