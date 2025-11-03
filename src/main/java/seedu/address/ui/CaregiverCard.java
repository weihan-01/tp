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

/**
 * A UI component that displays a {@link Caregiver} as a card in the caregiver list.
 * <p>
 * The card shows the caregiver's name, phone, address, note, a caregiver-id chip, and
 * a list of assigned seniors as chips. If the caregiver is marked as pinned, a pin icon
 * is overlaid at the top-right and a subtle pinned background style is applied.
 */
public class CaregiverCard extends UiPart<Region> {

    private static final String FXML = "CaregiverListCard.fxml";

    public final Caregiver caregiver;
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
     * Creates a {@code CaregiverCard} with the given {@code Caregiver} and backing {@code Logic}.
     *
     * @param caregiver the caregiver whose data is displayed by this card (non-null)
     * @param logic     logic facade for querying assignment names; may be null in tests
     */
    public CaregiverCard(Caregiver caregiver, Logic logic) {
        super(FXML);
        // make FlowPane expand and wrap
        HBox.setHgrow(assignedChips, Priority.ALWAYS);
        assignedChips.setMaxWidth(Double.MAX_VALUE);

        // wrap to remaining width of the card (title + padding ≈ 160)
        assignedChips.prefWrapLengthProperty().bind(
                cardPane.widthProperty()
                        .subtract(assignedTitle.widthProperty())
                        .subtract(160));

        // ensure height can grow when chips wrap to multiple lines
        assignedChips.setPrefHeight(Region.USE_COMPUTED_SIZE);
        assignedChips.setMinHeight(Region.USE_PREF_SIZE);

        // nicer spacing
        assignedChips.setHgap(6);
        assignedChips.setVgap(4);

        this.caregiver = caregiver;
        this.logic = logic;

        name.setText(caregiver.getName().fullName);
        phone.setText(caregiver.getPhone().value);
        address.setText(caregiver.getAddress().value);

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
        updatePinIcon(caregiver.getPinned());
        // --- end pin icon ---

        if (caregiver.getPinned()) {
            if (!cardPane.getStyleClass().contains("pinned-bg")) {
                cardPane.getStyleClass().add("pinned-bg");
            }
        } else {
            cardPane.getStyleClass().remove("pinned-bg");
        }
    }

    /**
     * Shows or hides the note label based on the caregiver's note value.
     * <p>
     * This method always sets both {@code managed} and {@code visible} flags to ensure
     * proper layout when list cells are reused by JavaFX.
     */
    private void renderNote() {
        // NOTE: show if non-empty, else hide (cells are reused -> always set both managed & visible)
        String nv = caregiver.getNote() == null ? "" : caregiver.getNote().value;
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
     * Populates the "Seniors:" row with chips for each assigned senior.
     * <p>
     * When {@code logic} is null (e.g., certain tests), the row is skipped.
     * If no seniors are assigned, shows a single "Unassigned" placeholder chip.
     */
    private void renderAssignedRow() {
        assignedRow.setManaged(false);
        assignedRow.setVisible(false);
        assignedChips.getChildren().clear();

        if (logic == null) {
            return;
        }

        assignedTitle.setText("Seniors:");
        var names = logic.getAssignedSeniorNames(caregiver);

        assignedRow.setManaged(true);
        assignedRow.setVisible(true);
        if (names == null || names.isEmpty()) {
            assignedChips.getChildren().setAll(makeAssignedChip("Unassigned", true));
        } else {
            String cgName = caregiver.getName().fullName;
            List<Senior> allSeniors = logic.getAllSeniorList();
            java.util.List<Senior> assigned = new java.util.ArrayList<>();

            for (Senior s : allSeniors) {
                String assignedCg = logic.getAssignedCaregiverName(s);
                if (assignedCg != null && assignedCg.equals(cgName)) {
                    assigned.add(s);
                }
            }

            if (assigned.isEmpty()) {
                assignedChips.getChildren().add(makeAssignedChip("Unassigned", true));
                return;
            }

            for (Senior s : assigned) {
                String n = s.getName().toString();
                if (n.length() > 15) {
                    n = n.substring(0, 15) + "...";
                }
                int idx = s.getId();
                String label;

                if (idx >= 0) {
                    label = "S" + idx + " " + n;
                } else {
                    label = "S— " + n;
                }

                Label chip = makeAssignedChip(label, false);
                assignedChips.getChildren().add(chip);
            }
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
        l.getStyleClass().addAll("tag-chip", "chip-assigned");
        if (emptyStyle) {
            l.getStyleClass().add("chip-assigned-empty");
        }
        return l;
    }

    /**
     * Renders the caregiver ID chip (e.g., {@code C000123}) in the tags row.
     * Hides the row if the caregiver ID is null.
     */
    private void renderChips() {
        tags.getChildren().clear();
        tags.setManaged(false);
        tags.setVisible(false);

        Integer cgId = caregiver.getId();
        if (cgId != null) {
            tags.setManaged(true);
            tags.setVisible(true);
            Label chip = makeChip(String.format("C%06d", cgId));
            chip.getStyleClass().add("chip-caregiver");
            tags.getChildren().add(chip);
        }
    }

    /**
     * Creates a base tag chip label with the shared tag-chip style class.
     *
     * @param text chip text
     * @return a {@link Label} styled as a tag chip
     */
    private static Label makeChip(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("tag-chip");
        return l;
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
