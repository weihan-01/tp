package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * Tests for {@link PinCommand}.
 * Mirrors the structure and utilities used by DeleteCommandTest/CommandTestUtil.
 */
public class PinCommandTest {

    private static final String PIN_SENTINEL = "PINNED";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validNameUnfilteredList_success() {
        // pick first person in the unfiltered list
        Person personToPin = model.getFilteredPersonList().get(0);
        Name nameArg = personToPin.getName();

        PinCommand command = new PinCommand(nameArg);
        String expectedMessage = String.format(PinCommand.MESSAGE_SUCCESS, personToPin.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        // expected: unpin any existing pinned, then pin target
        unpinAll(expectedModel);
        Person pinnedVersion = withPinnedNote(personToPin);
        expectedModel.setPerson(personToPin, pinnedVersion);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_alreadyPinned_success() throws Exception {
        // Pre-pin a target in the model
        Person target = model.getFilteredPersonList().get(0);
        prePinInModel(model, target);

        // Expected model is identical (no change)
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        PinCommand command = new PinCommand(target.getName());
        String expectedMessage = String.format(PinCommand.MESSAGE_ALREADY_PINNED, target.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nameNotFound_failure() {
        Name missing = new Name("This Name Does Not Exist");
        PinCommand command = new PinCommand(missing);

        assertCommandFailure(command, model,
                String.format(PinCommand.MESSAGE_PERSON_NOT_FOUND, missing));
    }

    @Test
    public void toStringMethod() {
        Name name = new Name("Alice Tan");
        PinCommand command = new PinCommand(name);
        String expected = PinCommand.class.getCanonicalName() + "{targetName=" + name + "}";
        assertEquals(expected, command.toString());
    }

    // helpers to construct the expected state (mirrors PinCommand behavior)

    /** Removes the PINNED sentinel from every person’s note in the model. */
    private static void unpinAll(Model m) {
        List<Person> current = new ArrayList<>(m.getFilteredPersonList());
        for (Person p : current) {
            if (isPinned(p)) {
                m.setPerson(p, cloneWithNote(p, stripPinned(p.getNote())));
            }
        }
    }

    /** Returns a copy of {@code p} whose Note is marked PINNED (prefix strategy). */
    private static Person withPinnedNote(Person p) {
        return cloneWithNote(p, markPinned(p.getNote()));
    }

    /** Adds PINNED to the note if not already present. */
    private static Note markPinned(Note existing) {
        String cur = existing == null ? "" : existing.toString();
        if (cur == null) {
            cur = "";
        }
        cur = cur.trim();
        if (cur.isEmpty()) {
            return new Note(PIN_SENTINEL);
        }

        if (cur.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL)) {
            return existing;
        }

        return new Note(PIN_SENTINEL + " | " + cur);
    }

    /** Strips any PINNED sentinel prefix. */
    private static Note stripPinned(Note existing) {
        String cur = existing == null ? "" : existing.toString();
        if (cur == null) {
            cur = "";
        }
        String norm = cur.trim();
        for (String sep : new String[]{":", "|"}) {
            String pref = PIN_SENTINEL + " " + sep;
            if (norm.toUpperCase(Locale.ROOT).startsWith(pref)) {
                String rest = norm.substring(pref.length()).trim();
                return new Note(rest);
            }
        }
        if (norm.equalsIgnoreCase(PIN_SENTINEL)) {
            return new Note("");
        }
        if (norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + " ")) {
            String rest = norm.substring((PIN_SENTINEL + " ").length()).trim();
            return new Note(rest);
        }
        return existing;
    }

    /** True if the person’s note begins with the PINNED sentinel. */
    private static boolean isPinned(Person p) {
        Note n = p.getNote();
        String s = n == null ? "" : n.toString();
        if (s == null) {
            s = "";
        }
        String norm = s.trim();
        return norm.equalsIgnoreCase(PIN_SENTINEL)
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + " ")
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + ":")
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + " |");
    }

    /**
     * Clones {@code original} to the correct runtime type (Senior/Caregiver/Person),
     * replacing only the {@link Note}. Preserves other fields/relations.
     */
    private static Person cloneWithNote(Person original, Note newNote) {
        if (original instanceof Senior) {
            Senior s = (Senior) original;
            Set<Tag> risk = s.getRiskTags();
            if (s.hasCaregiver()) {
                return new Senior(s.getName(), s.getPhone(), s.getAddress(), risk, newNote, s.getCaregiver());
            } else {
                return new Senior(s.getName(), s.getPhone(), s.getAddress(), risk, newNote);
            }
        } else if (original instanceof Caregiver) {
            Caregiver c = (Caregiver) original;
            return new Caregiver(c.getName(), c.getPhone(), c.getAddress(), newNote, c.getCaregiverId());
        } else {
            return new Person(original.getName(), original.getPhone(), original.getAddress(), newNote);
        }
    }

    /** Precondition helper: unpins all then pins {@code target} in the given model. */
    private static void prePinInModel(Model m, Person target) throws CommandException {
        unpinAll(m);
        m.setPerson(target, withPinnedNote(target));
    }

}
