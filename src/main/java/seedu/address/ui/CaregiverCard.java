package seedu.address.ui;

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

/**
 * A UI component that displays information of a {@code Caregiver}.
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
     * Creates a {@code CaregiverCard} with the given {@code Caregiver} and index to display.
     */
    public CaregiverCard(Caregiver caregiver, int displayedIndex, Logic logic) {
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
        updatePinIcon(caregiver.isPinned());
        // --- end pin icon ---

        if (caregiver.isPinned()) {
            if (!cardPane.getStyleClass().contains("pinned-bg")) {
                cardPane.getStyleClass().add("pinned-bg");
            }
        } else {
            cardPane.getStyleClass().remove("pinned-bg");
        }
    }

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

    private void renderAssignedRow() {
        assignedRow.setManaged(false);
        assignedRow.setVisible(false);
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
            assignedChips.getChildren().setAll(
                    names.stream().map(n -> makeAssignedChip(n, false)).toList());
        }
    }

    private Label makeAssignedChip(String text, boolean emptyStyle) {
        Label l = new Label(text);
        l.getStyleClass().addAll("tag-chip", "chip-assigned");
        if (emptyStyle) {
            l.getStyleClass().add("chip-assigned-empty");
        }
        return l;
    }

    /**
     * Caregiver ID chip.
     */
    private void renderChips() {
        tags.getChildren().clear();
        tags.setManaged(false);
        tags.setVisible(false);

        Integer cgId = caregiver.getCaregiverId();
        if (cgId != null) {
            tags.setManaged(true);
            tags.setVisible(true);
            Label chip = makeChip(String.format("C%06d", cgId));
            chip.getStyleClass().add("chip-caregiver");
            tags.getChildren().add(chip);
        }
    }

    private static Label makeChip(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("tag-chip");
        return l;
    }

    private void updatePinIcon(boolean show) {
        if (pinIcon != null) {
            pinIcon.setVisible(show);
        }
    }

}
