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
     */
    public Caregiver(Name name, Phone phone, Address address, Note note, Integer caregiverId) {
        super(name, phone, address, note);
        this.caregiverId = caregiverId;
    }

    /**
     * Immutable factory: return a new Caregiver with the given id.
     */
    public Caregiver withId(int id) {
        return new Caregiver(getName(), getPhone(), getAddress(), getNote(), Integer.valueOf(id));
    }

    /** Returns this caregiver's ID */
    public Integer getCaregiverId() {
        return caregiverId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(super.toString())
                .add("caregiverId", caregiverId)
                .toString();
    }
}
