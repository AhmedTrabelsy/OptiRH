package tn.nexus.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import tn.nexus.Entities.ReservationTrajet;
import tn.nexus.services.ReservationTrajetService;
import tn.nexus.services.UserService;

import java.sql.SQLException;
import java.util.List;

public class GestionReservationController {

    @FXML private TextField idField;
    @FXML private ComboBox<String> disponibiliteCombo;
    @FXML private TextField userIdField;
    @FXML private Label errorMessage;
    @FXML private TableView<ReservationTrajet> reservationTable;
    @FXML private TableColumn<ReservationTrajet, Integer> idColumn;
    @FXML private TableColumn<ReservationTrajet, String> disponibiliteColumn;
    @FXML private TableColumn<ReservationTrajet, Integer> userIdColumn;@FXML
    private TableColumn<ReservationTrajet, String> userNameColumn;

    private final ReservationTrajetService reservationService = new ReservationTrajetService();
    private final ObservableList<ReservationTrajet> reservationList = FXCollections.observableArrayList();

    private int vehiculeId; // ID du véhicule associé
    private int trajetId; // ID du trajet associé

    // Méthode pour initialiser l'ID du véhicule et du trajet
    public void setVehiculeAndTrajetId(int vehiculeId, int trajetId) {
        this.vehiculeId = vehiculeId;
        this.trajetId = trajetId;
        loadReservations(); // Charger les réservations associées à ce véhicule et trajet
    }

    @FXML
    public void initialize() {
        // Lier les colonnes de la TableView aux propriétés de l'entité ReservationTrajet
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        disponibiliteColumn.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        // Ajouter un écouteur de sélection à la TableView
        reservationTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        fillFieldsWithSelectedReservation(newValue);
                    }
                }
        );

        // Initialiser la ComboBox de disponibilité
        disponibiliteCombo.getItems().addAll("Disponible", "Indisponible");
    }

    // Remplir les champs de saisie avec les valeurs de la réservation sélectionnée
    private void fillFieldsWithSelectedReservation(ReservationTrajet reservation) {
        idField.setText(String.valueOf(reservation.getId()));
        disponibiliteCombo.setValue(reservation.getDisponibilite());
        userIdField.setText(String.valueOf(reservation.getUserId()));
    }

    // Charger la liste des réservations associées au véhicule et au trajet
    private void loadReservations() {
        try {
            reservationList.clear();

            List<ReservationTrajet> reservations = reservationService.getReservationsByVehiculeAndTrajet(vehiculeId, trajetId);

            // Récupérer et ajouter le nom de l'utilisateur pour chaque réservation
            for (ReservationTrajet reservation : reservations) {
                UserService UserService = new UserService();
                String userName = UserService.getUserNameById(reservation.getUserId());
                reservation.setUserName(userName);
            }

            reservationList.addAll(reservations);
            reservationTable.setItems(reservationList);

        } catch (SQLException e) {
            showError("Erreur lors du chargement des réservations : " + e.getMessage());
        }
    }



    // Gérer l'ajout d'une réservation
    @FXML
    public void handleAjouterReservation() {
        String disponibilite = disponibiliteCombo.getValue();
        String userIdText = userIdField.getText();

        // Valider les champs
        if (disponibilite == null || userIdText.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);
            ReservationTrajet reservation = new ReservationTrajet(0, disponibilite,userId ,trajetId, vehiculeId);

            int result = reservationService.insert(reservation);
            if (result > 0) {
                showSuccess("Réservation ajoutée avec succès !");
                loadReservations();
                clearFields();
            } else {
                showError("Erreur lors de l'ajout de la réservation.");
            }
        } catch (NumberFormatException e) {
            showError("L'ID de l'utilisateur doit être un nombre valide !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la modification d'une réservation
    @FXML
    public void handleModifierReservation() {
        String idText = idField.getText();
        String disponibilite = disponibiliteCombo.getValue();
        String userIdText = userIdField.getText();

        // Valider les champs
        if (idText.isEmpty() || disponibilite == null || userIdText.isEmpty()) {
            showError("Tous les champs doivent être remplis !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int userId = Integer.parseInt(userIdText);
            ReservationTrajet reservation = new ReservationTrajet(id, disponibilite, vehiculeId, trajetId, userId);

            int result = reservationService.update(reservation);
            if (result > 0) {
                showSuccess("Réservation modifiée avec succès !");
                loadReservations();
                clearFields();
            } else {
                showError("Erreur lors de la modification de la réservation.");
            }
        } catch (NumberFormatException e) {
            showError("L'ID de l'utilisateur doit être un nombre valide !");
        } catch (SQLException e) {
            showError("Erreur de base de données : " + e.getMessage());
        }
    }

    // Gérer la suppression d'une réservation
    @FXML
    public void handleSupprimerReservation() {
        String idText = idField.getText();

        if (idText.isEmpty()) {
            showError("Veuillez sélectionner une réservation à supprimer !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            ReservationTrajet reservation = new ReservationTrajet(id, "", 0, 0, 0);

            int result = reservationService.delete(reservation);
            if (result > 0) {
                showSuccess("Réservation supprimée avec succès !");
                loadReservations();
                clearFields();
            } else {
                showError("Erreur lors de la suppression de la réservation.");
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
        userIdField.clear();
    }
}
