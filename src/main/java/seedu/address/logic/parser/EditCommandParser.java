package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CAREGIVER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SENIOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Tag;

/**
 * Parses input arguments and creates a new EditCommand object.
 */
public class EditCommandParser implements Parser<EditCommand> {

    @Override
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_SENIOR, PREFIX_CAREGIVER,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_NOTE, PREFIX_TAG);

        boolean isSenior;
        int index;

        // Check which type is being edited
        if (argMultimap.getValue(PREFIX_SENIOR).isPresent() && argMultimap.getValue(PREFIX_CAREGIVER).isPresent()) {
            throw new ParseException("Cannot edit both senior and caregiver at the same time.");
        } else if (argMultimap.getValue(PREFIX_SENIOR).isPresent()) {
            isSenior = true;
            index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SENIOR).get());
        } else if (argMultimap.getValue(PREFIX_CAREGIVER).isPresent()) {
            isSenior = false;
            index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CAREGIVER).get());
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        // Name
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String name = argMultimap.getValue(PREFIX_NAME).get();
            editPersonDescriptor.setName(ParserUtil.parseName(name));
        }

        // Phone
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String phone = argMultimap.getValue(PREFIX_PHONE).get();
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(phone));
        }

        // Address
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String address = argMultimap.getValue(PREFIX_ADDRESS).get();
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(address));
        }

        // Note
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            String note = argMultimap.getValue(PREFIX_NOTE).get();
            editPersonDescriptor.setNote(ParserUtil.parseNote(note));
        }

        // Tags
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            String tag = argMultimap.getValue(PREFIX_TAG).get();
            editPersonDescriptor.setRiskTags(ParserUtil.parseTag(tag));
        }

        // Caregiver ID (only for seniors)
        if (isSenior && argMultimap.getValue(PREFIX_CAREGIVER).isPresent()) {
            String caregiverStr = argMultimap.getValue(PREFIX_CAREGIVER).get();
            editPersonDescriptor.setCaregiverId(ParserUtil.parseIndex(caregiverStr));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor, isSenior);
    }

    private Optional<Tag> parseTagsForEdit(String tag) throws ParseException {
        assert tag != null;

        if (tag.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTag(tag));
    }
}
