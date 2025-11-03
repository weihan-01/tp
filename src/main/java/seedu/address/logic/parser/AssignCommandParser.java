package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;

import java.util.stream.Stream;

import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AssignCommand object
 */
public class AssignCommandParser implements Parser<AssignCommand> {

    public static final String MESSAGE_MISSING_SENIOR_INDEX = "Senior index must be single-value, "
            + "numeric and must not be empty.";
    public static final String MESSAGE_MISSING_CAREGIVER_INDEX = "Caregiver index must be single-value, "
            + "numeric and must not be empty.";

    /**
     * Parses the given {@code String} of arguments in the context of the AssignCommand
     * and returns an AssignCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SENIOR, PREFIX_CAREGIVER);

        // Check if both prefixes are present
        if (!arePrefixesPresent(argMultimap, PREFIX_SENIOR, PREFIX_CAREGIVER) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
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

        return new AssignCommand(seniorIndex, caregiverIndex);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
