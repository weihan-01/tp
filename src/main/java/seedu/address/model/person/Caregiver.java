package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Caregiver in the address book.
 *
 */
public class Caregiver extends Person {

    /** Always non-null, format: c<number> (e.g., c1, c2, c10). */
    private final String caregiverId;

    /**
     * Every field must be present and not null.
     */
    public Caregiver(Name name, Phone phone, Address address, Note note,  String caregiverId) {
        super(name, phone, address, note);
        requireNonNull(caregiverId);
        if (!caregiverId.matches("c\\d+")) {
            throw new IllegalArgumentException("Caregiver ID must be of the form c<number> (e.g., c1).");
        }
        this.caregiverId = caregiverId;
    }

    /** Returns this caregiver's ID, e.g. "c10". */
    public String getCaregiverId() {
        return caregiverId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", getName())
                .add("phone", getPhone())
                .add("address", getAddress())
                .add("caregiverId", caregiverId)
                .add("note", getNote())
                .toString();
    }
}
