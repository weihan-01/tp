package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;

import java.util.stream.Stream;

import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnassignCommand object
 */
public class UnassignCommandParser implements Parser<UnassignCommand> {

    public static final String MESSAGE_MISSING_SENIOR_INDEX = "Senior index cannot be empty.";
    public static final String MESSAGE_MISSING_CAREGIVER_INDEX = "Caregiver index cannot be empty.";

    /**
     * Parses the given {@code String} of arguments in the context of the UnassignCommand
     * and returns an UnassignCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public UnassignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SENIOR, PREFIX_CAREGIVER);

        // Check if both prefixes are present
        if (!arePrefixesPresent(argMultimap, PREFIX_SENIOR, PREFIX_CAREGIVER) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE));
        }

        // Check if senior index is empty
        if (argMultimap.getValue(PREFIX_SENIOR).get().trim().isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_SENIOR_INDEX);
        }

        // Check if caregiver index is empty
        if (argMultimap.getValue(PREFIX_CAREGIVER).get().trim().isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_CAREGIVER_INDEX);
        }

        // Check no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_CAREGIVER, PREFIX_SENIOR);

        Integer seniorIndex;
        Integer caregiverIndex;

        try {
            seniorIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SENIOR).get());
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_MISSING_SENIOR_INDEX, pe);
        }

        try {
            caregiverIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CAREGIVER).get());
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_MISSING_CAREGIVER_INDEX, pe);
        }

        return new UnassignCommand(seniorIndex, caregiverIndex);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
