package seedu.address.logic.commands;

import java.util.Optional;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Stores the details to edit a person.
 * Each non-empty field value will replace the corresponding field of the person.
 * Fields that are not edited remain null.
 */
public class EditPersonDescriptor {

    private Name name;
    private Phone phone;
    private Address address;
    private Note note;
    private Set<Tag> riskTags; // Senior only
    private Caregiver caregiver; // actual Caregiver object, set in EditCommand
    private Integer caregiverId; // temporary ID parsed from input
    private Boolean pinned;

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return name != null
                || phone != null
                || address != null
                || note != null
                || riskTags != null
                || caregiver != null
                || caregiverId != null
                || pinned != null;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Optional<Note> getNote() {
        return Optional.ofNullable(note);
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Optional<Set<Tag>> getRiskTags() {
        return Optional.ofNullable(riskTags);
    }

    public void setRiskTags(Set<Tag> riskTags) {
        this.riskTags = riskTags;
    }

    public Optional<Caregiver> getCaregiver() {
        return Optional.ofNullable(caregiver);
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public Optional<Integer> getCaregiverId() {
        return Optional.ofNullable(caregiverId);
    }

    public void setCaregiverId(Integer caregiverId) {
        this.caregiverId = caregiverId;
    }

    public Optional<Boolean> getPinned() {
        return Optional.ofNullable(pinned);
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
}
