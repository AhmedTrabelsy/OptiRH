package tn.nexus.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import tn.nexus.Entities.Evenement;
import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Services.Reservation_evenementServices;
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
    private TableColumn<Reservation_evenement, ?> DateReservationColumn;


    @FXML
    private TableColumn<Reservation_evenement, ?> EmailColumn;

    @FXML
    private TableColumn<Reservation_evenement, ?> FirstnameColumn;



    @FXML
    private TableColumn<Reservation_evenement, ?> LastNameColumn;

    @FXML
    private TableColumn<Reservation_evenement, ?> PhoneColumn;

    @FXML
    private DatePicker dateDebutField;

    @FXML
    private DatePicker dateFinField;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TextArea descriptionField;  // Utilisez TextArea au lieu de TextField


    @FXML
    private TableColumn<?, ?> endDateColumn;

    @FXML
    private TableView<Evenement> eventsTable;

    @FXML
    private TableView<Reservation_evenement> Reservation_Tab;

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
    private TableColumn<Reservation_evenement, String> titre1;


    @FXML
    private TextField titreField;

    //instance du serviceevenement et de servicereservation
    private EvenementServices serviceEvenement = new EvenementServices();
    private Reservation_evenementServices servicereservation = new Reservation_evenementServices();


    /*****************Initialisation du tableau ****************************/
    public void initialize() {
        try {
            // Initialisation des colonnes pour les réservations
            List<Reservation_evenement> reservationList = servicereservation.showAll();
            titre1.setCellValueFactory(new PropertyValueFactory<>("titre"));
            FirstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            DateReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));

            // Récupération des réservations depuis le service
           System.out.println(reservationList);
            // Vérification si la liste des réservations n'est pas vide
            if (reservationList != null && !reservationList.isEmpty()) {
                ObservableList<Reservation_evenement> observableList = FXCollections.observableArrayList(reservationList);
                Reservation_Tab.setItems(observableList);
            } else {
                System.out.println("Aucune réservation trouvée.");
            }

            // Initialisation des colonnes pour les événements

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Récupération des événements depuis le service
            List<Evenement> evenementList = serviceEvenement.showAll();

            // Vérification si la liste des événements n'est pas vide
            if (evenementList != null && !evenementList.isEmpty()) {
                ObservableList<Evenement> observableEvenementList = FXCollections.observableArrayList(evenementList);
                eventsTable.setItems(observableEvenementList);
            } else {
                System.out.println("Aucun événement trouvé.");
            }

            // Gestion de la sélection des événements
            eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
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

        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur pour le débogage
        }
    }


    /*****************Ajout d un evenement avec controle de saisie *********************/
    @FXML
    void ajouterEvenement(ActionEvent event) throws SQLException {

        // Vérification des champs vides
        if (titreField.getText().isEmpty() || lieuField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() || prixField.getText().isEmpty() ||
                heureField.getText().isEmpty() || dateDebutField.getValue() == null ||
                dateFinField.getValue() == null || imageField.getText().isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Avertissement", "Tous les champs doivent être remplis !", null);
            return;
        }

        // Vérification du titre
        String titretest = titreField.getText();
        if (titretest.length() > 10) {
            showAlert(Alert.AlertType.ERROR, "Avertissement", "Le titre ne doit pas dépasser 10 caractères.", null);
            return;
        }
        if (!titretest.matches("[a-zA-Z0-9 ]+")) {
            showAlert(Alert.AlertType.ERROR, "Avertissement", "Le titre ne doit contenir que des lettres et des chiffres.", null);
            return;
        }

// Vérification du lieu
        String lieutest = lieuField.getText();
        if (lieutest.length() > 10) {
            showAlert(Alert.AlertType.ERROR, "Avertissement", "Le lieu ne doit pas dépasser 10 caractères.", null);
            return;
        }
        if (!lieutest.matches("[a-zA-Z0-9 ]+")) {
            showAlert(Alert.AlertType.ERROR, "Avertissement", "Le lieu ne doit contenir que des lettres et des chiffres.", null);
            return;
        }

        // Vérification du prix (nombre positif)
        double prix;
        try {
            prix = Double.parseDouble(prixField.getText());
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le prix doit être un nombre valide !");
            return;
        }

        // Vérification du format de l'heure
        LocalTime heure;
        try {
            heure = LocalTime.parse(heureField.getText());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "L'heure doit être au format HH:mm !");
            return;
        }

        // Vérification des dates (date de début ne doit pas être avant aujourd'hui)
        LocalDate dateDebut = dateDebutField.getValue();
        if (dateDebut.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "La date de début ne peut pas être antérieure à la date d'aujourd'hui !");
            return;
        }

        // Vérification de la cohérence des dates
        LocalDate dateFin = dateFinField.getValue();
        if (dateFin.isBefore(dateDebut)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "La date de fin doit être après la date de début !");
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

                showAlert(Alert.AlertType.WARNING, "Avertissement", "Tous les champs doivent être remplis !", null);
                return;
            }

            // Vérification du prix (doit être un nombre positif)
            double prix;
            try {
                prix = Double.parseDouble(prixField.getText());
                if (prix < 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le prix doit être un nombre positif !");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le prix doit être un nombre valide !");
                return;
            }

            // Vérification du titre
            String titretest = titreField.getText();
            if (titretest.length() > 10) {
                showAlert(Alert.AlertType.ERROR, "Avertissement", "Le titre ne doit pas dépasser 10 caractères.", null);
                return;
            }
            if (!titretest.matches("[a-zA-Z0-9 ]+")) {
                showAlert(Alert.AlertType.ERROR, "Avertissement", "Le titre ne doit contenir que des lettres et des chiffres.", null);
                return;
            }

// Vérification du lieu
            String lieutest = lieuField.getText();
            if (lieutest.length() > 10) {
                showAlert(Alert.AlertType.ERROR, "Avertissement", "Le lieu ne doit pas dépasser 10 caractères.", null);
                return;
            }
            if (!lieutest.matches("[a-zA-Z0-9 ]+")) {
                showAlert(Alert.AlertType.ERROR, "Avertissement", "Le lieu ne doit contenir que des lettres et des chiffres.", null);
                return;
            }

            // Vérification du format de l'heure
            LocalTime heure;
            try {
                heure = LocalTime.parse(heureField.getText());
            } catch (DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "L'heure doit être au format HH:mm !");
                return;
            }

            // Vérification des dates (date de début ne doit pas être avant aujourd'hui)
            LocalDate dateDebut = dateDebutField.getValue();
            if (dateDebut.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "La date de début ne peut pas être antérieure à la date d'aujourd'hui !");
                return;
            }

            // Vérification de la cohérence des dates (la date de fin doit être après la date de début)
            LocalDate dateFin = dateFinField.getValue();
            if (dateFin.isBefore(dateDebut)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", null, "La date de fin doit être après la date de début !");
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
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun événement sélectionné !", null);
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

    /**************Alerte**********************/
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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

                imageField.setText(selectedFile.getAbsolutePath());
                System.out.println("Image selectionnée: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }



}

