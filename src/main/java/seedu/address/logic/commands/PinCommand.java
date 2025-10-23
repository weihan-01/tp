package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class PinCommand extends Command {

    public static final String COMMAND_WORD = "pin";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Pins a person by exact name.\n"
            + "Parameters: n/NAME\n"
            + "Example: " + COMMAND_WORD + " n/Yap Mei Ting";

    public static final String MESSAGE_SUCCESS = "Pinned: %s";
    public static final String MESSAGE_ALREADY_PINNED = "%1$s is already pinned.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with the name : %1$s";

    private static final String PIN_SENTINEL = "PINNED";

    private final Name targetName;

    public PinCommand(Name targetName) {
        requireNonNull(targetName);
        this.targetName = targetName;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find target by exact name (case-insensitve) within ALL persons
        List<Person> allPersons = model.getFilteredPersonList();
        Person target = findByExactName(allPersons, targetName);

        if (target == null) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, targetName));
        }

        // If already pinned, no operation
        if (isPinned(target)) {
            return new CommandResult(String.format(MESSAGE_ALREADY_PINNED, target.getName()));
        }

        for (Person p : allPersons) {
            if (isPinned(p)) {
                Person unpinned = cloneWithNote(p, stripPinned(p.getNote()));
                model.setPerson(p, unpinned);
            }
        }

        // Pin target
        Person pinned = cloneWithNote(target, markPinned(target.getNote()));
        model.setPerson(target, pinned);

        // Refresh list so the UI resorts (comparator should put pin first)
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName()));
    }

    // Helper Functions
    private static Person findByExactName(List<Person> list, Name targetName) {
        String needle = targetName.fullName.trim().toLowerCase(Locale.ROOT);
        for (Person p : list) {
            String hay = p.getName().fullName.trim().toLowerCase(Locale.ROOT);
            if (hay.equals(needle)) {
                return p;
            }
        }
        return null;
    }

    public static boolean isPinned(Person p) {
        Note n = p.getNote();
        String s = n == null ? "" : n.toString();

        String norm = s.trim();
        return norm.equalsIgnoreCase(PIN_SENTINEL)
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + " ")
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + ":")
                || norm.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL + " |");
    }

    private static Note markPinned(Note existing) {
        String cur = existing == null ? "" : existing.toString();
        cur = cur.trim();
        if (cur.isEmpty()) {
            return new Note(PIN_SENTINEL);
        }
        if (cur.toUpperCase(Locale.ROOT).startsWith(PIN_SENTINEL)) {
            return existing; // already marked
        }
        return new Note(PIN_SENTINEL + " | " + cur);
    }

    protected static Note stripPinned(Note existing) {
        String cur = existing == null ? "" : existing.toString();
        if (cur == null) {
            cur = "";
        }
        String norm = cur.trim();

        // Remove common prefixes we add
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
        return existing; // nothing to strip
    }

    /**
     * Clone a person preserving its concrete type, but with a different Note.
     */
    protected static Person cloneWithNote(Person original, Note newNote) {
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
            // Plain Person
            return new Person(original.getName(), original.getPhone(), original.getAddress(), newNote);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PinCommand
                && targetName.equals(((PinCommand) other).targetName));
    }

}
