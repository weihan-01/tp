package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new {@link PinCommand}.
 * Expected format: {@code pin n/NAME}
 */
public class PinCommand extends Command {

    public static final String COMMAND_WORD = "pin";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Pins a person by exact name.\n"
            + "Parameters: s/SENIOR_INDEX c/CAREGIVER_INDEX\n"
            + "Example: " + COMMAND_WORD + " s/5 c/3";

    public static final String MESSAGE_SUCCESS = "Pinned: %s";
    public static final String MESSAGE_ALREADY_PINNED = "%1$s is already pinned.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with the name : %1$s";
    public static final String MESSAGE_INVALID_PERSON = "Person is invalid";

    public static final String MESSAGE_INVALID_SENIOR_INDEX = "No such senior index exists.";
    public static final String MESSAGE_INVALID_CAREGIVER_INDEX = "No such caregiver index exists.";
    public static final String MESSAGE_NO_PERSONS = "No such senior and caregiver exist.";

    private static final String PIN_SENTINEL = "PINNED";

    private final Integer seniorIndex;
    private final Integer caregiverIndex;

    /**
     * Creates a {@code PinCommand} targeting a specific person by name.
     * <p>
     * The command will pin exactly the person whose name matches {@code seniorIndex}
     * (comparison is performed case-insensitively during execution).
     *
     * @param seniorIndex the id of the senior to pin;
     * @param caregiverIndex the id of the caregiver to pin;
     */
    public PinCommand(Integer seniorIndex, Integer caregiverIndex) {
        this.seniorIndex = seniorIndex;
        this.caregiverIndex = caregiverIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find target by exact name (case-insensitve) within ALL persons
        List<Senior> fullSeniorList = model.getAllSeniorList();
        List<Caregiver> fullCaregiverList = model.getAllCaregiverList();

        // Find senior by seniorId
        Senior senior = fullSeniorList.stream()
                .filter(s -> {
                    Integer seniorId = s.getSeniorId();
                    return seniorId != null && (seniorId.equals(seniorIndex));
                })
                .findFirst()
                .orElse(null);
        if (seniorIndex != null && senior == null) {
            throw new CommandException(MESSAGE_INVALID_SENIOR_INDEX);
        }

        // Find caregiver by caregiverId
        Caregiver caregiver = fullCaregiverList.stream()
                .filter(c -> {
                    Integer caregiverId = c.getCaregiverId();
                    return caregiverId != null && (caregiverId.equals(caregiverIndex));
                })
                .findFirst()
                .orElse(null);
        if (caregiverIndex != null && caregiver == null) {
            throw new CommandException(MESSAGE_INVALID_CAREGIVER_INDEX);
        }

        // If already pinned, no operation
        if (senior != null && isPinned(senior)) {
            return new CommandResult(String.format(MESSAGE_ALREADY_PINNED, senior.getName()));
        }
        if (caregiver != null && isPinned(caregiver)) {
            return new CommandResult(String.format(MESSAGE_ALREADY_PINNED, caregiver.getName()));
        }

        if (senior != null) {
            for (Senior s : fullSeniorList) {
                if (isPinned(s)) {
                    Person unpinned = cloneWithNote(s, stripPinned(s.getNote()));
                    model.setSenior(s, (Senior) unpinned);
                }
            }
            // Pin targets
            model.setSenior(senior, (Senior) cloneWithNote(senior, markPinned(senior.getNote())));
        }

        if (caregiver != null) {
            for (Caregiver c : fullCaregiverList) {
                if (isPinned(c)) {
                    Person unpinned = cloneWithNote(c, stripPinned(c.getNote()));
                    model.setCaregiver(c, (Caregiver) unpinned);
                }
            }
            model.setCaregiver(caregiver, (Caregiver) cloneWithNote(caregiver, markPinned(caregiver.getNote())));
        }

        // Refresh list so the UI resorts (comparator should put pin first)
        model.updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);

        if (senior != null && caregiver == null) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(senior)));
        } else if (senior == null && caregiver != null) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(caregiver)));
        } else if (senior != null & caregiver != null) {
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS, Messages.format(senior)) + " and "
                            + String.format(MESSAGE_SUCCESS, Messages.format(caregiver)));
        } else {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
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

    /**
     * Returns whether the given {@link Person} is marked as pinned.
     * <p>
     * A person is considered pinned if their {@link Note} text starts with the sentinel
     * {@code PIN_SENTINEL} (case-insensitive), optionally followed by a space and one of
     * {@code ":"} or {@code "|"}, or by a single trailing space. Examples that return {@code true}:
     * <ul>
     *   <li>{@code "PINNED"}</li>
     *   <li>{@code "PINNED : some note"}</li>
     *   <li>{@code "PINNED | some note"}</li>
     *   <li>{@code "PINNED some note"}</li>
     * </ul>
     *
     * @param p the person to check; must not be {@code null}
     * @return {@code true} if the person's note indicates they are pinned; {@code false} otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
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
    protected static Person cloneWithNote(Person original, Note newNote) throws CommandException {
        if (original instanceof Senior) {
            Senior s = (Senior) original;
            Set<Tag> risk = s.getRiskTags();
            return new Senior(s.getName(), s.getPhone(),
                        s.getAddress(), risk, newNote, s.getCaregiver(), s.getSeniorId());
        } else if (original instanceof Caregiver) {
            Caregiver c = (Caregiver) original;
            return new Caregiver(c.getName(), c.getPhone(), c.getAddress(), newNote, c.getCaregiverId());
        } else {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PinCommand
                && seniorIndex.equals(((PinCommand) other).seniorIndex)
                && caregiverIndex.equals(((PinCommand) other).caregiverIndex));
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName()
                + "{seniorId=" + seniorIndex
                + "caregiverId=" + caregiverIndex + "}";
    }
}
