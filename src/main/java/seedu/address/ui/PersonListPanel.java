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
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private static final String PINNED_STYLE_CLASS = "pinned-card";

    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, Logic logic) {
        super(FXML);
        this.logic = logic;

        SortedList<Person> sorted = getPeople(personList);
        personListView.setItems(sorted);

        // keep your existing cell factory
        personListView.setCellFactory(listView -> new PersonListViewCell(logic));


        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        personList.addListener((ListChangeListener<Person>) change -> personListView.refresh());
        personListView.setCellFactory(listView -> new PersonListViewCell(logic));

    }

    private static SortedList<Person> getPeople(ObservableList<Person> personList) {
        Comparator<Person> byPinnedThenName = (a, b) -> {
            boolean ap = isPinned(a);
            boolean bp = isPinned(b);
            
            if (ap != bp) {
                return ap ? -1 : 1;               // pinned first
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
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    private static class PersonListViewCell extends ListCell<Person> {
        private final Logic logic;
        PersonListViewCell(Logic logic) {
            this.logic = logic;
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            // always clear style first (cells are reused!)
            getStyleClass().remove(PINNED_STYLE_CLASS);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1, logic).getRoot());
                // tag the WHOLE CELL when pinned (affects entire card)
                if (isPinned(person)) {
                    if (!getStyleClass().contains(PINNED_STYLE_CLASS)) {
                        getStyleClass().add(PINNED_STYLE_CLASS);
                    }
                }

            }
        }
    }

}
