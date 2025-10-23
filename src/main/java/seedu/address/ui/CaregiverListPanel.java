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
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class CaregiverListPanel extends UiPart<Region> {
    private static final String FXML = "CaregiverListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CaregiverListPanel.class);
    private final Logic logic;

    @FXML
    private ListView<Caregiver> caregiverListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public CaregiverListPanel(ObservableList<Caregiver> caregiverList, Logic logic) {
        super(FXML);
        this.logic = logic;
        caregiverListView.setItems(caregiverList);
        // Refresh all rows whenever the list reports a change (e.g., a Senior was edited)
        caregiverList.addListener((ListChangeListener<Person>) change -> caregiverListView.refresh());
        caregiverListView.setCellFactory(listView -> new CaregiverListViewCell(logic));
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
            } else {
                setGraphic(new CaregiverCard(caregiver, getIndex() + 1, logic).getRoot());
            }
        }
    }

}
