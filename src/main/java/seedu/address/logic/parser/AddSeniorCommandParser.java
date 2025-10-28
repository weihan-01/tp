package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddSeniorCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddSeniorCommandParser implements Parser<AddSeniorCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSeniorCommand
     * and returns an AddSeniorCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSeniorCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_TAG, PREFIX_PHONE,
                        PREFIX_ADDRESS, PREFIX_NOTE, PREFIX_CID);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_TAG, PREFIX_ADDRESS, PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AddSeniorCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_TAG, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_NOTE, PREFIX_CID);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Note note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).orElse(""));
        // Risk tag as a single Tag (HR|MR|LR) so it passes Tag constraints
        Set<Tag> riskTag = ParserUtil.parseRiskTagAsTagSet(argMultimap.getValue(PREFIX_TAG).get());

        Integer caregiverId = null;
        if (argMultimap.getValue(PREFIX_CID).isPresent()) {
            caregiverId = ParserUtil.parseCaregiverId(argMultimap.getValue(PREFIX_CID).orElse(null));
        }

        Senior senior = new Senior(name, phone, address, riskTag,
                note, null, null, false);

        // Return command with fields; Senior (with ID) is created in execute()
        return new AddSeniorCommand(senior, caregiverId);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
