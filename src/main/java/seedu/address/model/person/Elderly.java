package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents an Elderly in the address book.
 *
 */
public class Elderly extends Person {
    // Identity fields
    private final Set<Tag> riskTags = new HashSet<>();
    private Caregiver caregiver;

    /**
     * Every field must be present and not null.
     * Caregiver can be null if no caregiver is assigned.
     */
    public Elderly(Name name, Phone phone, Email email, Address address,
                   Set<Tag> riskTags, Note note, Caregiver caregiver) {
        super(name, phone, email, address, note);
        requireAllNonNull(riskTags);
        this.riskTags.addAll(riskTags);
        this.caregiver = caregiver; // Can be null
    }

    /**
     * Convenience constructor for Elderly without a caregiver.
     */
    public Elderly(Name name, Phone phone, Email email, Address address,
                   Set<Tag> riskTags, Note note) {
        this(name, phone, email, address, riskTags, note, null);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getRiskTags() {
        return Collections.unmodifiableSet(riskTags);
    }

    /**
     * Returns the caregiver assigned to this elderly person.
     * Returns null if no caregiver is assigned.
     */
    public Caregiver getCaregiver() {
        return caregiver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(super.toString())
                .add("riskTags", riskTags)
                .toString();
    }
}
