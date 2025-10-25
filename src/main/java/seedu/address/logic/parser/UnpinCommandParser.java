package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Locale;

import seedu.address.logic.commands.UnpinCommand;
import seedu.address.logic.parser.exceptions.ParseException;

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
