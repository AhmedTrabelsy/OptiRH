package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.nexus.Entities.Demande;
import tn.nexus.Entities.Offre;
import tn.nexus.services.DemandeService;
import tn.nexus.services.OffreService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterDemandeController {

    @FXML private TextArea txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Offre> comboOffre;
    @FXML private TextField txtUtilisateurId;
    @FXML private TextField txtNomComplet;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtAdresse;
    @FXML private DatePicker dateDebutDisponiblePicker;
    @FXML private ComboBox<String> comboSituationActuelle; // Changement ici
    @FXML private Label fileLabel;

    private File selectedFile;
    private final DemandeService demandeService = new DemandeService();
    private final OffreService offreService = new OffreService();

    @FXML
    private void initialize() {
        chargerOffres();
        chargerSituationActuelle(); // Appel à la méthode pour charger les options de situation actuelle
        setDefaultDate();  // Appel à la méthode pour définir la date actuelle par défaut
    }


    private void chargerOffres() {
        try {
            List<Offre> offres = offreService.showAll();
            comboOffre.getItems().setAll(offres);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des offres : " + e.getMessage());
        }
    }

    private void chargerSituationActuelle() {
        // Ajouter les options à la ComboBox pour la situation actuelle
        comboSituationActuelle.getItems().addAll(
                "Employé",
                "Sans emploi",
                "Travailleur indépendant",
                "Étudiant",
                "Autre"
        );
    }
    private void setDefaultDate() {
        datePicker.setValue(LocalDate.now());  // Définit la date actuelle dans le DatePicker
        datePicker.setEditable(false);  // Désactive la possibilité de modifier la date manuellement
    }

    @FXML
    private void handleEnregistrer() {
        String description = txtDescription.getText().trim();
        LocalDate date = datePicker.getValue();
        Offre offreSelectionnee = comboOffre.getValue();
        int utilisateurId;
        String nomComplet = txtNomComplet.getText().trim();
        String email = txtEmail.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();
        LocalDate dateDebutDisponible = dateDebutDisponiblePicker.getValue();
        String situationActuelle = comboSituationActuelle.getValue(); // Modification ici pour récupérer la valeur du ComboBox

        // Validation des champs
        if (description.isEmpty() || description.length() < 10) {
            showError("La description doit contenir au moins 10 caractères.");
            return;
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            showError("Veuillez sélectionner une date valide (aujourd'hui ou plus tard).");
            return;
        }
        if (offreSelectionnee == null) {
            showError("Veuillez sélectionner une offre.");
            return;
        }
        try {
            utilisateurId = Integer.parseInt(txtUtilisateurId.getText().trim());
        } catch (NumberFormatException e) {
            showError("L'ID utilisateur doit être un nombre valide.");
            return;
        }

        if (nomComplet.isEmpty() || !isNameValid(nomComplet)) {
            showError("Le nom complet doit être valide (lettres et espaces seulement).");
            return;
        }

        if (email.isEmpty() || !isEmailValid(email)) {
            showError("L'email n'est pas valide.");
            return;
        }
        if (telephone.isEmpty() || !isPhoneValid(telephone)) {
            showError("Le numéro de téléphone doit être valide.");
            return;
        }
        if (adresse.isEmpty() || situationActuelle == null) {
            showError("Tous les champs doivent être remplis.");
            return;
        }

        if (dateDebutDisponible == null) {
            showError("Veuillez sélectionner une date de début disponible.");
            return;
        }

        // Création de la demande
        Demande demande = new Demande(
                utilisateurId,
                offreSelectionnee.getId(),
                Demande.Statut.EN_ATTENTE,  // Statut par défaut
                Timestamp.valueOf(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime()),
                description,
                selectedFile != null ? selectedFile.getAbsolutePath() : null,
                nomComplet,
                email,
                telephone,
                adresse,
                Date.valueOf(dateDebutDisponible),
                situationActuelle
        );

        try {
            demandeService.insert(demande);
            showSuccess("La demande a été enregistrée avec succès.");
            redirectToListeOffres();
        } catch (SQLException e) {
            showError("Erreur lors de l'enregistrement de la demande : " + e.getMessage());
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
    private boolean isEmailValid(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isPhoneValid(String phone) {
        String phonePattern = "^[0-9]{8}$";  // Exemple pour un numéro de 10 chiffres
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    private boolean isNameValid(String name) {
        String namePattern = "^[A-Za-z ]+$";  // Seulement des lettres et des espaces
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }



    private void fermerFenetre() {
        Stage stage = (Stage) txtDescription.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToListeOffres() {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Front_OffresList.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) txtDescription.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page des offres : " + e.getMessage());
        }
    }
}
