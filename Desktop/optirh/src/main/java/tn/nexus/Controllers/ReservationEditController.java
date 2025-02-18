package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Services.Reservation_evenementServices;

import java.sql.SQLException;

public class ReservationEditController {

    @FXML
    private Button ClearButton;

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
        // Récupérer les valeurs des champs
        String lastName = nomField.getText();
        String firstName = prenomField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();

        // Vérifier si les champs ne sont pas vides
        if (lastName.isEmpty() || firstName.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            // Afficher une alerte si l'un des champs est vide
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérifier si le numéro de téléphone est valide (doit commencer par + et avoir entre 11 et 14 chiffres)
        if (!telephone.matches("^\\+\\d{11,14}$")) {
            showAlert("Erreur", "Le numéro de téléphone doit commencer par '+' et contenir entre 11 et 14 chiffres.");
            return;
        }

        // Vérifier si l'email est valide
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            showAlert("Erreur", "L'email est invalide.");
            return;
        }

        // Si tout est valide, mettre à jour la réservation avec les valeurs des champs
        reservation.setLastName(lastName);
        reservation.setFirstName(firstName);
        reservation.setTelephone(telephone);
        reservation.setEmail(email);

        // Appeler la méthode de mise à jour dans la base de données
        try {
            reservationService.update(reservation);
            System.out.println("Réservation mise à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour !");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void handleclear(ActionEvent actionEvent) {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        emailField.clear();

    }
}
