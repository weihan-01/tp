package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

public class AssignCommandTest {
    private final Name snrName = new Name(VALID_NAME_AMY);
    private final Phone snrPhone = new Phone(VALID_PHONE_AMY);
    private final Address snrAddress = new Address(VALID_ADDRESS_AMY);
    private final Tag snrTag = new Tag("HR");
    private final Note snrNote = new Note(VALID_NOTE_AMY);

    private final Senior senior = new Senior(snrName, snrPhone, snrAddress, Set.of(snrTag), snrNote, null, null);

    private final Name cgrName = new Name(VALID_NAME_BOB);
    private final Phone cgrPhone = new Phone(VALID_PHONE_BOB);
    private final Address cgrAddress = new Address(VALID_ADDRESS_BOB);
    private final Note cgrNote = new Note(VALID_NOTE_BOB);

    private final Caregiver caregiver = new Caregiver(cgrName, cgrPhone, cgrAddress, cgrNote, 10);
    @Test
    public void execute_validIndices_successfulAssignment() throws Exception {
        ModelStubWithPersons model = new ModelStubWithPersons(senior, caregiver);
        AssignCommand command = new AssignCommand(1, 2);

        CommandResult result = command.execute(model);

        assertEquals(String.format(AssignCommand.MESSAGE_ASSIGN_SUCCESS, senior.getName(), caregiver.getName()),
                result.getFeedbackToUser());
        assertTrue(((Senior) model.getFilteredPersonList().get(0)).hasCaregiver());
    }


    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String allocateCaregiverId() {
            return "";
        }

        @Override
        public String getAssignedCaregiverName(Senior senior) {
            return "";
        }

        @Override
        public List<String> getAssignedSeniorNames(Caregiver caregiver) {
            return List.of();
        }

    }

    /**
     * A Model stub with people.
     */
    public class ModelStubWithPersons extends ModelStub {
        final ArrayList<Person> personList = new ArrayList<>();

        public ModelStubWithPersons(Person... persons) {
            for (Person p : persons) {
                personList.add(p);
            }
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            // Return an ObservableList backed by the personList so the test can read and update it.
            return FXCollections.observableList(personList);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int index = personList.indexOf(target);
            if (index == -1) {
                throw new AssertionError("Target person not found in list.");
            }
            personList.set(index, editedPerson);
        }

        @Override
        public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
        }
    }


}
