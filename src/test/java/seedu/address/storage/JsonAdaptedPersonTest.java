package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedSenior.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_RISK = "moderate risk"; // invalid tag chars

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_NOTE = BENSON.getNote().toString();
    private static final String VALID_TAG = AMY.getRiskTag().toString();
    private static final Integer VALID_ID = BENSON.getId();
    private static final boolean VALID_PIN = BENSON.getPinned();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedCaregiver person = new JsonAdaptedCaregiver(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedCaregiver person =
                new JsonAdaptedCaregiver(INVALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_NOTE, VALID_ID, VALID_PIN);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedCaregiver person =
                new JsonAdaptedCaregiver(null, VALID_PHONE, VALID_ADDRESS, VALID_NOTE, VALID_ID, VALID_PIN);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedCaregiver person =
                new JsonAdaptedCaregiver(VALID_NAME, INVALID_PHONE, VALID_ADDRESS, VALID_NOTE, VALID_ID, VALID_PIN);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedCaregiver person =
                new JsonAdaptedCaregiver(VALID_NAME, null, VALID_ADDRESS, VALID_NOTE, VALID_ID, VALID_PIN);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedCaregiver person =
                new JsonAdaptedCaregiver(VALID_NAME, VALID_PHONE, null, VALID_NOTE, VALID_ID, VALID_PIN);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRiskTagForSenior_throwsIllegalValueException() {
        JsonAdaptedSenior senior =
                new JsonAdaptedSenior(
                        VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                        VALID_NOTE, INVALID_RISK, VALID_ID, VALID_ID, VALID_PIN);
        assertThrows(IllegalValueException.class, senior::toModelType);
    }

    @Test
    public void toModelType_validRiskTagForSenior_returnsSenior() throws Exception {
        JsonAdaptedSenior senior =
                new JsonAdaptedSenior(VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_NOTE, VALID_TAG,
                        VALID_ID, VALID_ID, VALID_PIN);
        senior.toModelType(); // should not throw
    }
}
