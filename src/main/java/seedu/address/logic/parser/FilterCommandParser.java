package seedu.address.logic.parser;

import static seedu.address.logic.commands.FilterCommand.MESSAGE_NO_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
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

        List<String> rawTags = argMultimap.getAllValues(PREFIX_TAG);
        List<String> tags = new ArrayList<>();

        for (String s : rawTags) {
            String t = s.trim();
            if (t.isEmpty()) {
                continue;
            }
            if (!ALLOWED.contains(t)) {
                throw new ParseException("Invalid tag: \"" + t + "\".\nAllowed: lr, mr, hr, LR, MR, HR.");
            }
            tags.add(t.toLowerCase()); // normalize to lr/mr/hr
        }

        if (tags.isEmpty()) {
            throw new ParseException(MESSAGE_NO_TAG);
        }

        return new FilterCommand(new PersonHasAnyTagPredicate(tags));
    }
}
