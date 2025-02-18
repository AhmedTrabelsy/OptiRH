package tn.nexus.Controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.nexus.Entities.Mission;
import tn.nexus.Service.MissionService;


import java.sql.SQLException;

public class ModifierMissionController {

    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private CheckBox isPresentCheckBox;

    private Mission mission;
    private MissionService missionService = new MissionService();

    public void setMission(Mission mission) {
        this.mission = mission;
        remplirChamps();
    }

    private void remplirChamps() {
        if (mission != null) {
            titreField.setText(mission.getTitre());
            descriptionField.setText(mission.getDescription());
            statutComboBox.getItems().addAll("en cours", "terminé");
            statutComboBox.setValue(mission.getStatut());
            isPresentCheckBox.setSelected(mission.isPresent());
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (mission != null) {
            mission.setTitre(titreField.getText());
            mission.setDescription(descriptionField.getText());
            mission.setStatut(statutComboBox.getValue());
            mission.setPresent(isPresentCheckBox.isSelected());

            try {
                missionService.update(mission);
                showAlert("Succès", "Mission mise à jour avec succès.");
                fermerFenetre();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la mise à jour de la mission.");
            }
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