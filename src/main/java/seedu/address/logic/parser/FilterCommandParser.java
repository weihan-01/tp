package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonHasAnyTagPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        List<String> rawTags = argMultimap.getAllValues(PREFIX_TAG);
        List<String> tags = new ArrayList<>();
        for (String s : rawTags) {
            String t = s.trim();
            if (!t.isEmpty()) {
                tags.add(t);
            }
        }

        if (tags.isEmpty()) {
            throw new ParseException(FilterCommand.MESSAGE_NO_TAGS);
        }

        return new FilterCommand(new PersonHasAnyTagPredicate(tags));
    }
}
