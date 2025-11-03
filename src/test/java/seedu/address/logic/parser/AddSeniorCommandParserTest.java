package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EMPTY_ADDRESS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddSeniorCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

/**
 * Tests for {@link AddSeniorCommandParser}.
 */
public class AddSeniorCommandParserTest {
    private final AddSeniorCommandParser parser = new AddSeniorCommandParser();

    @Test
    public void parse_allRequiredFieldsPresent_success() {
        Senior expected = new Senior(new Name("Amy Bee"), new Phone("11111111"),
                new Address("Block 312, Amy Street 1"), new Tag("HR"), new Note("Trained in physiotherapy"),
                null, null, false);

        // with optional note
        assertParseSuccess(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + NOTE_DESC_AMY,
                new AddSeniorCommand(expected, null));

        // without optional note
        Senior expectedNoNote = new Senior(new Name("Amy Bee"), new Phone("11111111"),
                new Address("Block 312, Amy Street 1"), new Tag("HR"), new Note(""), null, null, false);
        assertParseSuccess(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                new AddSeniorCommand(expectedNoNote, null));
    }

    @Test
    public void parse_withCaregiverId_success() {
        Senior expected = new Senior(new Name("Amy Bee"), new Phone("11111111"),
                new Address("Block 312, Amy Street 1"), new Tag("HR"), new Note(""), null, null, false);
        assertParseSuccess(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + " c/1",
                new AddSeniorCommand(expected, 1));
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSeniorCommand.MESSAGE_USAGE);

        // missing name
        assertParseFailure(parser, TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY, expectedMessage);
        // missing tag
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY, expectedMessage);
        // missing phone
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + ADDRESS_DESC_AMY, expectedMessage);
        // missing address
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY, expectedMessage);
        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY
                + ADDRESS_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValues_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                seedu.address.model.person.Name.MESSAGE_CONSTRAINTS);
        // invalid phone
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + INVALID_PHONE_DESC + ADDRESS_DESC_AMY,
                seedu.address.model.person.Phone.MESSAGE_CONSTRAINTS);
        // empty address (a/ without value) should be rejected with specific empty-address message
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + INVALID_ADDRESS_DESC,
                MESSAGE_EMPTY_ADDRESS);
    }

    @Test
    public void parse_emptyAddress_failure() {
        // explicit empty address string should trigger specific message
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + " a/ \t\n",
                MESSAGE_EMPTY_ADDRESS);
    }

    @Test
    public void parse_invalidCaregiverId_failure() {
        // non-numeric caregiver id triggers index error from ParserUtil
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + " c/x",
                ParserUtil.MESSAGE_INVALID_INDEX);
        // duplicate c/ should surface as duplicate prefix error
        assertParseFailure(parser, NAME_DESC_AMY + TAG_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + " c/1 c/2",
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_CID));
    }
}
