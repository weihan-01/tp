package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;
import seedu.address.model.tag.Tag;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class CaregiverCard extends UiPart<Region> {

    private static final String FXML = "CaregiverListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
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
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public CaregiverCard(Person person, int displayedIndex, Logic logic) {
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
        this.person = person;
        this.logic = logic;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);

        renderNote();
        renderAssignedRow();
        renderChips();
    }

    public CaregiverCard(Person person, int displayedIndex) {
        this(person, displayedIndex, null);
    }

    private void renderNote() {
        // NOTE: show if non-empty, else hide (cells are reused -> always set both managed & visible)
        String nv = person.getNote() == null ? "" : person.getNote().value;
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

        if (person instanceof Senior s) {
            assignedTitle.setText("Caregiver:");
            String cgName = logic.getAssignedCaregiverName(s);
            showAssigned(cgName == null || cgName.isBlank()
                    ? List.of()
                    : List.of(cgName));
        } else if (person instanceof Caregiver c) {
            assignedTitle.setText("Seniors:");
            var names = (logic == null) ? List.<String>of()
                    : logic.getAssignedSeniorNames(c);

            assignedRow.setManaged(true);
            assignedRow.setVisible(true);

            if (names == null || names.isEmpty()) {
                assignedChips.getChildren().setAll(makeAssignedChip("Unassigned", true));
            } else {
                assignedChips.getChildren().setAll(
                        names.stream().map(n -> makeAssignedChip(n, false)).toList()
                );
            }
        }
    }

    private void showAssigned(List<String> names) {
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
     * Risk chip for Senior; caregiver id chip for Caregiver; hide row otherwise.
     */
    private void renderChips() {
        tags.getChildren().clear();
        tags.setManaged(false);
        tags.setVisible(false);

        if (person instanceof Senior) {
            Senior s = (Senior) person;
            Set<Tag> risk = s.getRiskTags();
            if (risk != null && !risk.isEmpty()) {
                tags.setManaged(true);
                tags.setVisible(true);
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
        } else if (person instanceof Caregiver) {
            Caregiver c = (Caregiver) person;
            int cgId = c.getCaregiverId();
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
}
