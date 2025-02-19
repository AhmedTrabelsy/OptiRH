package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.nexus.Entities.Demande;
import tn.nexus.Entities.Offre;
import tn.nexus.services.DemandeService;
import tn.nexus.services.OffreService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AjouterDemandeController {

    @FXML private TextArea txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Offre> comboOffre;  // Ajout de la ComboBox pour les offres
    @FXML private TextField txtUtilisateurId;
    @FXML private Label fileLabel;
    private File selectedFile;

    private final DemandeService demandeService = new DemandeService();
    private final OffreService offreService = new OffreService(); // Service pour récupérer les offres

    @FXML
    private void initialize() {

        // Charger les offres depuis la base de données
        try {
            OffreService offreService = new OffreService();
            List<Offre> offres = offreService.showAll();
            comboOffre.getItems().setAll(offres);  // Ajouter les offres dans la ComboBox
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception (par exemple, afficher un message d'erreur)
        }    }

    /**
     * Charge la liste des offres depuis la base de données et l'affiche dans la ComboBox.
     */
    private void chargerOffres() {
        List<Offre> offres = offreService.getAllOffres(); // Récupération des offres (implémente cette méthode)
        comboOffre.getItems().setAll(offres);
    }

    @FXML
    private void handleEnregistrer() {
        String description = txtDescription.getText();
        LocalDate date = datePicker.getValue();
        Offre offreSelectionnee = comboOffre.getValue(); // Récupérer l'offre sélectionnée

        // Vérifier qu'une offre est sélectionnée
        if (offreSelectionnee == null) {
            System.out.println("Erreur : Veuillez sélectionner une offre.");
            return;
        }

        // Récupérer l'ID de l'utilisateur et le convertir en int
        int utilisateurId;
        try {
            utilisateurId = Integer.parseInt(txtUtilisateurId.getText());
        } catch (NumberFormatException e) {
            System.out.println("Erreur : l'ID utilisateur doit être un nombre valide.");
            return;
        }

        // Création de la demande avec le statut "En attente" par défaut
        Demande demande = new Demande(Demande.Statut.EN_ATTENTE, description, java.sql.Date.valueOf(date), utilisateurId);
        demande.setOffreId(offreSelectionnee.getId()); // Associer l'offre sélectionnée

        // Ajouter la pièce jointe si un fichier a été sélectionné
        if (selectedFile != null) {
            demande.setFichierPieceJointe(selectedFile.getAbsolutePath()); // Enregistre le chemin du fichier (à adapter selon ta BD)
        }

        try {
            demandeService.insert(demande);
            fermerFenetre();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    @FXML
    private void handleFileUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            fileLabel.setText(selectedFile.getName());
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtDescription.getScene().getWindow();
        stage.close();
    }
}
