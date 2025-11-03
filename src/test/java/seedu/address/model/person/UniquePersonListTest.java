package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CHARLES;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.CHARLES;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.SeniorBuilder;

public class UniquePersonListTest {

    private final UniquePersonList<Senior> uniqueSeniorList = new UniquePersonList<>();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueSeniorList.contains(AMY));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueSeniorList.add(AMY);
        assertTrue(uniqueSeniorList.contains(AMY));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniqueSeniorList.add(AMY);
        Senior editedAmy = new SeniorBuilder(AMY).withAddress(VALID_ADDRESS_CHARLES).withRiskTag(VALID_TAG_CHARLES)
                .build();
        assertTrue(uniqueSeniorList.contains(editedAmy));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueSeniorList.add(AMY);
        assertThrows(DuplicatePersonException.class, () -> uniqueSeniorList.add(AMY));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.setPerson(null, AMY));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.setPerson(AMY, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueSeniorList.setPerson(AMY, AMY));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniqueSeniorList.add(AMY);
        uniqueSeniorList.setPerson(AMY, AMY);
        UniquePersonList<Senior> expectedUniquePersonList = new UniquePersonList<>();
        expectedUniquePersonList.add(AMY);
        assertEquals(expectedUniquePersonList, uniqueSeniorList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniqueSeniorList.add(AMY);
        Senior editedAmy = new SeniorBuilder(AMY).withAddress(VALID_ADDRESS_CHARLES).withRiskTag(VALID_TAG_CHARLES)
                .build();
        uniqueSeniorList.setPerson(AMY, editedAmy);
        UniquePersonList<Senior> expectedUniqueSeniorList = new UniquePersonList<>();
        expectedUniqueSeniorList.add(editedAmy);
        assertEquals(expectedUniqueSeniorList, uniqueSeniorList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniqueSeniorList.add(AMY);
        uniqueSeniorList.setPerson(AMY, CHARLES);
        UniquePersonList<Senior> expectedUniqueSeniorList = new UniquePersonList<>();
        expectedUniqueSeniorList.add(CHARLES);
        assertEquals(expectedUniqueSeniorList, uniqueSeniorList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueSeniorList.add(AMY);
        uniqueSeniorList.add(CHARLES);
        assertThrows(DuplicatePersonException.class, () -> uniqueSeniorList.setPerson(AMY, CHARLES));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueSeniorList.remove(AMY));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueSeniorList.add(AMY);
        uniqueSeniorList.remove(AMY);
        UniquePersonList<Senior> expectedUniqueSeniorList = new UniquePersonList<>();
        assertEquals(expectedUniqueSeniorList, uniqueSeniorList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.setPersons((UniquePersonList<Senior>) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniqueSeniorList.add(AMY);
        UniquePersonList<Senior> expectedUniqueSeniorList = new UniquePersonList<>();
        expectedUniqueSeniorList.add(CHARLES);
        uniqueSeniorList.setPersons(expectedUniqueSeniorList);
        assertEquals(expectedUniqueSeniorList, uniqueSeniorList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueSeniorList.setPersons((List<Senior>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniqueSeniorList.add(AMY);
        List<Senior> seniorList = Collections.singletonList(CHARLES);
        uniqueSeniorList.setPersons(seniorList);
        UniquePersonList<Senior> expectedUniqueSeniorList = new UniquePersonList<>();
        expectedUniqueSeniorList.add(CHARLES);
        assertEquals(expectedUniqueSeniorList, uniqueSeniorList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Senior> listWithDuplicatePersons = Arrays.asList(AMY, AMY);
        assertThrows(DuplicatePersonException.class, () -> uniqueSeniorList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueSeniorList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueSeniorList.asUnmodifiableObservableList().toString(), uniqueSeniorList.toString());
    }
}
