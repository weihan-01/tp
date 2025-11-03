package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.EditPersonDescriptor;

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
    private final boolean isPinned;

    /**
     * Initializes Person fields with all required person attributes.
     * Fields must be present and not null.
     *
     * @param name     Name of the person
     * @param phone    Phone number of the person
     * @param address  Address of the person's home
     * @param note     Additional notes for the person
     * @param isPinned Whether the person is pinned
     */
    public Person(Name name, Phone phone, Address address, Note note, boolean isPinned) {
        requireAllNonNull(name, phone, address);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.isPinned = isPinned;
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

    public boolean getPinned() {
        return isPinned;
    }

    /**
     * Returns true if both persons are the same object.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == null) {
            return false;
        }
        return this.name.equals(otherPerson.name)
                && this.phone.equals(otherPerson.phone);
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

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address)
                && note.equals(otherPerson.note);
    }

    /**
     * Creates a new edited copy of this person using the provided descriptor.
     * Implemented differently in subclasses (Senior, Caregiver).
     */
    public abstract Person edit(EditPersonDescriptor descriptor);

    /**
     * Returns the Id of the Person.
     * Implemented differently in subclasses.
     */
    public abstract Integer getId();

    @Override
    public int hashCode() {
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
