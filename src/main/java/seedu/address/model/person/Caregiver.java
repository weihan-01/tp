package seedu.address.model.person;

/**
 * Represents a Caregiver in the address book.
 *
 */
public class Caregiver extends Person {
    /**
     * Every field must be present and not null.
     */
    public Caregiver(Name name, Phone phone, Email email, Address address, Note note) {
        super(name, phone, email, address, note);
    }
}
