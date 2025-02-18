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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        try {
            // Récupérer l'ID de l'événement associé à la réservation
            int evenementId = reservation.getIdEvenement();

            // Récupérer l'événement associé à cet ID
            Evenement evenement = evenementService.getEvenementById(evenementId);

            if (evenement != null) {
                // Récupérer la date de début de l'événement
                LocalDate dateDebut = evenement.getDateDebut(); // Si c'est un LocalDate, vous pouvez utiliser atStartOfDay()

                // Convertir LocalDate en LocalDateTime (en ajoutant minuit par défaut)
                LocalDateTime dateDebutDateTime = dateDebut.atStartOfDay();

                // Obtenir la date actuelle
                LocalDateTime now = LocalDateTime.now();

                // Vérifier la différence entre la date actuelle et la date de début
                long hoursBetween = Duration.between(now, dateDebutDateTime).toHours();

                // Si la différence est inférieure à 24 heures, empêcher la suppression
                if (hoursBetween < 24) {
                    // Afficher un message d'alerte
                    showAlert("Erreur", "Impossible de supprimer la réservation car elle commence dans moins de 24 heures.");
                    return;
                }

                // Sinon, procéder à la suppression
                reservationService.delete(reservation);
                System.out.println("Réservation supprimée avec succès !");
                refreshReservationList();
            } else {
                // Si l'événement est introuvable
                showAlert("Erreur", "Événement non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la suppression !");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);  // Type d'alerte par défaut : erreur
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshReservationList() {
        try {
            // Récupérer à nouveau la liste des réservations
            List<Reservation_evenement> reservationList = reservationService.getReservatiobByuserID();

            // Mettre à jour la TableView avec les nouvelles données
            reservationTable.setItems(FXCollections.observableArrayList(reservationList));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer la liste des réservations après suppression.");
        }
    }

    @FXML
    private void handleEditAction(Reservation_evenement reservation) {
        try {
            // Récupérer la liste des réservations pour un utilisateur donné
            List<Reservation_evenement> reservationList = reservationService.getReservatiobByuserID2();

            // Vérifier si la liste contient des éléments
            if (!reservationList.isEmpty()) {
                // Récupérer le premier élément de la liste (ou utiliser un autre critère pour en choisir un spécifique)
                Reservation_evenement reservationDetails = reservationList.get(0);

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
                controller.initData(reservationDetails); // Passer les informations récupérées

                // Afficher la fenêtre
                dialogStage.showAndWait();
            } else {
                // Gérer le cas où aucune réservation n'est trouvée
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune réservation");
                alert.setHeaderText("Aucune réservation trouvée pour l'utilisateur.");
                alert.showAndWait();
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir la fenêtre de modification");
            alert.showAndWait();
        }
    }

}
