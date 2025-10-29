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
 * Expected format: {@code pin c/id or pin s/id}.
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

        // if prefix is present
        boolean hasSenior = argMultimap.getValue(PREFIX_SENIOR).isPresent();
        boolean hasCaregiver = argMultimap.getValue(PREFIX_CAREGIVER).isPresent();

        // must be EXACTLY one of s/ or c/
        if ((hasSenior && hasCaregiver) || (!hasSenior && !hasCaregiver)) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
        }

        // reject duplicates like "pin s/1 s/2" or "pin c/1 c/2"
        if (argMultimap.getAllValues(PREFIX_SENIOR).size() > 1
                || argMultimap.getAllValues(PREFIX_CAREGIVER).size() > 1) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
        }

        Integer seniorId = null;
        Integer caregiverId = null;


        try {
            if (hasSenior) {
                String raw = argMultimap.getValue(PREFIX_SENIOR).get().trim();
                // guard to fail fast on non-digits (example: "s/A" is rejected)
                if (!raw.matches("\\d+")) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
                }
                seniorId = ParserUtil.parseIndex(raw); // typically enforces >0, numeric
            } else { // hasCaregiver
                String raw = argMultimap.getValue(PREFIX_CAREGIVER).get().trim();
                if (!raw.matches("\\d+")) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE));
                }
                caregiverId = ParserUtil.parseIndex(raw);
            }
        } catch (ParseException e) {
            // Normalize any parse error to the command's usage message
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PinCommand.MESSAGE_USAGE), e);
        }

        return new PinCommand(seniorId, caregiverId);
    }

}
