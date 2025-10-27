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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    private final javafx.collections.ObservableList<seedu.address.model.person.Senior> backingList;
    private final javafx.collections.ObservableList<Senior> pinnedHeaderItems =
            javafx.collections.FXCollections.observableArrayList();

    @FXML
    private ListView<Senior> seniorListView;
    @FXML
    private ListView<Senior> pinnedHeaderList;

    /**
     * Creates a {@code SeniorListPanel} with the given {@code ObservableList}.
     */
    public SeniorListPanel(ObservableList<Senior> seniorList, ObservableList<Caregiver> caregiverList, Logic logic) {
        super(FXML);
        this.logic = logic;

        // keep a reference to the original list
        this.backingList = seniorList;

        SortedList<Senior> sorted = getSeniorsSorted(seniorList);
        var visibleList = new javafx.collections.transformation.FilteredList<>(backingList, s -> !isPinnedSenior(s));
        seniorListView.setItems(visibleList);

        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        seniorList.addListener((ListChangeListener<Senior>) change -> seniorListView.refresh());
        seniorListView.setCellFactory(listView -> new SeniorListViewCell(logic));

        // header list: same cell factory so it looks identical
        pinnedHeaderList.setItems(pinnedHeaderItems);
        pinnedHeaderList.setCellFactory(listView -> new SeniorListViewCell(logic));
        pinnedHeaderList.setFocusTraversable(false); // don’t steal keyboard focus

        pinnedHeaderList.setFixedCellSize(-1); // variable row height
        pinnedHeaderList.setPrefHeight(Region.USE_COMPUTED_SIZE);
        pinnedHeaderList.setMinHeight(135);
        pinnedHeaderList.setMaxHeight(140); // max

        // refresh header now and whenever list mutates (pin/unpin changes replace items)
        backingList.addListener((javafx.collections.ListChangeListener<? super Senior>) c -> refreshPinnedHeader());
        refreshPinnedHeader();
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

            if (empty || senior == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            // Build the normal card
            SeniorCard card = new SeniorCard(senior, getIndex() + 1, logic);
            Region root = card.getRoot(); // UiPart<Region> root of the card

            root.getStyleClass().remove(PINNED_STYLE_CLASS);
            if (isPinned(senior)) {
                root.getStyleClass().add(PINNED_STYLE_CLASS);
            }

            setGraphic(root);
            setText(null);
        }
    }

    private void refreshPinnedHeader() {
        java.util.Optional<Senior> pinnedOpt =
                backingList.stream().filter(this::isPinnedSenior).findFirst();

        pinnedHeaderItems.setAll(pinnedOpt.map(java.util.List::of).orElse(java.util.List.of()));

        boolean show = !pinnedHeaderItems.isEmpty();
        pinnedHeaderList.setVisible(show);
        pinnedHeaderList.setManaged(show);
    }

    private boolean isPinnedSenior(seedu.address.model.person.Senior s) {
        return isPinned(s);
    }

}
