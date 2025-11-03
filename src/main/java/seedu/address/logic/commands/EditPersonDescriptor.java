package seedu.address.logic.commands;

import java.util.Objects;
import java.util.Optional;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;

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
    private Tag riskTag; // Senior only
    private Caregiver caregiver; // actual Caregiver object, set in EditCommand
    private Integer caregiverId; // temporary ID parsed from input
    private Boolean pinned;

    /**
     * Stores the descriptions to edit of the Person
     */
    public EditPersonDescriptor() {
        this.name = null;
        this.phone = null;
        this.address = null;
        this.note = null;
        this.riskTag = null;
        this.caregiver = null;
        this.caregiverId = null;
        this.pinned = null;
    }

    /**
     * Uses an existing descriptor to store the descriptions to edit
     *
     * @param descriptor Existing descriptor with descriptions to edit
     */
    public EditPersonDescriptor(EditPersonDescriptor descriptor) {
        this.name = descriptor.name;
        this.phone = descriptor.phone;
        this.address = descriptor.address;
        this.note = descriptor.note;
        this.riskTag = descriptor.riskTag;
        this.caregiver = descriptor.caregiver;
        this.caregiverId = descriptor.caregiverId;
        this.pinned = descriptor.pinned;
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return name != null
                || phone != null
                || address != null
                || note != null
                || riskTag != null
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

    public Optional<Tag> getRiskTags() {
        return Optional.ofNullable(riskTag);
    }

    public void setRiskTags(Tag riskTag) {
        this.riskTag = riskTag;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EditPersonDescriptor that = (EditPersonDescriptor) o;
        return Objects.equals(name, that.name)
                && Objects.equals(phone, that.phone)
                && Objects.equals(address, that.address)
                && Objects.equals(note, that.note)
                && Objects.equals(riskTag, that.riskTag)
                && Objects.equals(caregiver, that.caregiver)
                && Objects.equals(caregiverId, that.caregiverId)
                && Objects.equals(pinned, that.pinned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, note, riskTag, caregiver, caregiverId, pinned);
    }
}
