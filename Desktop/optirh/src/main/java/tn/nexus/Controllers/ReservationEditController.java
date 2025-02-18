package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Services.Reservation_evenementServices;

import java.sql.SQLException;

public class ReservationEditController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;

    private Reservation_evenement reservation;
    private Reservation_evenementServices reservationService = new Reservation_evenementServices();


    // Méthode pour initialiser les données de la réservation
    public void initData(Reservation_evenement reservation) {
        this.reservation = reservation;
        // Utiliser les bons getters et setters de l'entité
        nomField.setText(reservation.getLastName());
        prenomField.setText(reservation.getFirstName());
        telephoneField.setText(reservation.getTelephone());
        emailField.setText(reservation.getEmail());
    }

    // Méthode pour sauvegarder les modifications
    @FXML
    private void handleSaveAction() {
        // Mettre à jour la réservation avec les valeurs des champs
        reservation.setLastName(nomField.getText());
        reservation.setFirstName(prenomField.getText());
        reservation.setTelephone(telephoneField.getText());
        reservation.setEmail(emailField.getText());

        // Appeler la méthode de mise à jour dans la base de données
        try {
            reservationService.update(reservation);
            System.out.println("Réservation mise à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour !");
        }
    }
}
