package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;

import java.util.stream.Stream;

import seedu.address.logic.commands.PinCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the {@code pin} command.
 * <p>
 * Expected format: {@code pin n/NAME}. This parser extracts the {@code NAME} value from the
 * {@code n/} prefix and constructs a {@link seedu.address.logic.commands.PinCommand}.
 * If the required prefix is missing or empty, a {@link ParseException} is thrown with the
 * appropriate usage message.
 *
 * @see seedu.address.logic.commands.PinCommand
 */
public class PinCommandParser implements Parser<PinCommand> {

    @Override
    public PinCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SENIOR, PREFIX_CAREGIVER);

        if (!arePrefixesPresent(argMultimap, PREFIX_SENIOR) && !arePrefixesPresent(argMultimap, PREFIX_CAREGIVER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
        }

        Integer seniorId = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SENIOR).get());
        Integer caregiverId = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CAREGIVER).get());

        return new PinCommand(seniorId, caregiverId);
    }

    /** Returns true if all the given prefixes are present (value non-empty). */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
