package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Locale;

import seedu.address.logic.commands.UnpinCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the {@code unpin} command.
 * <p>
 * Expected format: {@code unpin c} or {@code unpin s} or {@code unpin a}.
 * If the required prefix is missing or empty, a {@link ParseException} is thrown with the
 * appropriate usage message.
 * @see seedu.address.logic.commands.UnpinCommand
 */
public class UnpinCommandParser implements Parser<UnpinCommand> {
    @Override
    public UnpinCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String t = args.trim();
        if (t.isEmpty()) {
            return new UnpinCommand(UnpinCommand.Scope.BOTH);
        }
        String token = t.toLowerCase(Locale.ROOT);
        switch (token) {
        case "s":
        case "senior":
        case "sen":
            return new UnpinCommand(UnpinCommand.Scope.SENIOR);
        case "c":
        case "caregiver":
        case "cg":
            return new UnpinCommand(UnpinCommand.Scope.CAREGIVER);
        case "a":
        case "all":
            return new UnpinCommand(UnpinCommand.Scope.BOTH);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnpinCommand.MESSAGE_USAGE));
        }
    }
}
