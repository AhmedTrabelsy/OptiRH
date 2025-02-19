package tn.nexus.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.nexus.Entities.Trajet;
import tn.nexus.services.TrajetService;

import java.io.IOException;
import java.sql.SQLException;

public class GestionTrajetController {


    @FXML private TextField idField;
    @FXML private ComboBox<String> disponibiliteCombo;
    @FXML private TextField typeField;
    @FXML private TextField stationField;
    @FXML private Label errorMessage;
    @FXML private TableView<Trajet> trajetTable;
    @FXML private TableColumn<Trajet, Integer> idColumn;
    @FXML private TableColumn<Trajet, String> disponibiliteColumn;
    @FXML private TableColumn<Trajet, String> typeColumn;
    @FXML private TableColumn<Trajet, String> stationColumn;

    private final TrajetService trajetService = new TrajetService();
    private final ObservableList<Trajet> trajetList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Lier les colonnes de la TableView aux propriétés de l'entité Trajet
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        disponibiliteColumn.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        stationColumn.setCellValueFactory(new PropertyValueFactory<>("station"));

        // Charger la liste des trajets dans le tableau
        loadTrajets();

        // Ajouter un écouteur de sélection à la TableView
        trajetTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Remplir les champs de saisie avec les valeurs de la ligne sélectionnée
                        fillFieldsWithSelectedTrajet(newValue);
                    }
                }
        );
    }

    // Remplir les champs de saisie avec les valeurs du trajet sélectionné
    private void fillFieldsWithSelectedTrajet(Trajet trajet) {
        idField.setText(String.valueOf(trajet.getId()));
        disponibiliteCombo.setValue(trajet.getDisponibilite());
        typeField.setText(trajet.getType());
        stationField.setText(trajet.getStation());
    }

    // Charger la liste des trajets depuis la base de données
    private void loadTrajets() {
        try {
            trajetList.clear();
            trajetList.addAll(trajetService.showAll());
            trajetTable.setItems(trajetList);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des trajets : " + e.getMessage());
        }
    }

    // Gérer l'ajout d'un trajet
    @FXML
    public void handleAjouterTrajet() {
        String disponibilite = disponibiliteCombo.getValue();
        String type = typeField.getText();
        String station = stationField.getText();

        // Valider les champs
        if (disponibilite == null || type.isEmpty() || station.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        // Créer un nouveau trajet
        Trajet trajet = new Trajet(0, disponibilite, type, station);

        try {
            int result = trajetService.insert(trajet);
            if (result > 0) {
                showSuccess("Trajet ajouté avec succès !");
                loadTrajets(); // Recharger la liste des trajets
                clearFields();
            } else {
                showError("Erreur lors de l'ajout du trajet.");
            }
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la modification d'un trajet
    @FXML
    public void handleModifierTrajet() {
        String idText = idField.getText();
        String disponibilite = disponibiliteCombo.getValue();
        String type = typeField.getText();
        String station = stationField.getText();

        // Valider les champs
        if (idText.isEmpty() || disponibilite == null || type.isEmpty() || station.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Trajet trajet = new Trajet(id, disponibilite, type, station);

            int result = trajetService.update(trajet);
            if (result > 0) {
                showSuccess("Trajet modifié avec succès !");
                loadTrajets(); // Recharger la liste des trajets
                clearFields();
            } else {
                showError("Erreur lors de la modification du trajet.");
            }
        } catch (NumberFormatException e) {
            showError("L'ID doit être un nombre valide !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la suppression d'un trajet
    @FXML
    public void handleSupprimerTrajet() {
        String idText = idField.getText();

        if (idText.isEmpty()) {
            showError("Veuillez sélectionner un trajet à supprimer !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Trajet trajet = new Trajet(id, "", "", "");

            int result = trajetService.delete(trajet);
            if (result > 0) {
                showSuccess("Trajet supprimé avec succès !");
                loadTrajets(); // Recharger la liste des trajets
                clearFields();
            } else {
                showError("Erreur lors de la suppression du trajet.");
            }
        } catch (NumberFormatException e) {
            showError("L'ID doit être un nombre valide !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer l'annulation (vider les champs)
    @FXML
    public void handleAnnuler() {
        clearFields();
    }

    // Afficher un message d'erreur
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setTextFill(Color.RED);
        errorMessage.setVisible(true);
    }

    // Afficher un message de succès
    private void showSuccess(String message) {
        errorMessage.setText(message);
        errorMessage.setTextFill(Color.GREEN);
        errorMessage.setVisible(true);
    }

    // Vider les champs de saisie
    private void clearFields() {
        idField.clear();
        disponibiliteCombo.getSelectionModel().clearSelection();
        typeField.clear();
        stationField.clear();
    }

    @FXML
    public void handleOpenGestionVehicule() {
        Trajet selectedTrajet = trajetTable.getSelectionModel().getSelectedItem();
        if (selectedTrajet != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionVehicule.fxml"));
                Parent root = loader.load();

                // Passer l'ID du trajet sélectionné au contrôleur de gestion des véhicules
                GestionVehiculeController controller = loader.getController();
                controller.setTrajetId(selectedTrajet.getId());


                // Afficher l'interface de gestion des véhicules
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Gestion des Véhicules pour le Trajet #" + selectedTrajet.getId());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showError("Veuillez sélectionner un trajet !");
        }
    }

}