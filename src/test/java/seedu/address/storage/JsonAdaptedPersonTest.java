package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend"; // invalid tag chars

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_NOTE = BENSON.getNote().toString();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("PERSON", INVALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_NOTE,
                        /*risk*/ null, /*caregiverId*/ null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("PERSON", null, VALID_PHONE, VALID_ADDRESS, VALID_NOTE,
                        null, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("PERSON", VALID_NAME, INVALID_PHONE, VALID_ADDRESS, VALID_NOTE,
                        null, null);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("PERSON", VALID_NAME, null, VALID_ADDRESS, VALID_NOTE,
                        null, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("PERSON", VALID_NAME, VALID_PHONE, null, VALID_NOTE,
                        null, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRiskTagForSenior_throwsIllegalValueException() {
        JsonAdaptedPerson senior =
                new JsonAdaptedPerson("SENIOR", VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_NOTE,
                        Arrays.asList(new JsonAdaptedTag(INVALID_TAG)), null);
        assertThrows(IllegalValueException.class, senior::toModelType);
    }

    @Test
    public void toModelType_validRiskTagForSenior_returnsSenior() throws Exception {
        JsonAdaptedPerson senior =
                new JsonAdaptedPerson("SENIOR", VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_NOTE,
                        Arrays.asList(new JsonAdaptedTag("HR")), null);
        senior.toModelType(); // should not throw
    }
}
