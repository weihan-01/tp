package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.PinCommand;

/**
 * Tests for {@link PinCommandParser}.
 */
public class PinCommandParserTest {
    private final PinCommandParser parser = new PinCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " s/1", new PinCommand(1, null));
        assertParseSuccess(parser, " c/2", new PinCommand(null, 2));
    }

    @Test
    public void parse_invalidArgs_failure() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE);
        // both prefixes or none
        assertParseFailure(parser, "", expected);
        assertParseFailure(parser, " s/1 c/2", expected);
        // non-numeric
        assertParseFailure(parser, " s/A", expected);
        assertParseFailure(parser, " c/-1", expected);
        // duplicate prefixes
        assertParseFailure(parser, " s/1 s/2", expected);
        assertParseFailure(parser, " c/1 c/2", expected);
    }
}
