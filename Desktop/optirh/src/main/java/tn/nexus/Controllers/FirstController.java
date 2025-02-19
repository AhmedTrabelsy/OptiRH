
package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstController {

    @FXML
    private void handleAdminButton(ActionEvent event) {
        try {
            // Charger l'interface de gestion des projets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projet.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et l'afficher
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Projets");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de gestion des projets.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleEmployeeButton(ActionEvent event) {
        try {
            // Charger l'interface de recherche des projets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RechercherProjet.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et l'afficher
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Rechercher un Projet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}