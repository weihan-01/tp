package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnassignCommand;

/**
 * Contains unit tests for {@code UnassignCommandParser}.
 */
public class UnassignCommandParserTest {

    private final UnassignCommandParser parser = new UnassignCommandParser();

    /**
     * Tests that a valid input string with both senior and caregiver indices
     * is successfully parsed into an {@code UnassignCommand}.
     */
    @Test
    public void parse_validArgs_returnsUnassignCommand() {
        String userInput = " " + PREFIX_SENIOR + "1 " + PREFIX_CAREGIVER + "2";
        UnassignCommand expectedCommand = new UnassignCommand(1, 2);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    /**
     * Tests that missing the senior prefix throws a {@code ParseException}.
     */
    @Test
    public void parse_missingSeniorPrefix_throwsParseException() {
        String userInput = " " + PREFIX_CAREGIVER + "2";
        assertParseFailure(parser, userInput,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnassignCommand.MESSAGE_USAGE));
    }

    /**
     * Tests that missing the caregiver prefix throws a {@code ParseException}.
     */
    @Test
    public void parse_missingCaregiverPrefix_throwsParseException() {
        String userInput = " " + PREFIX_SENIOR + "1";
        assertParseFailure(parser, userInput,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnassignCommand.MESSAGE_USAGE));
    }

    /**
     * Tests that an empty string for both indices throws a {@code ParseException}.
     */
    @Test
    public void parse_emptyIndices_throwsParseException() {
        String userInput = " " + PREFIX_SENIOR + " " + PREFIX_CAREGIVER + " ";
        assertParseFailure(parser, userInput, "Senior index cannot be empty.");
    }

    /**
     * Tests that non-numeric indices throw a {@code ParseException}.
     */
    @Test
    public void parse_nonNumericIndices_throwsParseException() {
        String userInput = " " + PREFIX_SENIOR + "a " + PREFIX_CAREGIVER + "b";
        assertParseFailure(parser, userInput, "Senior index cannot be empty.");
    }

}
