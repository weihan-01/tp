package seedu.address.ui;

import static seedu.address.logic.commands.PinCommand.isPinned;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Panel containing the list of persons.
 */
public class SeniorListPanel extends UiPart<Region> {
    private static final String FXML = "SeniorListPanel.fxml";
    private static final String PINNED_STYLE_CLASS = "pinned-card";

    private final Logger logger = LogsCenter.getLogger(SeniorListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Senior> seniorListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public SeniorListPanel(ObservableList<Senior> seniorList, ObservableList<Caregiver> caregiverList, Logic logic) {
        super(FXML);
        this.logic = logic;

        SortedList<Senior> sorted = getSeniorsSorted(seniorList);
        seniorListView.setItems(sorted);

        // keep your existing cell factory
        seniorListView.setCellFactory(listView -> new SeniorListPanel.SeniorListViewCell(logic));

        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        seniorList.addListener((ListChangeListener<Senior>) change -> seniorListView.refresh());
        caregiverList.addListener((ListChangeListener<Caregiver>) change -> seniorListView.refresh());
        seniorListView.setCellFactory(listView -> new SeniorListViewCell(logic));
    }

    private static SortedList<Senior> getSeniorsSorted(ObservableList<Senior> personList) {
        Comparator<Person> byPinnedThenName = (a, b) -> {
            boolean ap = isPinned(a);
            boolean bp = isPinned(b);

            if (ap != bp) {
                return ap ? -1 : 1;
            }
            // tie-breaker: alphabetical by name
            String an = a == null ? "" : a.getName().fullName;
            String bn = b == null ? "" : b.getName().fullName;
            return an.compareToIgnoreCase(bn);
        };

        // Wrap in SortedList so it re-sorts automatically on updates
        return new SortedList<>(personList, byPinnedThenName);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Senior} using a {@code SeniorCard}.
     */
    private static class SeniorListViewCell extends ListCell<Senior> {
        private final Logic logic;
        SeniorListViewCell(Logic logic) {
            this.logic = logic;
        }

        @Override
        protected void updateItem(Senior senior, boolean empty) {
            super.updateItem(senior, empty);

            // always clear style first (cells are reused!)
            getStyleClass().remove(PINNED_STYLE_CLASS);

            if (empty || senior == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SeniorCard(senior, getIndex() + 1, logic).getRoot());
                // tag the WHOLE CELL when pinned (affects entire card)
                if (isPinned(senior)) {
                    if (!getStyleClass().contains(PINNED_STYLE_CLASS)) {
                        getStyleClass().add(PINNED_STYLE_CLASS);
                    }
                }
            }
        }
    }

}
