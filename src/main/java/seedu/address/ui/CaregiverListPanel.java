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
public class CaregiverListPanel extends UiPart<Region> {
    private static final String FXML = "CaregiverListPanel.fxml";
    private static final String PINNED_STYLE_CLASS = "pinned-card";

    private final Logger logger = LogsCenter.getLogger(CaregiverListPanel.class);
    private final Logic logic;

    private final javafx.collections.ObservableList<seedu.address.model.person.Caregiver> backingList;

    @FXML
    private ListView<Caregiver> caregiverListView;
    @FXML
    private javafx.scene.layout.HBox pinnedBar;

    /**
     * Creates a {@code CaregiverListPanel} with the given {@code ObservableList}.
     */
    public CaregiverListPanel(ObservableList<Senior> seniorList, ObservableList<Caregiver> caregiverList, Logic logic) {
        super(FXML);
        this.logic = logic;

        // keep a reference to the original list
        this.backingList = caregiverList;

        SortedList<Caregiver> sorted = getCaregiverSorted(caregiverList);
        var visibleList = new javafx.collections.transformation.FilteredList<>(backingList, c -> !isPinnedCaregiver(c));
        caregiverListView.setItems(visibleList);

        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        caregiverList.addListener((ListChangeListener<Caregiver>) change -> caregiverListView.refresh());
        caregiverListView.setCellFactory(listView -> new CaregiverListViewCell(logic));

        // refresh header now and whenever list mutates (pin/unpin changes replace items)
        backingList.addListener((javafx.collections.ListChangeListener<? super seedu.address.model.person.Caregiver>) c -> refreshPinnedBar());
        refreshPinnedBar();
    }

    private static SortedList<Caregiver> getCaregiverSorted(ObservableList<Caregiver> personList) {
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
     * Custom {@code ListCell} that displays the graphics of a {@code Caregiver} using a {@code CaregiverCard}.
     */
    private static class CaregiverListViewCell extends ListCell<Caregiver> {
        private final Logic logic;

        CaregiverListViewCell(Logic logic) {
            this.logic = logic;
        }

        @Override
        protected void updateItem(Caregiver caregiver, boolean empty) {
            super.updateItem(caregiver, empty);

            // always clear style first (cells are reused!)
            getStyleClass().remove(PINNED_STYLE_CLASS);

            if (empty || caregiver == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CaregiverCard(caregiver, getIndex() + 1, logic).getRoot());
                // tag the WHOLE CELL when pinned (affects entire card)
                if (isPinned(caregiver)) {
                    if (!getStyleClass().contains(PINNED_STYLE_CLASS)) {
                        getStyleClass().add(PINNED_STYLE_CLASS);
                    }
                }
            }
        }
    }

    private void refreshPinnedBar() {
        // find first pinned caregiver
        java.util.Optional<seedu.address.model.person.Caregiver> pinnedOpt =
                backingList.stream().filter(this::isPinnedCaregiver).findFirst();

        pinnedBar.getChildren().clear();
        if (pinnedOpt.isPresent()) {
            var pinned = pinnedOpt.get();
            // reuse your existing card
            var card = new CaregiverCard(pinned, 1); // index not shown/important in header
            pinnedBar.getChildren().add(card.getRoot());
            pinnedBar.setVisible(true);
            pinnedBar.setManaged(true);
        } else {
            pinnedBar.setVisible(false);
            pinnedBar.setManaged(false);
        }
    }

    private boolean isPinnedCaregiver(seedu.address.model.person.Caregiver c) {
        return isPinned(c);
    }

}
