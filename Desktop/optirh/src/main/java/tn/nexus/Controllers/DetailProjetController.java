package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.nexus.Entities.*;
import tn.nexus.Service.*;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DetailProjetController {
    @FXML
    private Button ModifierProjetButton;
    @FXML
    private Button supprimerProjetButton;
    @FXML
    private Button changerStatusProjetButton;

    @FXML
    private Button ModifierMissionButton;
    @FXML
    private Button ajouterMissionButton;
    @FXML
    private Button supprimerMissionButton;
    @FXML
    private Button changerStatusMissionButton;


    @FXML
    private Label typeProjetLabel;
    @FXML
    private Label descriptionProjetLabel;
    @FXML
    private Label statusProjetLabel;
    @FXML
    private Label utilisateurIdLabel;
    @FXML
    private ListView<String> missionsListView;

    private Projet projet;
    private ProjetService projetService = new ProjetService();
    private MissionService missionService = new MissionService();

    public void setProjet(Projet projet) {
        this.projet = projet;
        afficherDetailsProjet();
        afficherMissions();
    }

    private void afficherDetailsProjet() {
        if (projet != null) {
            typeProjetLabel.setText("Type de projet: " + projet.getType());
            descriptionProjetLabel.setText("Description: " + projet.getDescription());
            statusProjetLabel.setText("Statut: " + projet.getStatus());
            utilisateurIdLabel.setText("ID Utilisateur: " + projet.getUtilisateurId());
        }
    }

    private void afficherMissions() {
        if (projet != null) {
            try {
                List<Mission> missions = missionService.getByProjetId(projet.getId());
                missionsListView.getItems().clear(); // Clear previous items
                for (Mission mission : missions) {
                    missionsListView.getItems().add(
                            mission.getId() + " - " + mission.getTitre() + " - " + mission.getDescription() + " (" + mission.getStatut() + ")"
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleModifierProjet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProjet.fxml"));
            Parent root = loader.load();

            ModifierProjetController controller = loader.getController();
            controller.setProjet(projet); // projet est l'objet Projet actuel


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le Projet");
            stage.showAndWait();

            afficherDetailsProjet();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de modification du projet.");
        }
    }
    @FXML
    private void handleSupprimerProjet(ActionEvent event) {
        try {
            projetService.delete(projet);

            Stage stage = (Stage) supprimerProjetButton.getScene().getWindow();
            stage.close();

            showAlert("Succès", "Projet supprimé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la suppression du projet.");
        }
    }
    @FXML
    private void handleChangerStatusProjet(ActionEvent event) {
        try {
            String nouveauStatut = projet.getStatus().equals("en cours") ? "terminée" : "en cours";
            projet.setStatus(nouveauStatut);

            projetService.update(projet);

            statusProjetLabel.setText("Statut: " + nouveauStatut);

            showAlert("Succès", "Statut du projet mis à jour avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du statut du projet.");
        }
    }
    @FXML
    private void handleModifierMission(ActionEvent event) {
        try {
            // Récupérer la mission sélectionnée dans la ListView
            String selectedMission = missionsListView.getSelectionModel().getSelectedItem();
            if (selectedMission != null) {
                // Extraire l'ID de la mission sélectionnée
                int missionId = extractMissionIdFromListView(selectedMission);
                System.out.println("Mission sélectionnée ID: " + missionId);
                Mission mission = missionService.getById(missionId);
                if (mission != null) {
                    // Charger la fenêtre de modification de la mission
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierMission.fxml"));
                    Parent root = loader.load();

                    // Passer la mission au contrôleur de la fenêtre de modification
                    ModifierMissionController controller = loader.getController();
                    controller.setMission(mission);

                    // Afficher la fenêtre
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier la Mission");
                    stage.showAndWait();

                    // Rafraîchir la liste des missions après modification
                    afficherMissions();
                } else {
                    showAlert("Erreur", "Mission non trouvée.");
                }
            } else {
                showAlert("Erreur", "Aucune mission sélectionnée.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de modification.");
        }
    }

    @FXML
    private void handleAjouterMission(ActionEvent event) {
        try {
            // Charger l'interface d'ajout de mission
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMission.fxml"));
            Parent root = loader.load();

            // Passer l'ID du projet au contrôleur de l'interface d'ajout de mission
            AjouterMissionController controller = loader.getController();
            controller.setProjetId(projet.getId()); // projet est l'objet Projet actuel

            // Afficher la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Mission");
            stage.showAndWait();

            // Rafraîchir la liste des missions après ajout
            afficherMissions();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre d'ajout de mission.");
        }
    }

    @FXML
    private void handleSupprimerMission(ActionEvent event) {
        try {
            String selectedMission = missionsListView.getSelectionModel().getSelectedItem();
            if (selectedMission != null) {
                Mission mission = missionService.getById(projet.getId()); // Vous devez implémenter cette méthode dans MissionService
                missionService.delete(mission);

                afficherMissions();

                showAlert("Succès", "Mission supprimée avec succès.");
            } else {
                showAlert("Erreur", "Aucune mission sélectionnée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la suppression de la mission.");
        }
    }
    // Méthode appelée pour changer le statut d'une mission
    @FXML
    private void handleChangerStatusMission(ActionEvent event) {
        try {
            String selectedMission = missionsListView.getSelectionModel().getSelectedItem();
            if (selectedMission != null) {
                int missionId = extractMissionIdFromListView(selectedMission);

                Mission mission = missionService.getById(missionId);
                if (mission != null) {
                    String nouveauStatut = mission.getStatut().equals("en cours") ? "terminé" : "en cours";
                    mission.setStatut(nouveauStatut);

                    missionService.update(mission);

                    afficherMissions();

                    showAlert("Succès", "Statut de la mission mis à jour avec succès.");
                } else {
                    showAlert("Erreur", "Mission non trouvée.");
                }
            } else {
                showAlert("Erreur", "Aucune mission sélectionnée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du statut de la mission.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private int extractMissionIdFromListView(String selectedMission) {
        String[] parts = selectedMission.split(" - ");
        return Integer.parseInt(parts[0]); // L'ID est le premier élément
    }
}
