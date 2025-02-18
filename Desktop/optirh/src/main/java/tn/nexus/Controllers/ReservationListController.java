package tn.nexus.Controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.nexus.Entities.Evenement;
import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Services.EvenementServices;
import tn.nexus.Services.Reservation_evenementServices;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationListController {
    @FXML
    private TableColumn<Reservation_evenement, LocalDate> BookingTimeColumn;  // Date de réservation

    @FXML
    private TableColumn<Reservation_evenement, String> TitreColumn;  // Titre de l'événement

    @FXML
    private TableColumn<Reservation_evenement, LocalDate> DateEventColumn;  // Date de l'événement

    @FXML
    private TableView<Reservation_evenement> reservationTable;


    @FXML
    private TableColumn<Reservation_evenement, String> ActionColumn; // Nouvelle colonne pour les actions


    private Reservation_evenementServices reservationService = new Reservation_evenementServices();
    private EvenementServices evenementService = new EvenementServices();

    @FXML
    public void initialize() {
        try {
            List<Reservation_evenement> reservations = reservationService.getReservatiobByuserID();
            ObservableList<Reservation_evenement> observableReservations = FXCollections.observableArrayList(reservations);

            // Associer date de réservation directement
            BookingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));

            // Associer titre et date de début en récupérant l'événement
            TitreColumn.setCellValueFactory(data -> {
                Evenement event = getEvenement(data.getValue().getIdEvenement());
                return new SimpleStringProperty(event != null ? event.getTitre() : "Inconnu");
            });

            DateEventColumn.setCellValueFactory(data -> {
                Evenement event = getEvenement(data.getValue().getIdEvenement());
                return new SimpleObjectProperty<>(event != null ? event.getDateDebut() : null);
            });

            // Création de la colonne des actions
            ActionColumn.setCellFactory(new Callback<TableColumn<Reservation_evenement, String>, TableCell<Reservation_evenement, String>>() {
                @Override
                public TableCell<Reservation_evenement, String> call(TableColumn<Reservation_evenement, String> param) {
                    return new TableCell<Reservation_evenement, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                // Créer les boutons Modifier et Supprimer
                                Button editButton = new Button("Modifier");
                                Button deleteButton = new Button("Supprimer");

                                // Actions sur les boutons
                                editButton.setOnAction(event -> handleEditAction(getTableRow().getItem()));
                                deleteButton.setOnAction(event -> handleDeleteAction(getTableRow().getItem()));

                                // Affichage des boutons dans la cellule
                                setGraphic(new HBox(10, editButton, deleteButton));
                            }
                        }
                    };
                }
            });

            reservationTable.setItems(observableReservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
    }


    private Evenement getEvenement(int idEvenement) {
        try {
            return evenementService.getEvenementById(idEvenement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @FXML
    private void handleDeleteAction(Reservation_evenement reservation) {
        if (reservation != null) {
            // Afficher une fenêtre de confirmation avant de supprimer
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette réservation ?");
            confirmationAlert.setContentText("Cette action est irréversible.");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Appeler la méthode delete du service ReservationEvenementService
                        int rowsAffected = reservationService.delete(reservation);
                        if (rowsAffected > 0) {
                            // Afficher un message de succès
                            showAlert(Alert.AlertType.INFORMATION, "Réservation supprimée", "La réservation a été supprimée avec succès.");
                            // Rafraîchir la liste des réservations
                            refreshReservationList();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Erreur", "La réservation n'a pas pu être supprimée.");
                        }
                    } catch (SQLException e) {
                        // En cas d'erreur, afficher un message d'erreur
                        showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Une erreur est survenue lors de la suppression de la réservation.");
                    }
                }
            });
        } else {
            // Si aucune réservation n'est sélectionnée
            showAlert(Alert.AlertType.WARNING, "Aucune réservation sélectionnée", "Veuillez sélectionner une réservation à supprimer.");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshReservationList() {
        try {
            List<Reservation_evenement> updatedReservations = reservationService.getReservatiobByuserID(); // Recharger les réservations
            ObservableList<Reservation_evenement> observableReservations = FXCollections.observableArrayList(updatedReservations);
            reservationTable.setItems(observableReservations); // Mettre à jour la TableView
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de recharger les réservations.");
        }
    }

    @FXML
    private void handleEditAction(Reservation_evenement reservation) {
        try {
            // Charger la nouvelle fenêtre FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationEditForm.fxml"));
            AnchorPane page = loader.load();

            // Créer la nouvelle scène
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier Réservation");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(reservationTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Passer la réservation à la nouvelle fenêtre
            ReservationEditController controller = loader.getController();
            controller.initData(reservation);

            // Afficher la fenêtre
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir la fenêtre de modification");
            alert.showAndWait();
        }
    }


}
