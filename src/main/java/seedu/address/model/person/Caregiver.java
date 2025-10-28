package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;


/**
 * Represents a Caregiver in the address book.
 */
public class Caregiver extends Person {

    // Caregiver data fields
    private final Integer caregiverId;

    /**
     * Instantiates a caregiver with all required and optional caregiver attributes
     *
     * @param name Name of the caregiver
     * @param phone Phone number of the caregiver
     * @param address Address of the caregiver's home
     * @param note Additional caregiving notes for the senior
     * @param caregiverId ID of the caregiver assigned by AddressBook
     * @param pinned boolean variable for if senior is pinned
     */
    public Caregiver(Name name, Phone phone, Address address, Note note, Integer caregiverId, boolean pinned) {
        super(name, phone, address, note, pinned);
        this.caregiverId = caregiverId;
    }

    /**
     * Immutable factory: return a new Caregiver with the given id.
     */
    public Caregiver withId(int id) {
        return new Caregiver(getName(), getPhone(), getAddress(), getNote(), Integer.valueOf(id), isPinned());
    }

    /** Returns this caregiver's ID */
    public Integer getCaregiverId() {
        return caregiverId;
    }

    /**
     * Returns Caregiver if Caregiver is pinned
     * @param value where it is a boolean value showing if Caregiver is pinned.
     * @return Caregiver if Caregiver is pinned
     */
    public Caregiver withPinned(boolean value) {
        return new Caregiver(getName(), getPhone(), getAddress(), getNote(), getCaregiverId(), value);
    }

    /**
     * Returns this caregiver's note.
     * @param newNote
     * @return Note
     */
    public Caregiver withNote(Note newNote) {
        return new Caregiver(getName(), getPhone(), getAddress(),
                newNote, getCaregiverId(), isPinned());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(super.toString())
                .add("caregiverId", caregiverId)
                .toString();
    }
}
