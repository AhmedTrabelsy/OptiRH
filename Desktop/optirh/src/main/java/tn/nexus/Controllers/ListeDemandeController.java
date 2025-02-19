package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.nexus.Entities.Demande;
import tn.nexus.services.DemandeService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ListeDemandeController {

    @FXML private TableView<Demande> tableDemandes;
    @FXML private TableColumn<Demande, Integer> colId;
    @FXML private TableColumn<Demande, Demande.Statut> colStatut;
    @FXML private TableColumn<Demande, String> colDescription;
    @FXML private TableColumn<Demande, String> colDate;
    @FXML private TableColumn<Demande, Void> colActions; // Pour les boutons

    @FXML
    private TableColumn<Demande, String> colFichier;

    private DemandeService demandeService = new DemandeService();

    @FXML
    public void initialize() {
        setupTableColumns();
        chargerDemandes();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox container = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setOnAction(event -> handleModifier(getTableView().getItems().get(getIndex())));
                btnSupprimer.setOnAction(event -> handleSupprimer(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
        colFichier.setCellValueFactory(new PropertyValueFactory<>("fichierPieceJointe"));

        colFichier.setCellFactory(column -> new TableCell<>() {
            final Button downloadButton = new Button("Télécharger");

            @Override
            protected void updateItem(String filePath, boolean empty) {
                super.updateItem(filePath, empty);
                if (empty || filePath == null || filePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    downloadButton.setOnAction(e -> openFile(filePath));
                    setGraphic(downloadButton);
                }
            }
        });
    }

    private void chargerDemandes() {
        try {
            List<Demande> demandes = demandeService.showAll();
            tableDemandes.getItems().setAll(demandes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouter() throws IOException {
        URL fxmlLocation = getClass().getResource("/AjouterDemande.fxml");
        System.out.println("FXML location: " + fxmlLocation);
        if (fxmlLocation == null) {
            throw new IOException("Fichier AjouterDemande.fxml introuvable !");
        }
        System.out.println("Bouton Ajouter cliqué !");
        Parent root = FXMLLoader.load(fxmlLocation);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        chargerDemandes(); // Rafraîchir la liste après ajout
    }

    private void handleModifier(Demande demande) {
        try {
            System.out.println("Modifier la demande : " + demande);

            // Charger l'interface ModifierDemande.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDemande.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et lui passer la demande sélectionnée
            ModifierDemandeController controller = loader.getController();
            controller.setDemande(demande); // Méthode à créer dans le contrôleur

            // Afficher la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Demande");
            stage.showAndWait();

            // Rafraîchir la table après modification
            chargerDemandes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleSupprimer(Demande demande) {
        try {
            demandeService.delete(demande);
            tableDemandes.getItems().remove(demande);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void openFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier introuvable !");
        }
    }

}
