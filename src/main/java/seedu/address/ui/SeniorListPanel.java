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
import seedu.address.model.person.Senior;

/**
 * Panel containing the list of persons.
 */
public class SeniorListPanel extends UiPart<Region> {
    private static final String FXML = "SeniorListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(SeniorListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Senior> seniorListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public SeniorListPanel(ObservableList<Senior> seniorList, Logic logic) {
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
            } else {
                setGraphic(new SeniorCard(senior, getIndex() + 1, logic).getRoot());
            }
        }
    }

}
