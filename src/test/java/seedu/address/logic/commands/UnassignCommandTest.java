package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
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
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
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
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for {@code UnassignCommand}.
 */
public class UnassignCommandTest {

    private final Name snrName = new Name(VALID_NAME_AMY);
    private final Phone snrPhone = new Phone(VALID_PHONE_AMY);
    private final Address snrAddress = new Address(VALID_ADDRESS_AMY);
    private final Tag snrTag = new Tag("HR");
    private final Note snrNote = new Note(VALID_NOTE_AMY);

    private final Name cgrName = new Name(VALID_NAME_BOB);
    private final Phone cgrPhone = new Phone(VALID_PHONE_BOB);
    private final Address cgrAddress = new Address(VALID_ADDRESS_BOB);
    private final Note cgrNote = new Note(VALID_NOTE_BOB);

    private final Caregiver caregiver = new Caregiver(cgrName, cgrPhone, cgrAddress, cgrNote, "c10");

    @Test
    public void execute_validIndices_successfulUnassignment() throws Exception {
        // Senior already assigned to caregiver
        Senior seniorWithCgr = new Senior(snrName, snrPhone, snrAddress, Set.of(snrTag), snrNote, caregiver);

        ModelStubWithPersons model = new ModelStubWithPersons(seniorWithCgr, caregiver);

        UnassignCommand command = new UnassignCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        CommandResult result = command.execute(model);

        assertEquals(String.format(UnassignCommand.MESSAGE_UNASSIGN_SUCCESS, seniorWithCgr.getName(), caregiver.getName()),
                result.getFeedbackToUser());
        assertFalse(((Senior) model.getFilteredPersonList().get(0)).hasCaregiver());
    }

    @Test
    public void execute_notAssigned_throwsCommandException() {
        // Senior WITHOUT caregiver assigned
        Senior seniorWithoutCg = new Senior(snrName, snrPhone, snrAddress, Set.of(snrTag), snrNote);

        ModelStubWithPersons model = new ModelStubWithPersons(seniorWithoutCg, caregiver);

        UnassignCommand command = new UnassignCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    /**
     * A default model stub that has all methods throwing by default.
     */
    private static class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("Should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("Should not be called.");
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
     * A Model stub backed by a simple list of persons to support UnassignCommand tests.
     */
    private static class ModelStubWithPersons extends ModelStub {
        final ArrayList<Person> personList = new ArrayList<>();

        ModelStubWithPersons(Person... persons) {
            for (Person p : persons) {
                personList.add(p);
            }
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
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
            // no-op for these tests
        }
    }
}
