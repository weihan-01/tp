package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Senior> getSeniorList();

    ObservableList<Caregiver> getCaregiverList();

}
