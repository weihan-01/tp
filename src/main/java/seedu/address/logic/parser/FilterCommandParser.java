package seedu.address.logic.parser;

import static seedu.address.logic.commands.FilterCommand.MESSAGE_NO_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonHasAnyTagPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    // Accept exactly these; inputs may be upper/lower case, we normalize later.
    private static final Set<String> ALLOWED = Set.of("lr", "mr", "hr", "LR", "MR", "HR");

    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String rawTag = argMultimap.getValue(PREFIX_TAG).toString().trim();
        if (rawTag.isEmpty()) {
            throw new ParseException(MESSAGE_NO_TAG);
        }
        if (!ALLOWED.contains(rawTag)) {
            throw new ParseException("Invalid tag: \"" + rawTag + "\".\nAllowed: lr, mr, hr, LR, MR, HR.");
        }

        return new FilterCommand(new PersonHasAnyTagPredicate(rawTag));
    }
}
