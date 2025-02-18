package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.nexus.Entities.Mission;
import tn.nexus.Service.MissionService;

import java.sql.SQLException;

public class AjouterMissionController {

    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private CheckBox isPresentCheckBox;

    private MissionService missionService = new MissionService();
    private int projetId; // ID du projet auquel la mission est associée

    public void setProjetId(int projetId) {
        this.projetId = projetId;
        initialiserChamps();
    }

    private void initialiserChamps() {
        statutComboBox.getItems().addAll("en cours", "terminé");
        statutComboBox.setValue("en cours"); // Valeur par défaut
    }

    @FXML
    private void handleEnregistrer() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String statut = statutComboBox.getValue();
        boolean isPresent = isPresentCheckBox.isSelected();

        if (titre.isEmpty() || description.isEmpty() || statut == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        Mission mission = new Mission();
        mission.setTitre(titre);
        mission.setDescription(description);
        mission.setStatut(statut);
        mission.setPresent(isPresent);
        mission.setProjetId(projetId);

        try {
            int missionId = missionService.insert(mission);
            if (missionId != -1) {
                showAlert("Succès", "Mission ajoutée avec succès.");
                fermerFenetre();
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout de la mission.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout de la mission.");
        }
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        titreField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}