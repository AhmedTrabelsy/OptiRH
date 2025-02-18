package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.nexus.Entities.Projet;
import tn.nexus.Service.ProjetService;


import java.sql.SQLException;

public class ModifierProjetController {

    @FXML
    private TextField typeField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private TextField utilisateurIdField;

    private Projet projet;
    private ProjetService projetService = new ProjetService();

    public void setProjet(Projet projet) {
        this.projet = projet;
        remplirChamps();
    }

    private void remplirChamps() {
        if (projet != null) {
            typeField.setText(projet.getType());
            descriptionField.setText(projet.getDescription());
            statutComboBox.getItems().addAll("en cours", "terminé");
            statutComboBox.setValue(projet.getStatus());
            utilisateurIdField.setText(String.valueOf(projet.getUtilisateurId()));
        }
    }

    @FXML
    private void handleEnregistrer() {
        String type = typeField.getText();
        String description = descriptionField.getText();
        String statut = statutComboBox.getValue();
        int utilisateurId;

        try {
            utilisateurId = Integer.parseInt(utilisateurIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre.");
            return;
        }

        if (type.isEmpty() || description.isEmpty() || statut == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        projet.setType(type);
        projet.setDescription(description);
        projet.setStatus(statut);
        projet.setUtilisateurId(utilisateurId);

        try {
            int rowsUpdated = projetService.update(projet);
            if (rowsUpdated > 0) {
                showAlert("Succès", "Projet mis à jour avec succès.");
                fermerFenetre();
            } else {
                showAlert("Erreur", "Aucun projet mis à jour.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du projet.");
        }
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        typeField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}