package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.ID_ONE_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCaregiverCommand;
import seedu.address.logic.commands.AddSeniorCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonHasAnyTagPredicate;
import seedu.address.model.person.Senior;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.SeniorBuilder;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + PREFIX_SENIOR + ID_ONE_PERSON);
        assertEquals(new DeleteCommand(ID_ONE_PERSON, null), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Senior senior = new SeniorBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(senior).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + PREFIX_SENIOR + ID_ONE_PERSON + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(ID_ONE_PERSON, descriptor, true), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_addCaregiver() throws Exception {
        assertTrue(parser.parseCommand(AddCaregiverCommand.COMMAND_WORD
                + " n/Amy p/11111111 a/Somewhere nt/Note") instanceof AddCaregiverCommand);
    }

    @Test
    public void parseCommand_addSenior() throws Exception {
        assertTrue(parser.parseCommand(AddSeniorCommand.COMMAND_WORD
                + " n/Bob t/HR p/22222222 a/Addr") instanceof AddSeniorCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_filterSingleTag() throws Exception {
        AddressBookParser parser = new AddressBookParser();
        FilterCommand command = (FilterCommand) parser.parseCommand("filter t/lr");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("lr")));
        assertEquals(expected, command);
    }

    @Test
    public void parseCommand_filterMultipleTags() throws Exception {
        AddressBookParser parser = new AddressBookParser();
        // First t/ is empty on purpose to cover the "trim -> empty -> continue" branch in your parser
        FilterCommand command = (FilterCommand) parser.parseCommand("filter t/   t/LR t/hr  ");
        FilterCommand expected = new FilterCommand(new PersonHasAnyTagPredicate(List.of("lr", "hr")));
        assertEquals(expected, command);
    }

    @Test
    public void parseCommand_unassign_returnsUnassignCommand() throws Exception {
        AddressBookParser parser = new AddressBookParser();
        assertEquals(UnassignCommand.class,
                parser.parseCommand(UnassignCommand.COMMAND_WORD + " s/1 c/2").getClass());
    }
}
