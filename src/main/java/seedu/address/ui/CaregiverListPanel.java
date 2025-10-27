package seedu.address.ui;

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
    private final javafx.collections.ObservableList<Caregiver> pinnedHeaderItems =
            javafx.collections.FXCollections.observableArrayList();

    @FXML
    private ListView<Caregiver> caregiverListView;
    @FXML
    private ListView<Caregiver> pinnedHeaderList;

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

        // header list: same cell factory so it looks identical
        pinnedHeaderList.setItems(pinnedHeaderItems);
        pinnedHeaderList.setCellFactory(listView -> new CaregiverListPanel.CaregiverListViewCell(logic));
        pinnedHeaderList.setFocusTraversable(false); // don’t steal keyboard focus

        pinnedHeaderList.setFixedCellSize(-1); // variable row height
        pinnedHeaderList.setPrefHeight(Region.USE_COMPUTED_SIZE);
        pinnedHeaderList.setMinHeight(135);
        pinnedHeaderList.setMaxHeight(135); // max

        // refresh header now and whenever list mutates (pin/unpin changes replace items)
        backingList.addListener(
                (javafx.collections.ListChangeListener<? super seedu.address.model.person.Caregiver>)
                        c -> refreshPinnedHeader());
        refreshPinnedHeader();
    }

    private static SortedList<Caregiver> getCaregiverSorted(ObservableList<Caregiver> personList) {
        Comparator<Person> byPinnedThenName = (a, b) -> {
            boolean ap = a.isPinned();
            boolean bp = b.isPinned();

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

            if (empty || caregiver == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            // Build the normal card
            CaregiverCard card = new CaregiverCard(caregiver, getIndex() + 1, logic);
            Region root = card.getRoot(); // UiPart<Region> root of the card

            root.getStyleClass().remove(PINNED_STYLE_CLASS);
            if (caregiver.isPinned()) {
                root.getStyleClass().add(PINNED_STYLE_CLASS);
            }

            setGraphic(root);
            setText(null);
        }
    }

    private void refreshPinnedHeader() {
        java.util.Optional<Caregiver> pinnedOpt =
                backingList.stream().filter(Caregiver::isPinned).findFirst();

        pinnedHeaderItems.setAll(pinnedOpt.map(java.util.List::of).orElse(java.util.List.of()));

        boolean show = !pinnedHeaderItems.isEmpty();
        pinnedHeaderList.setVisible(show);
        pinnedHeaderList.setManaged(show);
    }

    private boolean isPinnedCaregiver(seedu.address.model.person.Caregiver c) {
        return c.isPinned();
    }

}
