package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonHasAnyTagPredicate;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parseSingleAllowedLowercaseSuccess() throws Exception {
        FilterCommand cmd = parser.parse(" t/lr ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("lr")));
        assertEquals(expected, cmd);
    }

    @Test
    public void parseMultipleAllowedMixedCaseSuccess() throws Exception {
        FilterCommand cmd = parser.parse(" t/lr t/HR t/mr ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("lr", "hr", "mr")));
        assertEquals(expected, cmd);
    }

    @Test
    public void parse_noTags_failure() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse("   "));
        assertEquals(FilterCommand.MESSAGE_NO_TAG, ex.getMessage());
    }

    @Test
    public void parse_invalid_failure() {
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(" t/hello "));
        assertEquals("Invalid tag: \"hello\".\nAllowed: lr, mr, hr, LR, MR, HR.", ex.getMessage());
    }

    @Test
    void parse_ignoresEmptyTagValues_success() throws Exception {
        FilterCommandParser parser = new FilterCommandParser();
        // Leading empty tag value then a valid one
        FilterCommand cmd = parser.parse(" t/   t/mr ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("mr")));
        assertEquals(expected, cmd);
    }

    @Test
    void parse_blankTagValue() throws Exception {
        // First t/ has only spaces -> triggers `t.isEmpty()` -> `continue;`
        FilterCommand cmd = new FilterCommandParser().parse(" t/   t/mr ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("mr")));
        assertEquals(expected, cmd);
    }

    @Test
    void parse_onlyBlankTagValue() {
        // Only an empty value -> loop hits `continue;`, tags list remains empty -> MESSAGE_NO_TAGS
        ParseException ex = assertThrows(ParseException.class, () -> new FilterCommandParser().parse(" t/    "));
        assertEquals(FilterCommand.MESSAGE_NO_TAG, ex.getMessage());
    }

    @Test
    void parse_blankTagValueThenValidTag() throws Exception {
        // first t/ is only spaces -> triggers t.isEmpty() -> continue;
        FilterCommand cmd = new FilterCommandParser().parse(" t/   t/mr ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("mr")));
        assertEquals(expected, cmd);
    }

    @Test
    void parse_onlyBlankTagValueTreatedAsNoTags() {
        ParseException ex = assertThrows(ParseException.class, () -> new FilterCommandParser().parse(" t/    "));
        assertEquals(FilterCommand.MESSAGE_NO_TAG, ex.getMessage());
    }
}
