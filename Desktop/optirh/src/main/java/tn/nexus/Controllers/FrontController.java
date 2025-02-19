package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.nexus.Entities.Projet ;
import tn.nexus.Service.ProjetService;

import java.sql.SQLException;
import java.util.List;

public class FrontController {




        @FXML
        private TextField utilisateurIdField;

        @FXML
        private ListView<String> projetsListView;

        private ProjetService projetService = new ProjetService();

        @FXML
        private void handleRechercherProjets() {
            try {
                // Récupérer l'ID utilisateur saisi
                int utilisateurId = Integer.parseInt(utilisateurIdField.getText());

                // Récupérer les projets associés à l'utilisateur
                List<Projet> projets = projetService.getProjetsByUtilisateurId(utilisateurId);

                // Afficher les projets dans la ListView
                projetsListView.getItems().clear();
                for (Projet projet : projets) {
                    projetsListView.getItems().add(
                            "ID: " + projet.getId() + " - Type: " + projet.getType() + " - Description: " + projet.getDescription() + " - Statut: " + projet.getStatus()
                    );
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID utilisateur doit être un nombre.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la récupération des projets.");
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

