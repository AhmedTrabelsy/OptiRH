package tn.nexus.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import tn.nexus.Entities.Offre;
import tn.nexus.services.OffreService;

public class OffresController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Offre> tableOffres;

    @FXML
    private TableColumn<Offre, String> colPoste;

    @FXML
    private TableColumn<Offre, String> colDescription;

    @FXML
    private TableColumn<Offre, String> colStatut;

    @FXML
    private TableColumn<Offre, String> colDate;
    @FXML
    private TableColumn<Offre, String> colActions;



    private OffreService offreService = new OffreService();
    private ObservableList<Offre> offresList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lier les colonnes aux propriétés de l'objet Offre
        colPoste.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPoste()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut()));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCreation().toString()));

        // Dans OffresController.java, dans la méthode initialize()
        colActions.setCellFactory(col -> new TableCell<Offre, String>() {
            private final Button editButton = new Button("Editer");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setSpacing(10);
                // Quand on clique sur le bouton Editer
                editButton.setOnAction(e -> {
                    // Sélectionner la ligne correspondante
                    getTableView().getSelectionModel().select(getIndex());
                    // Appeler la méthode déjà existante pour éditer l'offre
                    handleEditOffer();
                });

                // Quand on clique sur le bouton Supprimer
                deleteButton.setOnAction(e -> {
                    getTableView().getSelectionModel().select(getIndex());
                    // Appeler la méthode déjà existante pour supprimer l'offre
                    handleDeleteOffer();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });


        // Charger toutes les offres depuis la base de données
        loadOffres();
    }

    private void loadOffres() {
        try {
            List<Offre> offres = offreService.showAll();
            offresList.setAll(offres);
            tableOffres.setItems(offresList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des offres.", e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();

        // Contrôle de saisie : vérifier que le champ n'est pas vide
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Champ de recherche vide", "Veuillez saisir un poste à rechercher.");
            // Réinitialiser la table avec toutes les offres
            tableOffres.setItems(offresList);
            return;
        }

        // Filtrer la liste des offres en fonction du critère saisi (exemple : le nom du poste)
        ObservableList<Offre> filteredList = offresList.filtered(offre ->
                offre.getPoste().toLowerCase().contains(query.toLowerCase())
        );

        if (filteredList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aucune offre", "Aucune offre trouvée", "Aucune offre ne correspond à votre recherche.");
        }

        tableOffres.setItems(filteredList);
    }

    @FXML
    private void handleAddOffer() {
        try {
            // Charge le fichier FXML de la page d'ajout d'offre
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutOffre.fxml"));
            Scene scene = new Scene(root);
            // Récupère la fenêtre actuelle pour y placer la nouvelle scène
            Stage stage = (Stage) tableOffres.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement de la page d'ajout", ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteOffer() {
        Offre selectedOffre = tableOffres.getSelectionModel().getSelectedItem();

        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection invalide", "Aucune offre sélectionnée", "Veuillez sélectionner une offre à supprimer.");
            return;
        }

        try {
            int result = offreService.delete(selectedOffre);
            if (result > 0) {
                offresList.remove(selectedOffre); // Retirer l'offre de la liste
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Suppression réussie", "L'offre a été supprimée.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Suppression échouée", "Une erreur est survenue lors de la suppression.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression", e.getMessage());
        }
    }

    @FXML
    private void handleEditOffer() {
        Offre selectedOffre = tableOffres.getSelectionModel().getSelectedItem();

        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection invalide", "Aucune offre sélectionnée", "Veuillez sélectionner une offre à modifier.");
            return;
        }

        try {
            // Charge la page d'édition avec l'offre sélectionnée
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditOffre.fxml"));
            Parent root = loader.load();
            EditOffreController controller = loader.getController();
            controller.initData(selectedOffre);  // Initialisation avec l'offre à modifier

            Scene scene = new Scene(root);
            Stage stage = (Stage) tableOffres.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement de la page de modification", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
