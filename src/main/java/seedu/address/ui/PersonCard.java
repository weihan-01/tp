package seedu.address.ui;

import java.util.Comparator;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;
import seedu.address.model.person.Caregiver;
import seedu.address.model.tag.Tag;

public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML private HBox cardPane;
    @FXML private Label name;
    @FXML private Label id;
    @FXML private Label phone;
    @FXML private Label address;
    @FXML private Label note;         // <-- NEW
    @FXML private FlowPane tags;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);

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

        renderChips();
    }

    /** Risk chip for Senior; caregiver id chip for Caregiver; hide row otherwise. */
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
                            Label chip = new Label(t.tagName);
                            chip.getStyleClass().add("tag-chip"); // base pill style
                            switch (t.tagName.toUpperCase()) {
                            case "HR": chip.getStyleClass().add("chip-hr"); break; // red
                            case "MR": chip.getStyleClass().add("chip-mr"); break; // orange
                            case "LR": chip.getStyleClass().add("chip-lr"); break; // yellow
                            default: break;
                            }
                            tags.getChildren().add(chip);
                        });
            }
        } else if (person instanceof Caregiver) {
            Caregiver c = (Caregiver) person;
            String cgId = c.getCaregiverId();
            if (cgId != null && !cgId.isBlank()) {
                tags.setManaged(true);
                tags.setVisible(true);
                Label chip = makeChip(cgId);
                chip.getStyleClass().add("chip-caregiver");
                tags.getChildren().add(chip);
            }
        }
    }

    private static Label makeChip(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("tag-chip");
        return l;
    }
}
