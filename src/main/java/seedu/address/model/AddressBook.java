package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList<Senior> seniors;
    private final UniquePersonList<Caregiver> caregivers;
    private int seniorSeq = 0; // last assigned senior id (not next)
    private int caregiverSeq = 0; // last assigned caregiver id (not next)

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    public int getSeniorSeq() {
        return seniorSeq;
    }

    public int getCaregiverSeq() {
        return caregiverSeq;
    }

    public void setSeniorSeq(int seq) {
        if (seq < 0) {
            seq = 0;
        }
        this.seniorSeq = seq;
    }

    public void setCaregiverSeq(int seq) {
        if (seq < 0) {
            seq = 0;
        }
        this.caregiverSeq = seq;
    }

    /** Returns next senior id by incrementing the sequence. */
    public int nextSeniorId() {
        seniorSeq += 1;
        return seniorSeq;
    }

    /** Returns next caregiver id by incrementing the sequence. */
    public int nextCaregiverId() {
        caregiverSeq += 1;
        return caregiverSeq;
    }

    /** Returns caregiver with matching id. */
    public Caregiver getCaregiverWithId(Integer caregiverId) {
        for (Person p: caregivers) {
            if (p instanceof Caregiver) {
                Caregiver caregiver = (Caregiver) p;
                int cid = caregiver.getCaregiverId();
                if (cid == caregiverId) {
                    return caregiver;
                }
            }
        }
        return null;
    }

    /** One-time recompute from existing data (for legacy files with no seq). */
    public void recomputeSeqFromData() {
        int seniorMax = 0;
        int caregiverMax = 0;
        for (Person p : seniors) { // persons is the UniquePersonList backing AddressBook
            if (p instanceof Senior) {
                Integer sid = ((Senior) p).getSeniorId();
                if (sid < 0) {
                    throw new IllegalArgumentException("Senior ID must be a positive integer.");
                }
                if (sid > seniorMax) {
                    seniorMax = sid;
                }
            }
        }

        for (Person p : caregivers) {
            if (p instanceof Caregiver) {
                int cid = ((Caregiver) p).getCaregiverId();
                if (cid < 0) {
                    throw new IllegalArgumentException("Caregiver ID must be a positive integer.");
                }
                if (cid > caregiverMax) {
                    caregiverMax = cid;
                }
            }
        }
        seniorSeq = Math.max(seniorSeq, seniorMax);
        caregiverSeq = Math.max(caregiverSeq, caregiverMax);
    }
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        seniors = new UniquePersonList();
        caregivers = new UniquePersonList();
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setSeniors(List<Senior> persons) {
        this.seniors.setPersons(persons);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setCaregivers(List<Caregiver> persons) {
        this.caregivers.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setSeniors(newData.getSeniorList());
        setCaregivers(newData.getCaregiverList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return seniors.contains(person) || caregivers.contains(person);
    }

    /**
     * Adds a senior to the address book.
     * The senior must not already exist in the address book.
     */
    public void addSenior(Senior p) {
        seniors.add(p);
    }

    /**
     * Adds a caregiver to the address book.
     * The caregiver must not already exist in the address book.
     */
    public void addCaregiver(Caregiver p) {
        caregivers.add(p);
    }

    /**
     * Replaces the given senior {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setSenior(Senior target, Senior editedPerson) {
        requireNonNull(editedPerson);

        seniors.setPerson(target, editedPerson);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setCaregiver(Caregiver target, Caregiver editedPerson) {
        requireNonNull(editedPerson);

        caregivers.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeSeniors(Senior key) {
        seniors.remove(key);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeCaregiver(Caregiver key) {
        caregivers.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("seniors", seniors)
                .add("caregivers", caregivers)
                .toString();
    }

    @Override
    public ObservableList<Senior> getSeniorList() {
        return seniors.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Caregiver> getCaregiverList() {
        return caregivers.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return seniors.equals(otherAddressBook.seniors) && caregivers.equals(otherAddressBook.caregivers);
    }

    @Override
    public int hashCode() {
        // TODO: Change hashcode
        return seniors.hashCode();
    }
}
