package tn.nexus.Controllers.reclamation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.nexus.Entities.reclamation.Reclamation;
import tn.nexus.Services.reclamation.ReclamationService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class ReclamationfrontController {
    @FXML
    private TableView<Reclamation> reclamationsTable;
    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> statusColumn;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> statusField;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterStatusField;

    private final ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Reclamation> observableReclamationList;

    public void initialize() throws SQLException {
        if (reclamationsTable == null || descriptionColumn == null || statusColumn == null || dateColumn == null) {
            System.err.println("FXML components are not properly injected.");
            return;
        }

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        observableReclamationList = FXCollections.observableArrayList(reclamationService.showAll());

        statusField.setItems(FXCollections.observableArrayList("En attente", "En cours", "Résolue"));
        filterStatusField.setItems(FXCollections.observableArrayList("Tous", "En attente", "En cours", "Résolue"));
        filterStatusField.setValue("Tous");

        FilteredList<Reclamation> filteredData = new FilteredList<>(observableReclamationList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters(filteredData));
        filterStatusField.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters(filteredData));

        SortedList<Reclamation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reclamationsTable.comparatorProperty());
        reclamationsTable.setItems(sortedData);
    }

    private void applyFilters(FilteredList<Reclamation> filteredData) {
        String searchKeyword = searchField.getText().toLowerCase();
        String selectedStatus = filterStatusField.getValue();

        filteredData.setPredicate(reclamation -> {
            if (reclamation == null) return false;
            boolean statusMatches = Objects.equals(selectedStatus, "Tous") || Objects.equals(reclamation.getStatus(), selectedStatus);
            boolean searchMatches = searchKeyword.isEmpty() || reclamation.getDescription().toLowerCase().contains(searchKeyword);
            return statusMatches && searchMatches;
        });
    }

    @FXML
    public void ajouterReclamation() throws SQLException {
        if (descriptionField.getText().isEmpty() || statusField.getValue() == null || dateField.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !");
            return;
        }

        if (dateField.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une date valide !");
            return;
        }

        Reclamation reclamation = new Reclamation(
                descriptionField.getText(),
                statusField.getValue(),
                java.sql.Date.valueOf(dateField.getValue()),
                1
        );
        reclamationService.insert(reclamation);
        observableReclamationList.add(reclamation);
        clearFields();
    }

    @FXML
    public void modifierReclamation() throws SQLException {
        Reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune réclamation sélectionnée !");
            return;
        }

        if (descriptionField.getText().isEmpty() || statusField.getValue() == null || dateField.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !");
            return;
        }

        selectedReclamation.setDescription(descriptionField.getText());
        selectedReclamation.setStatus(statusField.getValue());
        selectedReclamation.setDate(java.sql.Date.valueOf(dateField.getValue()));
        reclamationService.update(selectedReclamation);
        reclamationsTable.refresh();
        clearFields();
    }

    @FXML
    public void supprimerReclamation() throws SQLException {
        Reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune réclamation sélectionnée !");
            return;
        }

        if (reclamationService.delete(selectedReclamation) > 0) {
            observableReclamationList.remove(selectedReclamation);
            showAlert(Alert.AlertType.INFORMATION, "Réclamation supprimée !");
        } else {
            showAlert(Alert.AlertType.WARNING, "Échec de la suppression !");
        }
    }

    @FXML
    public void clearFields() {
        descriptionField.clear();
        dateField.setValue(null);
        statusField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
