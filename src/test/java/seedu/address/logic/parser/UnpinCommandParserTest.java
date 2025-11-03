package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnpinCommand;

/**
 * Tests for {@link UnpinCommandParser}.
 */
public class UnpinCommandParserTest {
    private final UnpinCommandParser parser = new UnpinCommandParser();

    @Test
    public void parse_validTokens_success() {
        assertParseSuccess(parser, "", new UnpinCommand(UnpinCommand.Scope.BOTH));
        assertParseSuccess(parser, " s", new UnpinCommand(UnpinCommand.Scope.SENIOR));
        assertParseSuccess(parser, " senior", new UnpinCommand(UnpinCommand.Scope.SENIOR));
        assertParseSuccess(parser, " c", new UnpinCommand(UnpinCommand.Scope.CAREGIVER));
        assertParseSuccess(parser, " cg", new UnpinCommand(UnpinCommand.Scope.CAREGIVER));
        assertParseSuccess(parser, " a", new UnpinCommand(UnpinCommand.Scope.BOTH));
        assertParseSuccess(parser, " all", new UnpinCommand(UnpinCommand.Scope.BOTH));
    }

    @Test
    public void parse_invalidToken_failure() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnpinCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " x", expected);
        assertParseFailure(parser, " seniors", expected);
        assertParseFailure(parser, " caregivers", expected);
    }
}
