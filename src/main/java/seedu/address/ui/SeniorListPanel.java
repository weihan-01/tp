package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
public class SeniorListPanel extends UiPart<Region> {
    private static final String FXML = "SeniorListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(SeniorListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Person> seniorListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public SeniorListPanel(ObservableList<Person> seniorList, Logic logic) {
        super(FXML);
        this.logic = logic;
        seniorListView.setItems(seniorList);
        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        seniorList.addListener((ListChangeListener<Person>) change -> seniorListView.refresh());
        seniorListView.setCellFactory(listView -> new SeniorListViewCell(logic));
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Senior} using a {@code SeniorCard}.
     */
    private static class SeniorListViewCell extends ListCell<Person> {
        private final Logic logic;
        SeniorListViewCell(Logic logic) {
            this.logic = logic;
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1, logic).getRoot());
            }
        }
    }

}
