package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.PinCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        return new PinCommand(name);

    }

    /** Returns true if all the given prefixes are present (value non-empty). */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
