package tn.nexus.Controllers.Recrutement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.nexus.Entities.Recrutement.Offre;
import tn.nexus.Services.Recrutement.OffreService;



import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OffresListController {

    @FXML
    private TextField searchField;
    @FXML
    private VBox offresList;

    @FXML
    private ComboBox<String> modeTravailFilterCombo;
    @FXML
    private ComboBox<String> typeContratFilterCombo;
    @FXML
    private ComboBox<String> niveauExperienceFilterCombo;

    private OffreService offreService = new OffreService();
    private final BooleanProperty emptyList = new SimpleBooleanProperty();

    @FXML
    public void initialize() {

        emptyList.bind(Bindings.isEmpty(offresList.getChildren()));
        loadActiveOffres(); // Charger les offres actives au démarrage


    }
    public BooleanProperty emptyListProperty() {
        return emptyList;
    }

    private void loadActiveOffres() {
        offresList.getChildren().clear();
        try {
            List<Offre> activeOffres = offreService.getOffresActives()
                    .stream()
                    .collect(Collectors.toList());

            for (Offre offre : activeOffres) {
                createOfferCard(offre);
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur de chargement", e.getMessage());
        }
    }

    private void createOfferCard(Offre offre) {
        AnchorPane offrePane = new AnchorPane();
        offrePane.setStyle("-fx-border-color: lightgray; -fx-padding: 10; " +
                "-fx-background-color: #f9f9f9; -fx-border-radius: 5;");
        offrePane.setPrefWidth(800);
        offrePane.setMaxWidth(800);

        // Titre du poste
        Label posteLabel = new Label(offre.getPoste());
        posteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Badge de statut
        Label statutLabel = new Label(offre.getStatut());
        statutLabel.setStyle("-fx-text-fill: white; -fx-background-color: #1a6dda; " +
                "-fx-padding: 2 5; -fx-background-radius: 3;");

        // Détails
        HBox detailsBox = new HBox(15,
                new Label("📍 " + offre.getLocalisation()),
                new Label("📅 " + offre.getDateCreation().toLocalDate()),
                new Label("👥 " + offre.getNbPostes() + " postes"),
                statutLabel
        );
        detailsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox infoBox = new VBox(5, posteLabel, detailsBox);
        AnchorPane.setLeftAnchor(infoBox, 10.0);
        AnchorPane.setTopAnchor(infoBox, 10.0);

        // Bouton Détails
        Button detailsButton = new Button("Détails");
        detailsButton.setOnAction(e -> handleDetails(offre));
        AnchorPane.setRightAnchor(detailsButton, 20.0);
        AnchorPane.setTopAnchor(detailsButton, 20.0);

        offrePane.getChildren().addAll(infoBox, detailsButton);
        offresList.getChildren().add(offrePane);
    }

    /* @FXML
     private void handleSearch() throws SQLException { // Retirez throws SQLException
         offresList.getChildren().clear();
         List<Offre> filtered = offreService.getAllOffres(searchField.getText())
                 .stream()
                 .filter(Offre::isActive)
                 .collect(Collectors.toList());

         filtered.forEach(this::createOfferCard);

     }*/
    @FXML
    private void handleSearchTextChanged() {
        offresList.getChildren().clear(); // On vide la liste à chaque modification du champ
        String searchText = searchField.getText().toLowerCase();
        String selectedModeTravail = modeTravailFilterCombo.getValue();
        String selectedTypeContrat = typeContratFilterCombo.getValue();
        String selectedNiveauExperience = niveauExperienceFilterCombo.getValue();


        List<Offre> filtered = offreService.getAllOffres(searchText).stream()
                .filter(offre -> {
                    // Filtrage par mode de travail
                    if (selectedModeTravail != null && !selectedModeTravail.equals("Tous")) {
                        return offre.getModeTravail().equalsIgnoreCase(selectedModeTravail);
                    }
                    return true;
                })
                .filter(offre -> {
                    // Filtrage par type de contrat
                    if (selectedTypeContrat != null && !selectedTypeContrat.equals("Tous")) {
                        return offre.getTypeContrat().equalsIgnoreCase(selectedTypeContrat);
                    }
                    return true;
                })
                .filter(offre -> {
                    // Filtrage par niveau d'expérience
                    if (selectedNiveauExperience != null && !selectedNiveauExperience.equals("Tous")) {
                        return offre.getNiveauExperience().equalsIgnoreCase(selectedNiveauExperience);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // Affichage des offres filtrées
        filtered.forEach(this::createOfferCard);

        if (filtered.isEmpty()) {
            System.out.println("Aucune offre trouvée");
        }

    }





    // Méthode pour gérer le clic sur le bouton Détails
    private void handleDetails(Offre offre) {
        try {
            // Charger le FXML de la page de détails
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Recrutement/OffreDetails.fxml"));
            URL fxmlUrl = getClass().getResource("/Recrutement/OffreDetails.fxml");
            System.out.println("URL du FXML des détails : " + fxmlUrl);
            Parent root = loader.load();

            // Récupérer le contrôleur et lui transmettre l'offre sélectionnée
            OffreDetailsController detailsController = loader.getController();
            detailsController.setOffre(offre);

            // Créer une nouvelle scène et l'afficher dans une nouvelle fenêtre (Stage)
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Détails de l'offre");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page de détails", e.getMessage());
        }
    }


    // Méthode utilitaire pour afficher les alertes
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            // 1. Charger le FXML de login
            Parent root = FXMLLoader.load(getClass().getResource("/Auth/login.fxml"));

            // 2. Créer une nouvelle scène et une nouvelle fenêtre
            Scene loginScene = new Scene(root);
            Stage loginStage = new Stage();

            // 3. Configurer la fenêtre
            loginStage.setTitle("Connexion - Nexus Recrutement");
            loginStage.setScene(loginScene);

            // 4. Personnalisation optionnelle
            loginStage.setResizable(false); // Fenêtre non redimensionnable
            loginStage.initModality(Modality.NONE); // Autoriser l'interaction avec les autres fenêtres

            // 5. Afficher la nouvelle fenêtre
            loginStage.show();

        } catch (IOException e) {
            // Gestion d'erreur avec boîte de dialogue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur technique");
            alert.setHeaderText("Échec du chargement de la page de connexion");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}

