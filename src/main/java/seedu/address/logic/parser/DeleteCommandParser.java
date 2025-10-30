package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;

import java.util.stream.Stream;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final String MESSAGE_MULTIPLE_PREFIXES =
            "Please specify only one senior and/or one caregiver. Multiple 's/' or 'c/' prefixes detected.";

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SENIOR, PREFIX_CAREGIVER);

        // Check presence
        if (!arePrefixesPresent(argMultimap, PREFIX_SENIOR) && !arePrefixesPresent(argMultimap, PREFIX_CAREGIVER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        // Check for multiple occurrences: reject duplicates like "delete s/1 s/2" or "delete c/1 c/2"
        if (argMultimap.getAllValues(PREFIX_SENIOR).size() > 1
                || argMultimap.getAllValues(PREFIX_CAREGIVER).size() > 1) {
            throw new ParseException(MESSAGE_MULTIPLE_PREFIXES);
        }

        Integer seniorId = null;
        if (argMultimap.getValue(PREFIX_SENIOR).isPresent()) {
            seniorId = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SENIOR).get());
        }

        Integer caregiverId = null;
        if (argMultimap.getValue(PREFIX_CAREGIVER).isPresent()) {
            caregiverId = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CAREGIVER).get());
        }

        return new DeleteCommand(seniorId, caregiverId);
    }

    /** Returns true if all the given prefixes are present (value non-empty). */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
