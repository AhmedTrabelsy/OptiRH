package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.nexus.Entities.Offre;
import tn.nexus.services.OffreService;

import java.io.IOException;
import java.sql.SQLException;

public class EditOffreController {

    @FXML
    private TextField posteField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private DatePicker dateCreationPicker;

    private OffreService offreService = new OffreService();
    private Offre currentOffre;

    // Initialisation des données de l'offre sélectionnée
    public void initData(Offre offre) {
        this.currentOffre = offre;
        posteField.setText(offre.getPoste());
        descriptionArea.setText(offre.getDescription());
        statutComboBox.setValue(offre.getStatut());
        dateCreationPicker.setValue(offre.getDateCreation().toLocalDate());
    }

    // Sauvegarder les modifications
    @FXML
    private void handleSave() {
        // Récupérer les nouvelles valeurs
        currentOffre.setPoste(posteField.getText());
        currentOffre.setDescription(descriptionArea.getText());
        currentOffre.setStatut(statutComboBox.getValue());
        currentOffre.setDateCreation(dateCreationPicker.getValue().atStartOfDay());

        try {
            // Mettre à jour l'offre dans la base de données
            offreService.update(currentOffre);
            showAlert(Alert.AlertType.INFORMATION, "Mise à jour réussie", "L'offre a été mise à jour avec succès", "");

            // Rediriger vers la page Offres
            redirectToOffresPage();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de l'offre", e.getMessage());        }
    }

    // Annuler et revenir à la liste des offres
    @FXML
    private void handleCancel() {
        // Rediriger directement vers la page Offres
        redirectToOffresPage();
    }
    // Méthode utilitaire pour charger Offres.fxml et remplacer la scène actuelle
    private void redirectToOffresPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Offres.fxml"));
            Stage stage = (Stage) posteField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page Offres", e.getMessage());
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
