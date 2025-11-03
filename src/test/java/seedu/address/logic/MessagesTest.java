package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_formatsCorrectly() {
        String msg = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_SENIOR, CliSyntax.PREFIX_CAREGIVER);
        String header = Messages.MESSAGE_DUPLICATE_FIELDS;
        String body = msg.substring(header.length());
        boolean hasSenior = body.contains(CliSyntax.PREFIX_SENIOR.toString());
        boolean hasCare = body.contains(CliSyntax.PREFIX_CAREGIVER.toString());
        assertEquals(true, hasSenior && hasCare);
    }

    @Test
    public void formatSenior_includesOptionalFieldsWhenPresent() {
        Senior s = new Senior(new Name("Alice"), new Phone("91234567"), new Address("Addr"), new Tag("HR"),
                new Note("Note"), null, 1, false);
        String formatted = Messages.formatSenior(s);
        // Risk, phone, address, notes present
        assertEquals(true, formatted.contains("Risk: HR"));
        assertEquals(true, formatted.contains("Phone: 91234567"));
        assertEquals(true, formatted.contains("Address: Addr"));
        assertEquals(true, formatted.contains("Notes: Note"));
    }

    @Test
    public void formatSenior_omitsEmptyNotes() {
        Senior senior = new Senior(new Name("Bob"), new Phone("91234567"), new Address("Addr"), new Tag("LR"),
                new Note(""), null, 1, false);
        String formatted = Messages.formatSenior(senior);
        // No Notes label when empty
        assertEquals(false, formatted.contains("Notes:"));
    }

    @Test
    public void formatCaregiver_includesOptionalFieldsWhenPresent() {
        Caregiver caregiver = new Caregiver(new Name("Carol"), new Phone("90000000"),
                new Address("A"), new Note("N"), 1, false);
        String formatted = Messages.formatCaregiver(caregiver);
        assertEquals(true, formatted.contains("Phone: 90000000"));
        assertEquals(true, formatted.contains("Address: A"));
        assertEquals(true, formatted.contains("Notes: N"));
    }

    @Test
    public void formatCaregiver_omitsMissingFields() {
        Caregiver c = new Caregiver(new Name("Carol"), new Phone("90000000"),
                new Address(""), new Note(""), 1, false);
        String formatted = Messages.formatCaregiver(c);

        // Address and Notes omitted when blank
        assertEquals(false, formatted.contains("Address:"));
        assertEquals(false, formatted.contains("Notes:"));
    }
}
