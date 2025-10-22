package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns the caregiver with the same id as {@code caregiverId} in the address book.
     */
    Caregiver getCaregiverWithId(Integer caregiverId);

    /**
     * Deletes the given senior.
     * The senior must exist in the address book.
     */
    void deleteSenior(Person target);

    /**
     * Deletes the given caregiver.
     * The caregiver must exist in the address book.
     */
    void deleteCaregiver(Person target);

    /**
     * Adds the given senior.
     * {@code person} must not already exist in the address book.
     */
    void addSenior(Person person);

    /**
     * Adds the given caregiver.
     * {@code person} must not already exist in the address book.
     */
    void addCaregiver(Person person);

    /**
     * Replaces the given senior {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setSenior(Person target, Person editedPerson);

    /**
     * Replaces the given caregiver {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setCaregiver(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered senior list */
    ObservableList<Person> getFilteredSeniorList();

    /** Returns an unmodifiable view of the filtered senior list */
    ObservableList<Person> getFilteredCaregiverList();

    /**
     * Updates the filter of the filtered senior list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredSeniorList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered caregiver list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCaregiverList(Predicate<Person> predicate);

    /**
     * Allocates and returns the next caregiver identifier.
     * The identifier is unique within the address book and has the form {@code N},
     * where {@code N} is a monotonically increasing positive integer.
     * <p>
     * This method does not add a caregiver; it only reserves an ID for immediate use.
     * Implementations should persist the underlying sequence so IDs remain monotonic
     * across application restarts.
     *
     * @return the next unused caregiver ID (e.g. {@code "10"}).
     */
    int allocateCaregiverId();

    /**
     * Allocates and returns the next senior identifier.
     * The identifier is unique within the address book and has the form {@code N},
     * where {@code N} is a monotonically increasing positive integer.
     * <p>
     * This method does not add a caregiver; it only reserves an ID for immediate use.
     * Implementations should persist the underlying sequence so IDs remain monotonic
     * across application restarts.
     *
     * @return the next unused caregiver ID (e.g. {@code "10"}).
     */
    int allocateSeniorId();

    /**
     * Returns the name of the Senior's assigned caregiver (1).
     */
    String getAssignedCaregiverName(Senior senior);

    /**
     * Returns the names of the Caregiver's assigned seniors.
     */
    List<String> getAssignedSeniorNames(Caregiver caregiver);

}
