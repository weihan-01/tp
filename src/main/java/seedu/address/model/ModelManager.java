package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Senior> filteredSeniors;
    private final FilteredList<Caregiver> filteredCaregivers;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredSeniors = new FilteredList<>(this.addressBook.getSeniorList());
        filteredCaregivers = new FilteredList<>(this.addressBook.getCaregiverList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public Caregiver getCaregiverWithId(Integer caregiverId) {
        requireNonNull(caregiverId);
        return addressBook.getCaregiverWithId(caregiverId);
    }

    @Override
    public void deleteSenior(Senior target) {
        addressBook.removeSeniors(target);
    }

    @Override
    public void deleteCaregiver(Caregiver target) {
        addressBook.removeCaregiver(target);
    }

    @Override
    public void addSenior(Senior senior) {
        addressBook.addSenior(senior);
        updateFilteredSeniorList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void addCaregiver(Caregiver caregiver) {
        addressBook.addCaregiver(caregiver);
        updateFilteredCaregiverList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setSenior(Senior target, Senior editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setSenior(target, editedPerson);
    }

    @Override
    public void setCaregiver(Caregiver target, Caregiver editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setCaregiver(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Senior> getFilteredSeniorList() {
        return filteredSeniors;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Caregiver> getFilteredCaregiverList() {
        return filteredCaregivers;
    }

    @Override
    public void updateFilteredSeniorList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredSeniors.setPredicate(predicate);
    }

    @Override
    public void updateFilteredCaregiverList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredCaregivers.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredSeniors.equals(otherModelManager.filteredSeniors)
                && filteredCaregivers.equals(otherModelManager.filteredCaregivers);
    }

    @Override
    public int allocateSeniorId() {
        // Make sure the sequence reflects what's already in memory (sample or loaded)
        addressBook.recomputeSeqFromData();
        return addressBook.nextSeniorId();
    }

    @Override
    public int allocateCaregiverId() {
        // Make sure the sequence reflects what's already in memory (sample or loaded)
        addressBook.recomputeSeqFromData();
        return addressBook.nextCaregiverId();
    }

    @Override
    public String getAssignedCaregiverName(Senior senior) {
        if (senior == null) {
            return null;
        }
        Caregiver cg = senior.getCaregiver();
        if (cg == null) {
            return null;
        }
        return cg.getName().fullName;
    }

    @Override
    public List<String> getAssignedSeniorNames(Caregiver caregiver) {
        if (caregiver == null) {
            return List.of();
        }
        return addressBook.getSeniorList().stream()
                .filter(s -> {
                    Caregiver c = s.getCaregiver();
                    return c != null && c.isSamePerson(caregiver);
                })
                .map(s -> s.getName().fullName)
                .sorted(String::compareToIgnoreCase)
                .toList();
    }
}
