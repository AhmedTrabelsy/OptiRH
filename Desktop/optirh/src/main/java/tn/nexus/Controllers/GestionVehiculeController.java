package tn.nexus.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.nexus.Entities.Vehicule;
import tn.nexus.services.VehiculeService;

import java.io.IOException;
import java.sql.SQLException;

public class GestionVehiculeController {

    @FXML private TextField idField;
    @FXML private ComboBox<String> disponibiliteCombo;
    @FXML private TextField typeField;
    @FXML private TextField nbrPlaceField;
    @FXML private Label errorMessage;
    @FXML private TableView<Vehicule> vehiculeTable;
    @FXML private TableColumn<Vehicule, Integer> idColumn;
    @FXML private TableColumn<Vehicule, String> disponibiliteColumn;
    @FXML private TableColumn<Vehicule, String> typeColumn;
    @FXML private TableColumn<Vehicule, Integer> nbrPlaceColumn;

    private final VehiculeService vehiculeService = new VehiculeService();
    private final ObservableList<Vehicule> vehiculeList = FXCollections.observableArrayList();

    private int trajetId; // ID du trajet associé

    // Méthode pour initialiser l'ID du trajet
    public void setTrajetId(int trajetId) {
        this.trajetId = trajetId;
        System.out.println("Trajet ID reçu dans GestionVehiculeController: " + trajetId);
        loadVehicules(); // Charger les véhicules associés à ce trajet
    }

    @FXML
    public void initialize() {
        // Lier les colonnes de la TableView aux propriétés de l'entité Vehicule
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        disponibiliteColumn.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nbrPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("nbrplace"));

        // Ajouter un écouteur de sélection à la TableView
        vehiculeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        fillFieldsWithSelectedVehicule(newValue);
                    }
                }
        );
    }

    // Remplir les champs de saisie avec les valeurs du véhicule sélectionné
    private void fillFieldsWithSelectedVehicule(Vehicule vehicule) {
        idField.setText(String.valueOf(vehicule.getId()));
        disponibiliteCombo.setValue(vehicule.getDisponibilite());
        typeField.setText(vehicule.getType());
        nbrPlaceField.setText(String.valueOf(vehicule.getNbrplace()));
    }

    // Charger la liste des véhicules associés au trajet
    private void loadVehicules() {
        try {
            vehiculeList.clear();
            vehiculeList.addAll(vehiculeService.getVehiculesByTrajetId(trajetId));
            vehiculeTable.setItems(vehiculeList);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des véhicules : " + e.getMessage());
        }
    }

    // Gérer l'ajout d'un véhicule
    @FXML
    public void handleAjouterVehicule() {
        String disponibilite = disponibiliteCombo.getValue();
        String type = typeField.getText();
        String nbrPlaceText = nbrPlaceField.getText();

        // Valider les champs
        if (disponibilite == null || type.isEmpty() || nbrPlaceText.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        try {
            int nbrPlace = Integer.parseInt(nbrPlaceText);
            Vehicule vehicule = new Vehicule(0, disponibilite, type, nbrPlace, trajetId);

            int result = vehiculeService.insert(vehicule);
            if (result > 0) {
                showSuccess("Véhicule ajouté avec succès !");
                loadVehicules(); // Recharger la liste des véhicules
                clearFields();
            } else {
                showError("Erreur lors de l'ajout du véhicule.");
            }
        } catch (NumberFormatException e) {
            showError("Le nombre de places doit être un nombre valide !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la modification d'un véhicule
    @FXML
    public void handleModifierVehicule() {
        String idText = idField.getText();
        String disponibilite = disponibiliteCombo.getValue();
        String type = typeField.getText();
        String nbrPlaceText = nbrPlaceField.getText();

        // Valider les champs
        if (idText.isEmpty() || disponibilite == null || type.isEmpty() || nbrPlaceText.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int nbrPlace = Integer.parseInt(nbrPlaceText);
            Vehicule vehicule = new Vehicule(id, disponibilite, type, nbrPlace, trajetId);

            int result = vehiculeService.update(vehicule);
            if (result > 0) {
                showSuccess("Véhicule modifié avec succès !");
                loadVehicules(); // Recharger la liste des véhicules
                clearFields();
            } else {
                showError("Erreur lors de la modification du véhicule.");
            }
        } catch (NumberFormatException e) {
            showError("L'ID et le nombre de places doivent être des nombres valides !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la suppression d'un véhicule
    @FXML
    public void handleSupprimerVehicule() {
        String idText = idField.getText();

        if (idText.isEmpty()) {
            showError("Veuillez sélectionner un véhicule à supprimer !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Vehicule vehicule = new Vehicule(id, "", "", 0, trajetId);

            int result = vehiculeService.delete(vehicule);
            if (result > 0) {
                showSuccess("Véhicule supprimé avec succès !");
                loadVehicules(); // Recharger la liste des véhicules
                clearFields();
            } else {
                showError("Erreur lors de la suppression du véhicule.");
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
        nbrPlaceField.clear();
    }

    @FXML
    public void handleOpenGestionReservation() {
        Vehicule selectedVehicule = vehiculeTable.getSelectionModel().getSelectedItem();
        if (selectedVehicule != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReservation.fxml"));
                Parent root = loader.load();

                // Passer l'ID du véhicule et du trajet au contrôleur de gestion des réservations
                GestionReservationController controller = loader.getController();
                controller.setVehiculeAndTrajetId(selectedVehicule.getId(), trajetId);

                // Afficher l'interface de gestion des réservations
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Gestion des Réservations pour le Véhicule #" + selectedVehicule.getId());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showError("Veuillez sélectionner un véhicule !");
        }
    }

}