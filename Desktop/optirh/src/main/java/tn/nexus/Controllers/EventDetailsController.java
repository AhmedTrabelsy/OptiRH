package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.nexus.Entities.Evenement;
import tn.nexus.Entities.Reservation_evenement;

import tn.nexus.Services.Reservation_evenementServices;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

public class EventDetailsController {

    @FXML
    private Label DateDebutData;

    @FXML
    private Label DateFinData;

    @FXML
    private Label DescriptionData;

    @FXML
    private Label HeureData;

    @FXML
    private Label LieuxData;

    @FXML
    private Label PrixData;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView eventImage;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label titleLabel;


    private Evenement currentEvent;
    private Reservation_evenementServices reservationService = new Reservation_evenementServices();

    public void setEventData(Evenement event) {
        this.currentEvent = event;
        titleLabel.setText(event.getTitre());
        DateDebutData.setText(event.getDateDebut().toString());
        DateFinData.setText(event.getDateFin().toString());
        DescriptionData.setText(event.getDescription());
        PrixData.setText(String.valueOf(event.getPrix()));
        HeureData.setText(String.valueOf(event.getHeure()));
        LieuxData.setText(String.valueOf(event.getLieu()));

        File file = new File(event.getImage());
        if (file.exists()) {
            eventImage.setImage(new Image(file.toURI().toString()));
        }
    }


    @FXML
    void handleReservation(ActionEvent event) {

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            // Afficher une alerte si des champs sont vides
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de réserver !");
            alert.showAndWait();
            return;
        }

        // Création de l'objet Reservation_evenement
        Reservation_evenement reservation = new Reservation_evenement();
        reservation.setIdUser(1); // ID utilisateur fixe
        reservation.setIdEvenement(currentEvent.getIdEvenement()); // ID de l'événement
        reservation.setFirstName(firstName);
        reservation.setLastName(lastName);
        reservation.setEmail(email);
        reservation.setTelephone(phone);
        reservation.setDateReservation(LocalDate.now()); // Date actuelle

        // Insertion dans la base de données
        Reservation_evenementServices reservationService = new Reservation_evenementServices();
        try {
            int result = reservationService.insert(reservation);
            if (result > 0) {
                // Afficher une confirmation
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Réservation confirmée");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Votre réservation a été enregistrée avec succès !");
                successAlert.showAndWait();
            } else {
                // Afficher une erreur en cas d'échec
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur est survenue lors de la réservation. Veuillez réessayer.");
                errorAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur SQL");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur s'est produite lors de l'enregistrement dans la base de données.");
            errorAlert.showAndWait();
        }
    }
}