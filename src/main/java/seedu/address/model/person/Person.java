package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an abstract Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public abstract class Person {

    // General identity fields
    private final Name name;
    private final Phone phone;

    // General data fields
    private final Address address;
    private final Note note;

    /**
     * Initialises Person fields with all required person attributes
     * Fields must be present and not null
     *
     * @param name Name of the person
     * @param phone Phone number of the person
     * @param address Address of the person's home
     * @param note Additional notes for the person
     */
    public Person(Name name, Phone phone, Address address, Note note) {
        requireAllNonNull(name, phone, address);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Note getNote() {
        return note;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address)
                && note.equals(otherPerson.note);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("note", note)
                .toString();
    }

}
