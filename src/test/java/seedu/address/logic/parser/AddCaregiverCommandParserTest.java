package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCaregiverCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;

/**
 * Tests for {@link AddCaregiverCommandParser}.
 */
public class AddCaregiverCommandParserTest {
    private final AddCaregiverCommandParser parser = new AddCaregiverCommandParser();

    @Test
    public void parse_requiredFieldsPresent_success() {
        Caregiver expected = new Caregiver(new Name("Amy Bee"), new Phone("11111111"),
                new Address("N/A"), new Note("Trained in physiotherapy"), null, false);

        // with optional note only
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + NOTE_DESC_AMY,
                new AddCaregiverCommand(expected));

        // with optional address and note
        Caregiver expectedWithAddress = new Caregiver(new Name("Amy Bee"), new Phone("11111111"),
                new Address("Block 312, Amy Street 1"), new Note("Trained in physiotherapy"), null, false);
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + NOTE_DESC_AMY,
                new AddCaregiverCommand(expectedWithAddress));
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCaregiverCommand.MESSAGE_USAGE);

        // missing name
        assertParseFailure(parser, PHONE_DESC_AMY, expectedMessage);
        // missing phone
        assertParseFailure(parser, NAME_DESC_AMY, expectedMessage);
        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY + PHONE_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValues_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_AMY,
                seedu.address.model.person.Name.MESSAGE_CONSTRAINTS);
        // invalid phone
        assertParseFailure(parser, NAME_DESC_AMY + INVALID_PHONE_DESC,
                seedu.address.model.person.Phone.MESSAGE_CONSTRAINTS);
        // Note: empty address (a/) is accepted for caregivers (treated as optional), so no failure expected here.
    }
}
