package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AssignCommand;

/**
 * Tests for {@link AssignCommandParser}.
 */
public class AssignCommandParserTest {
    private final AssignCommandParser parser = new AssignCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " s/1 c/2", new AssignCommand(1, 2));
        assertParseSuccess(parser, " c/2 s/3", new AssignCommand(3, 2));
    }

    @Test
    public void parse_missingPrefixes_failure() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expected);
        assertParseFailure(parser, " s/1", expected);
        assertParseFailure(parser, " c/1", expected);
        assertParseFailure(parser, " preamble s/1 c/2", expected);
    }

    @Test
    public void parse_emptyOrInvalidIndex_failure() {
        assertParseFailure(parser, " s/ c/1", AssignCommandParser.MESSAGE_MISSING_SENIOR_INDEX);
        assertParseFailure(parser, " s/a c/1", AssignCommandParser.MESSAGE_MISSING_SENIOR_INDEX);
        assertParseFailure(parser, " s/1 c/ ", AssignCommandParser.MESSAGE_MISSING_CAREGIVER_INDEX);
        assertParseFailure(parser, " s/1 c/a ", AssignCommandParser.MESSAGE_MISSING_CAREGIVER_INDEX);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String expectedSenior = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_SENIOR);
        String expectedCaregiver = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_CAREGIVER);
        assertParseFailure(parser, " s/1 s/2 c/3", expectedSenior);
        assertParseFailure(parser, " s/1 c/2 c/3", expectedCaregiver);
    }
}
