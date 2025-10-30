package seedu.address.ui;

import static seedu.address.logic.Messages.MESSAGE_NO_CAREGIVERS_PROMPT;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;
import seedu.address.model.person.Senior;

/**
 * Panel containing the list of caregivers plus a small header area that shows
 * the currently pinned caregiver (if any).
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
     * Creates a {@code CaregiverListPanel}.
     *
     * @param seniorList     seniors list (observed only for refresh when assignments change)
     * @param caregiverList  caregivers list from the model; also used to derive the pinned header
     * @param logic          logic facade passed down to cards for resolving display data
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

        // Implement logic when senior list is empty, display message prompt
        Label emptySeniorsListPlaceholder = createEmptyCaregiverPlaceholder();
        Runnable refreshPlaceholder = createRefreshPlaceholder(
                backingList, emptySeniorsListPlaceholder, MESSAGE_NO_CAREGIVERS_PROMPT);
        initializeSeniorPlaceholder(refreshPlaceholder, backingList);
        caregiverListView.setPlaceholder(emptySeniorsListPlaceholder);


        seniorList.addListener((ListChangeListener<Senior>) c -> {
            caregiverListView.refresh();
            pinnedHeaderList.refresh();
        });

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

    /**
     * Returns a {@link SortedList} view that keeps caregivers ordered by
     * pinned-first and then by name (case-insensitive).
     *
     * @param personList the observable caregivers list
     * @return a live-sorted view
     */
    private static SortedList<Caregiver> getCaregiverSorted(ObservableList<Caregiver> personList) {
        Comparator<Person> byPinnedThenName = (a, b) -> {
            boolean ap = a.getPinned();
            boolean bp = b.getPinned();

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
     * Custom {@link ListCell} that renders a {@link Caregiver} using a {@link CaregiverCard}.
     * Adds a CSS class to the card root when the caregiver is pinned.
     */
    private static class CaregiverListViewCell extends ListCell<Caregiver> {
        private final Logic logic;

        CaregiverListViewCell(Logic logic) {
            this.logic = logic;
        }

        /**
         * Updates the cell’s content to display the given caregiver.
         * Ensures empty cells are cleared and applies the pinned style when applicable.
         */
        @Override
        protected void updateItem(Caregiver caregiver, boolean empty) {
            super.updateItem(caregiver, empty);

            if (empty || caregiver == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            // Build the normal card
            CaregiverCard card = new CaregiverCard(caregiver, logic);
            Region root = card.getRoot(); // UiPart<Region> root of the card

            root.getStyleClass().remove(PINNED_STYLE_CLASS);
            if (caregiver.getPinned()) {
                root.getStyleClass().add(PINNED_STYLE_CLASS);
            }

            setGraphic(root);
            setText(null);
        }
    }

    /**
     * Refreshes the header ListView so it shows the single pinned caregiver if one exists,
     * or hides the header area otherwise.
     */
    private void refreshPinnedHeader() {
        java.util.Optional<Caregiver> pinnedOpt =
                backingList.stream().filter(Caregiver::getPinned).findFirst();

        pinnedHeaderItems.setAll(pinnedOpt.map(java.util.List::of).orElse(java.util.List.of()));

        boolean show = !pinnedHeaderItems.isEmpty();
        pinnedHeaderList.setVisible(show);
        pinnedHeaderList.setManaged(show);
    }

    /**
     * Predicate helper for filtering out the pinned caregiver from the main list.
     *
     * @param c caregiver to test
     * @return {@code true} if the caregiver is pinned
     */
    private boolean isPinnedCaregiver(seedu.address.model.person.Caregiver c) {
        return c.getPinned();
    }

    /**
     * Creates and configures the placeholder Label for an empty seniors list.
     * The Label is wrapped, center-aligned, and suitable for use as a ListView placeholder.
     *
     * @return a styled Label to display when no seniors are present
     */
    private Label createEmptyCaregiverPlaceholder() {
        Label placeholder = new Label();
        placeholder.setWrapText(true);
        placeholder.setAlignment(Pos.TOP_CENTER);
        placeholder.setTextAlignment(TextAlignment.CENTER);
        return placeholder;
    }

    /**
     * Generates a Runnable that updates the seniors placeholder based on the backing list's emptiness.
     * The runnable sets placeholder text and visibility according to the list state.
     *
     * @param backingList the ObservableList representing the seniors
     * @param placeholder the Label to update as the placeholder
     * @param message the message to display when the list is empty
     * @return a Runnable that refreshes the placeholder's text and visibility
     */
    private Runnable createRefreshPlaceholder(
            ObservableList<Caregiver> backingList, Label placeholder, String message) {
        return () -> {
            boolean isEmpty = backingList.isEmpty();
            placeholder.setText(isEmpty ? message : "");
            placeholder.setVisible(isEmpty);
        };
    }

    /**
     * Initializes the seniors placeholder, ensuring it stays synchronized with the backing list.
     * The placeholder refreshes immediately and on any subsequent list modifications.
     *
     * @param refreshPlaceholder the Runnable responsible for updating the placeholder
     * @param backingList the ObservableList to monitor for changes
     */
    private void initializeSeniorPlaceholder(Runnable refreshPlaceholder, ObservableList<Caregiver> backingList) {
        refreshPlaceholder.run();
        backingList.addListener((ListChangeListener<? super Caregiver>) c -> refreshPlaceholder.run());
    }
}
