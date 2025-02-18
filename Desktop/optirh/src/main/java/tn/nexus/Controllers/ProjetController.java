package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.nexus.Entities.Mission;
import tn.nexus.Entities.Projet;
import tn.nexus.Service.MissionService;
import tn.nexus.Service.ProjetService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjetController {
    @FXML
    private TextField typeProjetField;
    @FXML
    private TextField descriptionProjetField;
    @FXML
    private ComboBox<String> statusProjetComboBox;
    @FXML
    private TextField utilisateurIdField;
    @FXML
    private TextField titreMissionField;
    @FXML
    private TextField descriptionMissionField;
    @FXML
    private ComboBox<String> statutMissionComboBox;
    @FXML
    private CheckBox isPresentCheckBox;
    @FXML
    private ListView<String> missionsListView;

    private List<Mission> missions = new ArrayList<>();
    private ProjetService projetService = new ProjetService();
    private MissionService missionService = new MissionService();

    @FXML
    public void initialize() {
        statusProjetComboBox.getItems().addAll("en cours", "terminée");
        statutMissionComboBox.getItems().addAll("en cours", "terminée");
    }

    @FXML
    private void ajouterMission() {
        String titreMission = titreMissionField.getText();
        String descriptionMission = descriptionMissionField.getText();
        String statutMission = statutMissionComboBox.getValue();
        boolean isPresent = isPresentCheckBox.isSelected();

        if (titreMission.isEmpty() || descriptionMission.isEmpty() || statutMission == null) {
            showAlert("Erreur de saisie", "Tous les champs de la mission sont requis.");
            return;
        }

        Mission mission = new Mission();
        mission.setTitre(titreMission);
        mission.setDescription(descriptionMission);
        mission.setStatut(statutMission);
        mission.setPresent(isPresent);

        missions.add(mission);
        missionsListView.getItems().add(titreMission + " - " + descriptionMission + " (" + statutMission + ")");

        titreMissionField.clear();
        descriptionMissionField.clear();
        statutMissionComboBox.getSelectionModel().clearSelection();
        isPresentCheckBox.setSelected(false);
    }

    @FXML
    private void enregistrerProjet() {
        String typeProjet = typeProjetField.getText();
        String descriptionProjet = descriptionProjetField.getText();
        String statusProjet = statusProjetComboBox.getValue();
        int utilisateurId;


        if (typeProjet.isEmpty() || descriptionProjet.isEmpty() || statusProjet == null || missions.isEmpty()) {
            showAlert("Erreur de saisie", "Tous les champs du projet et au moins une mission sont requis.");
            return;
        }
        try {
            utilisateurId = Integer.parseInt(utilisateurIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "L'ID utilisateur doit être un nombre.");
            return;
        }


        Projet projet = new Projet();
        projet.setType(typeProjet);
        projet.setDescription(descriptionProjet);
        projet.setStatus(statusProjet);
        projet.setUtilisateurId(utilisateurId);
        System.out.println(statusProjet);
        try {
            int projetId = projetService.insert(projet);
            if (projetId != -1) {
                for (Mission mission : missions) {
                    mission.setProjetId(projetId);
                    missionService.insert(mission);
                }
                showAlert("Succès", "Projet et missions enregistrés avec succès.");
                clearFields();
                loadDetailProjetPage(projet);
            } else {
                showAlert("Erreur", "Erreur lors de l'enregistrement du projet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Erreur lors de l'enregistrement en base de données : " + e.getMessage();
            showAlert("Erreur en base de données", errorMessage);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void clearFields() {
        typeProjetField.clear();
        descriptionProjetField.clear();
        statusProjetComboBox.getSelectionModel().clearSelection();
        utilisateurIdField.clear();
        missionsListView.getItems().clear();
        missions.clear();
    }
    private void loadDetailProjetPage(Projet projet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailProjet.fxml"));
            Parent root = loader.load();

            DetailProjetController detailController = loader.getController();
            detailController.setProjet(projet); // Passer l'objet Projet au contrôleur

            Stage stage = (Stage) typeProjetField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des détails du projet.");
        }
    }

}