package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;

import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReclamationController {

    @FXML
    private TableView<Reclamation> reclamationsTable;

    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> statusColumn;

    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;

    @FXML
    private TableColumn<Reclamation, Integer> utilisateurIdColumn;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    // private TextField utilisateurIdField;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        utilisateurIdColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateurId"));

        List<Reclamation> reclamationList = reclamationService.showAll();
        ObservableList<Reclamation> observableReclamationList = FXCollections.observableArrayList(reclamationList);
        reclamationsTable.setItems(observableReclamationList);

        statusField.getItems().addAll("En attente", "En cours", "Résolue");

        // Sélectionner une réclamation et remplir les champs
        reclamationsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                descriptionField.setText(newValue.getDescription());
                statusField.setValue(newValue.getStatus());
                dateField.setValue(convertToLocalDate(newValue.getDate()));
                //utilisateurIdField.setText(String.valueOf(newValue.getUtilisateurId()));
            }
        });
    }

    @FXML
    void ajouterReclamation(ActionEvent event) throws SQLException {
        if (descriptionField.getText().isEmpty() || statusField.getValue() == null || dateField.getValue() == null ) {
            showAlert(Alert.AlertType.WARNING, "Tous les champs doivent être remplis !");
            return;
        }

        // int utilisateurId;
        /*try {
            utilisateurId = Integer.parseInt(utilisateurIdField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "L'ID utilisateur doit être un nombre valide !");
            return;
        }*/

        Reclamation reclamation = new Reclamation(
                descriptionField.getText(),
                statusField.getValue(),
                Date.valueOf(dateField.getValue()),
                1 // Fixe l'utilisateur ID à 1
        );


        reclamationService.insert(reclamation);
        reclamationsTable.getItems().add(reclamation);
        clearFields();
    }

    @FXML
    void modifierReclamation(ActionEvent event) throws SQLException {
        Reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            selectedReclamation.setDescription(descriptionField.getText());
            selectedReclamation.setStatus(statusField.getValue());
            selectedReclamation.setDate(Date.valueOf(dateField.getValue())); // Conversion LocalDate -> Date SQL
            //selectedReclamation.setUtilisateurId(Integer.parseInt(utilisateurIdField.getText()));

            reclamationService.update(selectedReclamation);
            reclamationsTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune réclamation sélectionnée !");
        }
    }

    @FXML
    void supprimerReclamation(ActionEvent event) throws SQLException {
        Reclamation selectedReclamation = reclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            int rowsAffected = reclamationService.delete(selectedReclamation);
            reclamationsTable.getItems().remove(selectedReclamation);
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Réclamation supprimée avec succès !");
                refreshTable();  // Recharger la TableView
            } else {
                showAlert(Alert.AlertType.WARNING, "La suppression a échoué !");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune réclamation sélectionnée !");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void clearFields() {
        descriptionField.clear();
        statusField.setValue(null);
        dateField.setValue(null);
        //utilisateurIdField.clear();
    }

    // Convertit une Date SQL en LocalDate
    private LocalDate convertToLocalDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private void refreshTable() throws SQLException {
        List<Reclamation> reclamationList = reclamationService.showAll();
        ObservableList<Reclamation> observableReclamationList = FXCollections.observableArrayList(reclamationList);
        reclamationsTable.setItems(observableReclamationList);
    }

}