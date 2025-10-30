package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.EditPersonDescriptor;

/**
 * Represents an Elderly in the address book.
 */
public class Senior extends Person<Senior> {

    // Senior data fields
    private final Integer seniorId;
    private final Tag riskTag;
    private Caregiver caregiver;

    /**
     * Instantiates a senior with all required and optional senior attributes
     * @param name Name of the senior
     * @param phone Phone number of the senior
     * @param address Address of the senior's home
     * @param riskTag Risk status of the senior i.e. High Risk, Medium Risk or Low Risk
     * @param note Additional caregiving notes for the senior
     * @param caregiver Caregiver assigned to the senior
     * @param seniorId ID of the senior assigned by AddressBook
     * @param pinned boolean variable for if senior is pinned
     */
    public Senior(Name name, Phone phone, Address address, Tag riskTag,
                  Note note, Caregiver caregiver, Integer seniorId, boolean pinned) {
        super(name, phone, address, note, pinned);
        requireAllNonNull(riskTag);
        this.riskTag = (riskTag);
        this.caregiver = caregiver;
        this.seniorId = seniorId;
    }

    /**
     * Immutable factory: return a new Senior with the given id.
     */
    public Senior withId(int id) {
        return new Senior(getName(), getPhone(), getAddress(), getRiskTag(),
                getNote(), caregiver, Integer.valueOf(id), isPinned());
    }

    /**
     * Immutable factory: return a new Senior with the given caregiver.
     */
    public Senior withCaregiver(Caregiver caregiver) {
        return new Senior(getName(), getPhone(), getAddress(), getRiskTag(),
                getNote(), caregiver, getSeniorId(), isPinned());
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Tag getRiskTag() {
        return riskTag;
    }

    /**
     * Returns the caregiver assigned to this senior.
     * Returns null if no caregiver is assigned.
     */
    public Caregiver getCaregiver() {
        return caregiver;
    }

    /**
     * Returns the caregiver's id assigned to this senior.
     * Returns null if no caregiver is assigned.
     */
    public Integer getCaregiverId() {
        if (caregiver == null) {
            return null;
        }
        return caregiver.getCaregiverId();
    }

    /**
     * Returns the ID assigned to this senior.
     */
    public Integer getSeniorId() {
        return seniorId;
    }

    /**
     * Returns the ID assigned to this senior.
     */
    @Override
    public Integer getId() {
        return getSeniorId();
    }

    /**
     * Returns the caregiver assigned to this senior.
     * Returns null if no caregiver is assigned.
     */
    public void setCaregiver(Caregiver cgr) {
        caregiver = cgr;
    }

    /**
     * Returns true if this senior has a caregiver assigned.
     */
    public boolean hasCaregiver() {
        return caregiver != null;
    }

    /**
     * Returns Senior if Senior is pinned
     * @param value where it is a boolean value showing if senior is pinned.
     * @return Senior if Senior is pinned
     */
    public Senior withPinned(boolean value) {
        return new Senior(getName(), getPhone(), getAddress(),
                getRiskTag(), getNote(), getCaregiver(), getSeniorId(), value);
    }

    /**
     * Returns this senior's note.
     * @param newNote
     * @return Note
     */
    public Senior withNote(Note newNote) {
        return new Senior(getName(), getPhone(), getAddress(),
                getRiskTag(), newNote, getCaregiver(), getSeniorId(), isPinned());
    }

    /**
     * Returns a new Senior with the edited attributes
     *
     * @param descriptor
     * @return Senior
     */
    @Override
    public Senior edit(EditPersonDescriptor descriptor) {
        return new Senior(
                descriptor.getName().orElse(getName()),
                descriptor.getPhone().orElse(getPhone()),
                descriptor.getAddress().orElse(getAddress()),
                descriptor.getRiskTags().orElse(getRiskTag()),
                descriptor.getNote().orElse(getNote()),
                descriptor.getCaregiver().orElse(getCaregiver()),
                getSeniorId(),
                descriptor.getPinned().orElse(isPinned())
        );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(super.toString())
                .add("riskTags", riskTag)
                .add("caregiver", caregiver != null ? caregiver.getName() : "No caregiver")
                .add("seniorId", seniorId)
                .toString();
    }
}
