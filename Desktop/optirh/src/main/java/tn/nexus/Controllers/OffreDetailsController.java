package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.nexus.Entities.Offre;

public class OffreDetailsController {

    @FXML
    private Label posteLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label statutLabel;
    @FXML
    private Label dateCreationLabel;
    @FXML
    private Label modeTravailLabel;
    @FXML
    private Label typeContratLabel;
    @FXML
    private Label localisationLabel;
    @FXML
    private Label niveauExperienceLabel;
    @FXML
    private Label nbPostesLabel;

    private Offre offre;

    // Méthode pour recevoir l'offre et mettre à jour l'affichage
    public void setOffre(Offre offre) {
        this.offre = offre;
        posteLabel.setText(offre.getPoste());
        descriptionLabel.setText(offre.getDescription());
        statutLabel.setText(offre.getStatut());
        dateCreationLabel.setText(offre.getDateCreation().toLocalDate().toString());
        modeTravailLabel.setText(offre.getModeTravail());
        typeContratLabel.setText(offre.getTypeContrat());
        localisationLabel.setText(offre.getLocalisation());
        niveauExperienceLabel.setText(offre.getNiveauExperience());
        nbPostesLabel.setText("Postes disponibles : " + offre.getNbPostes());
    }

    // Gestion du bouton Retour
    @FXML
    private void handleBack(ActionEvent event) {
        // Ferme la fenêtre des détails
        Stage stage = (Stage) posteLabel.getScene().getWindow();
        stage.close();
    }

    // Gestion du bouton Postuler
    @FXML
    private void handlePostuler(ActionEvent event) {
        try {
            // Charger le FXML de la page Front_AjouterDemande.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front_AjouterDemande.fxml"));
            Parent root = loader.load();
            // Si besoin, récupérer le contrôleur d'AjouterDemande pour transmettre des infos
            // Exemple : AjouterDemandeController demandeController = loader.getController();
            // demandeController.setOffre(offre);

            // Créer et afficher la nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Postuler à l'offre");
            stage.setScene(scene);
            stage.show();

            // Optionnel : fermer la fenêtre des détails
            // ((Stage) posteLabel.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            // Tu peux ajouter un message d'alerte ici si besoin
        }
    }
}
