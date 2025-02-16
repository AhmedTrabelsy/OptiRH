package tn.nexus.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import tn.nexus.Entities.Evenement;
import tn.nexus.Services.EvenementServices;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ListeEvenementController {

    @FXML
    private DatePicker dateDebutField;

    @FXML
    private DatePicker dateFinField;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TextField descriptionField;

    @FXML
    private TableColumn<?, ?> endDateColumn;

    @FXML
    private TableView<Evenement> eventsTable;

    @FXML
    private TextField heureField;

    @FXML
    private TableColumn<?, ?> imageColumn;

    @FXML
    private TextField imageField;

    @FXML
    private TextField lieuField;

    @FXML
    private TableColumn<?, ?> locationColumn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField prixField;

    @FXML
    private TableColumn<?, ?> startDateColumn;

    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<Evenement, Integer> idColumn;
    @FXML
    private TextField titreField;

    private EvenementServices serviceEvenement = new EvenementServices();


    /*****************Initialisation du tableau ****************************/
    public void initialize() throws SQLException {


        // Associer les colonnes avec les propriétés correspondantes dans la classe Evenement
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        List<Evenement> evenementList = serviceEvenement.showAll(); // Récupère les événements

        //FXCollections.observableArrayList(evenementList) transforme la List<Evenement> en ObservableList<Evenement>.
        //ObservableList est nécessaire pour JavaFX car elle permet d'actualiser automatiquement l'interface graphique lorsque les données changent.
        ObservableList<Evenement> observableEvenementList = FXCollections.observableArrayList(evenementList); // Convertit en ObservableList

        //remplit la TableView avec la liste des événements.
        eventsTable.setItems(observableEvenementList);

        //selection
        eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Mettre à jour les champs avec les données de l'événement sélectionné
                titreField.setText(newValue.getTitre());
                descriptionField.setText(newValue.getDescription());
                lieuField.setText(newValue.getLieu());
                prixField.setText(String.valueOf(newValue.getPrix()));
                dateDebutField.setValue(newValue.getDateDebut());
                dateFinField.setValue(newValue.getDateFin());
                heureField.setText(String.valueOf(newValue.getHeure()));

                imageField.setText(newValue.getImage());
            }
        });



    }

    /*****************Ajout d un evenement avec controle de saisie *********************/
    @FXML
    void ajouterEvenement(ActionEvent event) throws SQLException {


        // Vérification des champs vides
        if (titreField.getText().isEmpty() || lieuField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() || prixField.getText().isEmpty() ||
                heureField.getText().isEmpty() || dateDebutField.getValue() == null ||
                dateFinField.getValue() == null || imageField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Tous les champs doivent être remplis !");
            alert.showAndWait();
            return;
        }

        // Vérification du prix (nombre positif)
        double prix;
        try {
            prix = Double.parseDouble(prixField.getText());
            if (prix < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le prix doit être un nombre positif !");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le prix doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

        // Vérification du format de l'heure
        LocalTime heure;
        try {
            heure = LocalTime.parse(heureField.getText());
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("L'heure doit être au format HH:mm !");
            alert.showAndWait();
            return;
        }

        // Vérification des dates (date de début ne doit pas être avant aujourd'hui)
        LocalDate dateDebut = dateDebutField.getValue();
        if (dateDebut.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("La date de début ne peut pas être antérieure à la date d'aujourd'hui !");
            alert.showAndWait();
            return;
        }

        // Vérification de la cohérence des dates
        LocalDate dateFin = dateFinField.getValue();
        if (dateFin.isBefore(dateDebut)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("La date de fin doit être après la date de début !");
            alert.showAndWait();
            return;
        }



        // Création et ajout de l'événement

        String titre = titreField.getText();
        String lieu = lieuField.getText();
        String description = descriptionField.getText();
        String image = imageField.getText();

        Evenement evenement = new Evenement(titre, lieu, description, prix, dateDebut, dateFin, image, heure);

        EvenementServices evenementServices = new EvenementServices();
        evenementServices.insert(evenement);

        // Rafraîchir la table
        eventsTable.getItems().add(evenement);

        // Réinitialiser les champs après l'ajout
        clearFields();


    }



    /**************************Modifier un evenement ******************************/
    @FXML
    void modifierEvenement(ActionEvent event) {
        Evenement selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Vérification des champs vides
            if (titreField.getText().isEmpty() || lieuField.getText().isEmpty() ||
                    descriptionField.getText().isEmpty() || prixField.getText().isEmpty() ||
                    heureField.getText().isEmpty() || dateDebutField.getValue() == null ||
                    dateFinField.getValue() == null || imageField.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText("Tous les champs doivent être remplis !");
                alert.showAndWait();
                return;
            }

            // Vérification du prix (doit être un nombre positif)
            double prix;
            try {
                prix = Double.parseDouble(prixField.getText());
                if (prix < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le prix doit être un nombre positif !");
                    alert.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le prix doit être un nombre valide !");
                alert.showAndWait();
                return;
            }

            // Vérification du format de l'heure
            LocalTime heure;
            try {
                heure = LocalTime.parse(heureField.getText());
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("L'heure doit être au format HH:mm !");
                alert.showAndWait();
                return;
            }

            // Vérification des dates (date de début ne doit pas être avant aujourd'hui)
            LocalDate dateDebut = dateDebutField.getValue();
            if (dateDebut.isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("La date de début ne peut pas être antérieure à la date d'aujourd'hui !");
                alert.showAndWait();
                return;
            }

            // Vérification de la cohérence des dates (la date de fin doit être après la date de début)
            LocalDate dateFin = dateFinField.getValue();
            if (dateFin.isBefore(dateDebut)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("La date de fin doit être après la date de début !");
                alert.showAndWait();
                return;
            }

            // Mise à jour des données de l'événement sélectionné
            selectedEvent.setTitre(titreField.getText());
            selectedEvent.setLieu(lieuField.getText());
            selectedEvent.setDescription(descriptionField.getText());
            selectedEvent.setPrix(prix);
            selectedEvent.setDateDebut(dateDebut);
            selectedEvent.setDateFin(dateFin);
            selectedEvent.setHeure(heure);
            selectedEvent.setImage(imageField.getText());

            try {
                // Appel à la méthode update pour mettre à jour la base de données
                serviceEvenement.update(selectedEvent);

                // Rafraîchir la TableView après la mise à jour
                eventsTable.refresh();
            } catch (SQLException e) {
                // Gérer l'exception si la mise à jour échoue
                e.printStackTrace();
            }
        } else {
            // Si aucun événement n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Aucun événement sélectionné !");
            alert.showAndWait();
        }
    }

    /***************************Suppression d un evenement**************************/
    @FXML
    void supprimerEvenement(ActionEvent event) {
        Evenement selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                // Appel à la méthode delete avec l'événement sélectionné
                serviceEvenement.delete(selectedEvent);
                // Rafraîchir la TableView après la suppression
                eventsTable.getItems().remove(selectedEvent);
            } catch (SQLException e) {
                // Gérer l'exception si la suppression échoue
                e.printStackTrace();
            }
        }
    }



    /*****************Methode clear*********************/
    @FXML
    private void clearFields() {
        titreField.clear();
        lieuField.clear();
        descriptionField.clear();
        prixField.clear();
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        heureField.clear();
        imageField.clear();
    }



    /*********************choisir image png jpg jpeg******************************/
    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Définir un filtre pour n'afficher que les fichiers image
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Ouvrir la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Récupérer le nom du fichier sélectionné
                String fileName = selectedFile.getName();

                // Définir le chemin de destination dans le dossier "images" du projet
                File destinationDir = new File("src/main/resources/image");
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs(); // Créer le dossier s'il n'existe pas
                }

                File destinationFile = new File(destinationDir, fileName);

                // Copier l'image sélectionnée dans le dossier "images"
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Afficher uniquement le nom du fichier dans le champ de texte
                imageField.setText(fileName);


                System.out.println("Image enregistrée avec succès : " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'enregistrer l'image !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }

}

