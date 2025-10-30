package seedu.address.ui;

import static seedu.address.logic.Messages.MESSAGE_NO_SENIORS_PROMPT;

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
 * Panel containing the list of seniors plus a small header area that shows
 * the currently pinned senior (if any).
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
     * Creates a {@code SeniorListPanel} with the given lists and logic.
     *
     * @param seniorList     observable list of seniors from the model (backing list for this panel)
     * @param caregiverList  observable list of caregivers (observed only to refresh when assignments change)
     * @param logic          logic facade used by cards to resolve display data (e.g., assigned caregiver names)
     */
    public SeniorListPanel(ObservableList<Senior> seniorList, ObservableList<Caregiver> caregiverList, Logic logic) {
        super(FXML);
        this.logic = logic;

        // keep a reference to the original list
        this.backingList = seniorList;
        ObservableList<Senior> originalAllSeniors = logic.getUnfilteredSeniorList();

        SortedList<Senior> sorted = getSeniorsSorted(seniorList);
        var visibleList = new javafx.collections.transformation.FilteredList<>(backingList, s -> !isPinnedSenior(s));
        seniorListView.setItems(visibleList);

        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        seniorList.addListener((ListChangeListener<Senior>) change -> seniorListView.refresh());
        seniorListView.setCellFactory(listView -> new SeniorListViewCell(logic));

        caregiverList.addListener((ListChangeListener<Caregiver>) c -> {
            // caregivers changed seniors “assigned caregiver” chip may change
            seniorListView.refresh();
            pinnedHeaderList.refresh();
        });

        // Implement logic when senior list is empty, display message prompt
        Label emptySeniorsListPlaceholder = createEmptySeniorsPlaceholder();
        Runnable refreshPlaceholder = createRefreshPlaceholder(
                originalAllSeniors, emptySeniorsListPlaceholder, MESSAGE_NO_SENIORS_PROMPT);
        initializeSeniorPlaceholder(refreshPlaceholder, originalAllSeniors);
        seniorListView.setPlaceholder(emptySeniorsListPlaceholder);


        // header list: same cell factory so it looks identical
        pinnedHeaderList.setItems(pinnedHeaderItems);
        pinnedHeaderList.setCellFactory(listView -> new SeniorListViewCell(logic));
        pinnedHeaderList.setFocusTraversable(false); // don’t steal keyboard focus

        pinnedHeaderList.setFixedCellSize(-1); // variable row height
        pinnedHeaderList.setPrefHeight(Region.USE_COMPUTED_SIZE);
        pinnedHeaderList.setMinHeight(135);
        pinnedHeaderList.setMaxHeight(140); // max

        // refresh header now and whenever list mutates (pin/unpin changes replace items)
        backingList.addListener(
                (javafx.collections.ListChangeListener<? super Senior>)
                        c -> refreshPinnedHeader());
        refreshPinnedHeader();
    }

    /**
     * Returns a live-sorted view of {@code personList} where pinned seniors appear first,
     * followed by alphabetical ordering by name (case-insensitive).
     *
     * @param personList the observable seniors list to sort
     * @return a {@link SortedList} that updates ordering automatically on mutations
     */
    private static SortedList<Senior> getSeniorsSorted(ObservableList<Senior> personList) {
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
            SeniorCard card = new SeniorCard(senior, logic);
            Region root = card.getRoot(); // UiPart<Region> root of the card

            root.getStyleClass().remove(PINNED_STYLE_CLASS);
            if (senior.getPinned()) {
                root.getStyleClass().add(PINNED_STYLE_CLASS);
            }

            setGraphic(root);
            setText(null);
        }
    }

    /**
     * Updates the header area so it shows exactly one pinned senior if present,
     * or hides the header when there is none.
     */
    private void refreshPinnedHeader() {
        java.util.Optional<Senior> pinnedOpt =
                backingList.stream().filter(Senior::getPinned).findFirst();

        pinnedHeaderItems.setAll(pinnedOpt.map(java.util.List::of).orElse(java.util.List.of()));

        boolean show = !pinnedHeaderItems.isEmpty();
        pinnedHeaderList.setVisible(show);
        pinnedHeaderList.setManaged(show);
    }

    /**
     * Helper used by the main list’s filtered view to exclude the pinned senior.
     *
     * @param s the senior to check
     * @return {@code true} if the senior is pinned
     */
    private boolean isPinnedSenior(seedu.address.model.person.Senior s) {
        return s.getPinned();
    }

    /**
     * Creates and configures the placeholder Label for an empty seniors list.
     * The Label is wrapped, center-aligned, and suitable for use as a ListView placeholder.
     *
     * @return a styled Label to display when no seniors are present
     */
    private Label createEmptySeniorsPlaceholder() {
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
    private Runnable createRefreshPlaceholder(ObservableList<Senior> backingList, Label placeholder, String message) {
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
    private void initializeSeniorPlaceholder(Runnable refreshPlaceholder, ObservableList<Senior> backingList) {
        refreshPlaceholder.run();
        backingList.addListener((ListChangeListener<? super Senior>) c -> refreshPlaceholder.run());
    }


}
