package seedu.address.ui;

import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Tag;

/**
 * A UI component that displays a {@link Senior} as a card in the seniors list.
 * <p>
 * The card renders basic fields (name, phone, address, note), risk-tags chips, and the
 * assigned caregiver chip. When the senior is marked as pinned, a pin icon is shown at
 * the top-right and the card receives a subtle pinned background style.
 */
public class SeniorCard extends UiPart<Region> {

    private static final String FXML = "SeniorListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Senior senior;
    private ImageView pinIcon;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label note;
    @FXML
    private FlowPane tags;
    @FXML
    private HBox assignedRow;
    @FXML
    private Label assignedTitle;
    @FXML
    private FlowPane assignedChips;

    private final Logic logic;


    /**
     * Creates a {@code SeniorCard} that renders the given {@link Senior}.
     *
     * @param senior the senior to display (non-null)
     * @param logic  logic facade for resolving assigned caregiver names; may be null in tests
     */
    public SeniorCard(Senior senior, Logic logic) {
        super(FXML);
        // make FlowPane expand and wrap
        HBox.setHgrow(assignedChips, Priority.ALWAYS);
        assignedChips.setMaxWidth(Double.MAX_VALUE);

        // wrap to remaining width of the card (title + padding ≈ 160; adjust if needed)
        assignedChips.prefWrapLengthProperty().bind(
                cardPane.widthProperty()
                        .subtract(assignedTitle.widthProperty())
                        .subtract(160));

        // ensure height can grow when chips wrap to multiple lines
        assignedChips.setPrefHeight(Region.USE_COMPUTED_SIZE);
        assignedChips.setMinHeight(Region.USE_PREF_SIZE);

        // optional: nicer spacing
        assignedChips.setHgap(6);
        assignedChips.setVgap(4);
        this.senior = senior;
        this.logic = logic;

        name.setText(senior.getName().fullName);
        phone.setText(senior.getPhone().value);
        address.setText(senior.getAddress().value);

        renderNote();
        renderAssignedRow();
        renderChips();

        // PIN
        // --- pin icon ---
        pinIcon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/images/pin.png")).toExternalForm()
        ));
        pinIcon.setFitWidth(18);
        pinIcon.setPreserveRatio(true);
        pinIcon.setSmooth(true);
        pinIcon.setManaged(false);
        pinIcon.setMouseTransparent(true);
        pinIcon.getStyleClass().add("pin-icon");

        cardPane.getChildren().add(pinIcon);

        // absolute top-right inside the card
        pinIcon.setLayoutY(6);
        pinIcon.layoutXProperty().bind(
                cardPane.widthProperty().subtract(pinIcon.fitWidthProperty()).subtract(8)
        );
        // show only if pinned
        updatePinIcon(senior.getPinned());
        // --- end pin icon ---

        if (senior.getPinned()) {
            if (!cardPane.getStyleClass().contains("pinned-bg")) {
                cardPane.getStyleClass().add("pinned-bg");
            }
        } else {
            cardPane.getStyleClass().remove("pinned-bg");
        }
    }

    /**
     * Shows or hides the note label based on the senior's note value.
     * <p>
     * Both {@code managed} and {@code visible} are set to ensure correct layout in reused cells.
     */
    private void renderNote() {
        // NOTE: show if non-empty, else hide (cells are reused -> always set both managed & visible)
        String nv = senior.getNote() == null ? "" : senior.getNote().value;
        if (nv != null && !nv.trim().isEmpty()) {
            note.setText(nv);
            note.setManaged(true);
            note.setVisible(true);
        } else {
            note.setText("");
            note.setManaged(false);
            note.setVisible(false);
        }
    }

    /**
     * Populates the "Caregiver:" row with a single chip of the assigned caregiver's name.
     * If none is assigned, shows an "Unassigned" placeholder chip.
     */
    private void renderAssignedRow() {
        assignedRow.setManaged(false);
        assignedRow.setVisible(false);

        if (logic == null) {
            return; // tests / safety
        }

        assignedTitle.setText("Caregiver:");
        String cgName = logic.getAssignedCaregiverName(senior);
        showAssigned(cgName == null || cgName.isBlank()
                ? List.of()
                : List.of(cgName));
    }

    /**
     * Updates the assigned-chip container with the given names.
     *
     * @param names list of names to render; empty or null shows a single "Unassigned" chip
     */
    private void showAssigned(java.util.List<String> names) {
        assignedRow.setManaged(true);
        assignedRow.setVisible(true);
        assignedChips.getChildren().clear();

        if (logic == null) {
            return;
        }

        if (names == null || names.isEmpty()) {
            Label chip = makeAssignedChip("Unassigned", true);
            assignedChips.getChildren().add(chip);
            return;
        }

        Caregiver found = null;
        String cgName = logic.getAssignedCaregiverName(senior);
        List<Caregiver> allCaregivers = logic.getAllCaregiverList();

        for (Caregiver c : allCaregivers) {

            String full = null;
            if (c.getName() != null) {
                full = c.getName().fullName;
            }

            if (cgName.equals(full)) {
                found = c;
                break;
            }
        }

        if (found != null) {
            Integer idx = found.getId();
            String n = found.getName().toString();
            if (n.length() > 15) {
                n = n.substring(0, 15) + "...";
            }
            String label;
            if (idx >= 0) {
                label = "C" + idx + " " + n;
            } else {
                label = "C— " + n;
            }
            Label chip = makeAssignedChip(label, false);
            assignedChips.getChildren().add(chip);

        }
    }

    /**
     * Creates a styled chip label for the assigned row.
     *
     * @param text       chip text
     * @param emptyStyle whether to apply the "empty" variant (used for "Unassigned")
     * @return a {@link Label} configured with chip styles
     */
    private Label makeAssignedChip(String text, boolean emptyStyle) {
        Label l = new Label(text);
        l.getStyleClass().addAll("tags-chip", "chip-assigned");
        if (emptyStyle) {
            l.getStyleClass().add("chip-assigned-empty");
        }
        return l;
    }

    /**
     * Renders the senior's risk chips and ID chip in the tags row.
     * Hides the row if no risk tags are present.
     */
    private void renderChips() {
        tags.getChildren().clear();
        tags.setManaged(false);
        tags.setVisible(false);

        Tag risk = senior.getRiskTag();
        if (risk != null) {
            tags.setManaged(true);
            tags.setVisible(true);

            Label idChip = makeChip(String.format("S%06d", senior.getId()));
            idChip.getStyleClass().add("chip-senior");
            tags.getChildren().add(idChip);

            String chipStr = riskLabel(risk.getTagName());
            Label chip = makeChip(chipStr);
            chip.getStyleClass().add("tag-chip"); // base pill style

            switch (risk.getTagName().toUpperCase()) {
            case "HR":
                chip.getStyleClass().add("chip-hr");
                break; // red
            case "MR":
                chip.getStyleClass().add("chip-mr");
                break; // orange
            case "LR":
                chip.getStyleClass().add("chip-lr");
                break; // yellow
            default:
                break;
            }
            tags.getChildren().add(chip);
        }
    }

    /**
     * Creates a base tags chip label with the shared tags-chip style class.
     *
     * @param text chip text
     * @return a {@link Label} styled as a tags chip
     */
    private static Label makeChip(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("tags-chip");
        return l;
    }

    /**
     * Returns the display label for a risk code.
     *
     * @param code risk tags code (e.g., "HR", "MR", "LR")
     * @return human-readable label or the original code if unrecognized
     */
    private static String riskLabel(String code) {
        if (code == null) {
            return "";
        }
        switch (code.trim().toUpperCase()) {
        case "HR":
            return "High Risk";
        case "MR":
            return "Medium Risk";
        case "LR":
            return "Low Risk";
        default:
            return code; // any unexpected tags shows as-is
        }
    }

    /**
     * Shows or hides the pin icon overlay without affecting layout.
     *
     * @param show {@code true} to display the pin; {@code false} to hide it
     */
    private void updatePinIcon(boolean show) {
        if (pinIcon != null) {
            pinIcon.setVisible(show);
        }
    }

}
