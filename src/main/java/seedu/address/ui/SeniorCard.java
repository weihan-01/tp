package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * A UI component that displays information of a {@code Person}.
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
     * Creates a {@code SeniorCard} with the given {@code Senior} and index to display.
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
        updatePinIcon(senior.isPinned());
        // --- end pin icon ---

        if (senior.isPinned()) {
            if (!cardPane.getStyleClass().contains("pinned-bg")) {
                cardPane.getStyleClass().add("pinned-bg");
            }
        } else {
            cardPane.getStyleClass().remove("pinned-bg");
        }
    }

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

    private void showAssigned(java.util.List<String> names) {
        assignedRow.setManaged(true);
        assignedRow.setVisible(true);
        assignedChips.getChildren().clear();

        if (names == null || names.isEmpty()) {
            assignedChips.getChildren().add(makeAssignedChip("Unassigned", true));
            return;
        }
        names.forEach(n -> assignedChips.getChildren().add(makeAssignedChip(n, false)));
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
     * Risk chip for Senior.
     */
    private void renderChips() {
        tags.getChildren().clear();
        tags.setManaged(false);
        tags.setVisible(false);

        Set<Tag> risk = senior.getRiskTags();
        if (risk != null && !risk.isEmpty()) {
            tags.setManaged(true);
            tags.setVisible(true);

            Label idChip = makeChip(String.format("S%06d", senior.getSeniorId()));
            idChip.getStyleClass().add("chip-senior");
            tags.getChildren().add(idChip);

            risk.stream()
                    .sorted(Comparator.comparing(t -> t.tagName))
                    .forEach(t -> {
                        String chipStr = riskLabel(t.tagName);
                        Label chip = makeChip(chipStr);
                        chip.getStyleClass().add("tag-chip"); // base pill style
                        switch (t.tagName.toUpperCase()) {
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
                    });
        }
    }

    private static Label makeChip(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("tag-chip");
        return l;
    }

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
            return code; // any unexpected tag shows as-is
        }
    }

    private void updatePinIcon(boolean show) {
        if (pinIcon != null) {
            pinIcon.setVisible(show);
        }
    }

}
